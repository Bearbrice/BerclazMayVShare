/*
 * Project VSShare, AcceptClients
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 03.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptClients implements Runnable {

	static Logger myLogger;

	private Socket clientSocketOnServer;
	private int clientNumber;
	boolean stay = true;
	Scanner scan = new Scanner(System.in);

	String loginReceived;

	String actions = "Here are the following actions :\n" + "1. Upload a file\n"
			+ "2. Display list of accessible files\n" + "3. Delete a file\n" + "4. Download a file from the server\n"
			+ "[SHARE] 5. Copy a local file to the share\n" + "[SHARE] 6. Display available files on the share\n"
			+ "[SHARE] 7. Delete a file from the share\n" + "[SHARE] 8. Download a file from the share\n"
			+ "9. Quit the server\n" + "Please, enter 1,2,3,4 or 5 to perform an action : ";

	public AcceptClients(Socket clientSocketOnServer, int clientCpt, Logger myLogger) {
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientCpt;
		this.myLogger = myLogger;
	}

	public void run() {

		/* CONNECTION FROM A CLIENT */
		myLogger.log(Level.INFO, "---> Client number " + clientNumber + " is connected. <---");

		try {
			PrintWriter pwFirst = new PrintWriter(clientSocketOnServer.getOutputStream(), true);
			BufferedReader serverMessage = new BufferedReader(
					new InputStreamReader(clientSocketOnServer.getInputStream()));

			String welcome = "What do you want to do ? :\n" + "1. Register a new account\n"
					+ "2. Connect to an existing account";
			pwFirst.println(welcome);

			String clientChoice = serverMessage.readLine();
			int userChoice = Integer.parseInt(clientChoice);

			switch (userChoice) {
			case 1:
				/* --- REGISTRATION --- */
				String newLogin = "Choose a username";
				pwFirst.println(newLogin);
				String newLoginReceived = serverMessage.readLine();

				String newPwd = "Choose a password";
				pwFirst.println(newPwd);
				String newPwdReceived = serverMessage.readLine();

				String path = ".\\VSShareCloud\\Users.txt";

				// Add a blank line
				append(path, "");

				// Login and password
				append(path, newLoginReceived);
				append(path, newPwdReceived);
				myLogger.log(Level.INFO, "New user added in DB : " + newLoginReceived);

				String newUserFolder = ".\\VSShareCloud\\" + newLoginReceived;
				new File(newUserFolder).mkdirs();
				myLogger.log(Level.INFO, "New folder created for the new user : " + newLoginReceived);

				createTxt(newLoginReceived);

				// Tell the client he is connected
				pwFirst.println("Success");
				break;
			case 2:
				/* --- CONNEXION --- */
				String login = "Enter your username";
				pwFirst.println(login);
				loginReceived = serverMessage.readLine();

				String pwd = "Enter your password";
				pwFirst.println(pwd);
				String pwdReceived = serverMessage.readLine();

				// Check if login is correct or not
				File users = new File(".\\VSShareCloud\\Users.txt");

				BufferedReader br = new BufferedReader(new FileReader(users));

				Boolean isCorrect = false;
				String line;

				while ((line = br.readLine()) != null) {
					if (line.equals(loginReceived)) {
						// read next line
						String x = br.readLine();
						if (x.equals(pwdReceived)) {
							isCorrect = true;
							pwFirst.println("Success");
							myLogger.log(Level.INFO, "User connection accepted for : " + loginReceived);
							break;
						}
					}
				}

				if (isCorrect == false) {
					pwFirst.println("Refused");
					myLogger.log(Level.WARNING, "Failed to connect with the username : " + loginReceived);
					clientSocketOnServer.close();
					return;
				}
			}

			pwFirst.println(actions);

			String choice = serverMessage.readLine();
			int choosen = Integer.parseInt(choice);

			executeAction(choosen);

		} catch (Exception e) {
			// e.printStackTrace();
			myLogger.log(Level.SEVERE, "Failed to receive menu choice from client : " + loginReceived
					+ " or the connection to the client crashed.");
		}
	}

	public void performAction() {
		PrintWriter pwFirst;
		BufferedReader serverMessage;

		try {
			pwFirst = new PrintWriter(clientSocketOnServer.getOutputStream(), true);

			serverMessage = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream()));

			// It is a function to prevent the program to go too fast and let the client
			// interract
			try {
				Thread.sleep(1500);

			} catch (InterruptedException e) {
				myLogger.log(Level.SEVERE, "Thread failed to sleep.");
				e.printStackTrace();
			}

			pwFirst.println(actions);

			String choice = serverMessage.readLine();
			int choosen = Integer.parseInt(choice);

			executeAction(choosen);

			clientSocketOnServer.close();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.print("performAction error");
		}
	}

	public void createTxt(String login) {
		String path = ".\\VSShareCloud\\" + login + "\\PWD.txt";

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.close();
	}

	// DEV BRICE
	public void executeAction(int choosen) {
		switch (choosen) {
		// Receive a file
		case 1:
			@SuppressWarnings("unused")
			ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Send the list of file
		case 2:
			@SuppressWarnings("unused")
			SendList sl1 = new SendList(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Delete a file
		case 3:
			SendList sl2 = new SendList(clientSocketOnServer, loginReceived, myLogger);
			DeleteFile df = new DeleteFile(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Send a file to the client
		case 4:
			SendList sl3 = new SendList(clientSocketOnServer, loginReceived, myLogger);
			ThrowAFile taf = new ThrowAFile(clientSocketOnServer, loginReceived, false);
			performAction();
			break;
		// Share a file
		case 5:
			SendList sl4 = new SendList(clientSocketOnServer, loginReceived, myLogger);
			CopyAFileInShared cafis = new CopyAFileInShared(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Display list share
		case 6:
			@SuppressWarnings("unused")
			SendShareList shl1 = new SendShareList(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Delete from the share
		case 7:

			SendShareList shl2 = new SendShareList(clientSocketOnServer, loginReceived, myLogger);
			DeleteSharedFile dsf = new DeleteSharedFile(clientSocketOnServer, loginReceived, myLogger);
			// loginReceived);

			performAction();
			break;
		// Download a file from the share
		case 8:
			SendShareList shl3 = new SendShareList(clientSocketOnServer, loginReceived, myLogger);
			ThrowAFile taf2 = new ThrowAFile(clientSocketOnServer, loginReceived, true);
			performAction();
			break;
		// End of the program
		case 9:
			try {
				clientSocketOnServer.close();
			} catch (IOException e) {
				myLogger.log(Level.SEVERE, "User " + loginReceived + " has failed to close the connection.");
			}
			myLogger.log(Level.WARNING, "User " + loginReceived + " has closed the connection.");
			break;

		}
	}

	public static void append(String filename, String text) {
		BufferedWriter bufWriter = null;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename, true);
			bufWriter = new BufferedWriter(fileWriter);
			// Insert a line break
			bufWriter.newLine();
			bufWriter.write(text);
			bufWriter.close();
		} catch (IOException ex) {
			myLogger.log(Level.SEVERE, "Method append - Failed to do files list");
		} finally {
			try {
				bufWriter.close();
				fileWriter.close();
			} catch (IOException ex) {
				myLogger.log(Level.SEVERE, "Method append - Failed to close the writers");
			}
		}
	}
}
