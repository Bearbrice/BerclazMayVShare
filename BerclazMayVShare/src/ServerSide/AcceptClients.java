/*
 * Project VSShare, AcceptClients
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 07.01.2020
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

/**
 * Class that execute action on the server according to the customer's wishes
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class AcceptClients implements Runnable {

	static Logger myLogger;

	private Socket clientSocketOnServer;
	private int clientNumber;
	boolean stay = true;
	Scanner scan = new Scanner(System.in);

	String loginReceived;

	/* List of the actions available */
	String actions = "Here are the actions available :\n" + "[USER] 1. Upload a file " + "\t \t \t \t"
			+ "[SHARE] 6. Copy one of your file to the share \n" + "[USER] 2. Display list of accessible files"
			+ "\t \t" + "[SHARE] 7. Display available files on the share \n" + "[USER] 3. Delete a file "
			+ "\t \t \t \t" + "[SHARE] 8. Delete a file from the share \n"
			+ "[USER] 4. Download a file from the server " + "\t \t" + "[SHARE] 9. Download a file from the share \n"
			+ "[USER] 5. Quit the server\n" + "Please, enter a number from 1 to 9 to perform an action : ";

	/**
	 * Constructor
	 * 
	 * @param clientSocketOnServer
	 * @param clientCpt
	 * @param myLogger
	 */
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

			// Sending the welcome message (to connect or register)
			String welcome = "What do you want to do ? :\n" + "1. Register a new account\n"
					+ "2. Connect to an existing account";
			pwFirst.println(welcome);

			// Reading the client choice
			String clientChoice = serverMessage.readLine();
			int userChoice = Integer.parseInt(clientChoice);

			/* Switch according the choice (Registration or connection) */
			switch (userChoice) {

			/* --- REGISTRATION --- */
			case 1:
				// Sending a message to the client
				String newLogin = "Choose a username";
				pwFirst.println(newLogin);

				// Reading the username sent
				String newLoginReceived = serverMessage.readLine();

				// Sending a message to choose the password
				String newPwd = "Choose a password";
				pwFirst.println(newPwd);

				// Reading the password sent
				String newPwdReceived = serverMessage.readLine();

				// Setting the path (file which contain the users accounts)
				String path = ".\\VSShareCloud\\Users.txt";

				// Add a blank line
				append(path, "");

				// Add the login and password to the file
				append(path, newLoginReceived);
				append(path, newPwdReceived);
				myLogger.log(Level.INFO, "New user added in DB : " + newLoginReceived);

				// Creating a folder on the server for the new user
				String newUserFolder = ".\\VSShareCloud\\" + newLoginReceived;
				new File(newUserFolder).mkdirs();
				myLogger.log(Level.INFO, "New folder created for the new user : " + newLoginReceived);

				// Now the client is connected so we switch him as a current user
				loginReceived = newLoginReceived;

				// Tell the client he is connected
				pwFirst.println("Success");
				break;

			/* --- CONNEXION --- */
			case 2:
				// Ask the username
				String login = "Enter your username";
				pwFirst.println(login);

				// Reading the username sended
				loginReceived = serverMessage.readLine();

				// Ask the password
				String pwd = "Enter your password";
				pwFirst.println(pwd);

				// Reading the password sent
				String pwdReceived = serverMessage.readLine();

				// Setting the txt file of user account
				File users = new File(".\\VSShareCloud\\Users.txt");
				BufferedReader br = new BufferedReader(new FileReader(users));
				Boolean isCorrect = false;
				String line;

				// Reading the txt file of user account
				while ((line = br.readLine()) != null) {

					// Testing if the username sent match
					if (line.equals(loginReceived)) {
						// read next line
						String x = br.readLine();
						// Testing if the password sent match
						if (x.equals(pwdReceived)) {
							isCorrect = true;
							pwFirst.println("Success");
							myLogger.log(Level.INFO, "User connection accepted for : " + loginReceived);
							break;
						}
					}
				}

				/*
				 * After the loop, if the username and/or password doesn't match, the client is
				 * kicked
				 */
				if (isCorrect == false) {
					pwFirst.println("Refused");
					myLogger.log(Level.WARNING, "Failed to connect with the username : " + loginReceived);
					clientSocketOnServer.close();
					return;
				}
			}

			// Sending to the client the actions available
			pwFirst.println(actions);

			// Reading the client choice
			String choice = serverMessage.readLine();
			int choosen = Integer.parseInt(choice);

			// Execute an action according to the choice
			executeAction(choosen);

		} catch (Exception e) {
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

			/*
			 * It is a function to prevent the program to go too fast and let the client
			 * interact
			 */
			try {
				Thread.sleep(1500);

			} catch (InterruptedException e) {
				myLogger.log(Level.SEVERE, "Thread failed to sleep.");
			}

			// Sending back the actions available
			pwFirst.println(actions);

			// Reading the client choice
			String choice = serverMessage.readLine();
			int choosen = Integer.parseInt(choice);

			// Execute an action according to the choice
			executeAction(choosen);

			clientSocketOnServer.close();

		} catch (IOException e) {
			myLogger.log(Level.SEVERE, "performAction error");
		}
	}

	/**
	 * Method which execute the action according to the choice of the client
	 * 
	 * @param choosen
	 */
	public void executeAction(int choosen) {
		@SuppressWarnings("unused")
		SendList sl;

		switch (choosen) {
		// Receive a file
		case 1:
			@SuppressWarnings("unused")
			ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Send the list of file
		case 2:
			sl = new SendList(clientSocketOnServer, loginReceived, myLogger, false);
			performAction();
			break;
		// Delete a file (+ send file list)
		case 3:
			sl = new SendList(clientSocketOnServer, loginReceived, myLogger, false);

			// Prevent the program to go to fast (critical fail in JAR)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				myLogger.log(Level.SEVERE, "Thread failed to sleep.");
			}
			@SuppressWarnings("unused")
			DeleteFile df = new DeleteFile(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Send a file to the client (+ send file list)
		case 4:
			sl = new SendList(clientSocketOnServer, loginReceived, myLogger, false);
			// Prevent the program to go to fast (critical fail in JAR)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				myLogger.log(Level.SEVERE, "Thread failed to sleep.");
			}
			@SuppressWarnings("unused")
			ThrowAFile taf = new ThrowAFile(clientSocketOnServer, loginReceived, false, myLogger);
			performAction();
			break;
		// Share a file (+ send file list)
		case 6:
			sl = new SendList(clientSocketOnServer, loginReceived, myLogger, false);
			@SuppressWarnings("unused")
			CopyAFileInShared cafis = new CopyAFileInShared(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Display list share
		case 7:
			sl = new SendList(clientSocketOnServer, loginReceived, myLogger, true);
			performAction();
			break;
		// Delete from the share (+ send share file list)
		case 8:
			sl = new SendList(clientSocketOnServer, loginReceived, myLogger, true);

			// Prevent the program to go to fast (critical fail in JAR)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				myLogger.log(Level.SEVERE, "Thread failed to sleep.");
			}
			@SuppressWarnings("unused")
			DeleteSharedFile dsf = new DeleteSharedFile(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Download a file from the share (+ send share file list)
		case 9:
			sl = new SendList(clientSocketOnServer, loginReceived, myLogger, true);
			// Prevent the program to go to fast (critical fail in JAR)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				myLogger.log(Level.SEVERE, "Thread failed to sleep.");
			}
			@SuppressWarnings("unused")
			ThrowAFile taf2 = new ThrowAFile(clientSocketOnServer, loginReceived, true, myLogger);
			performAction();
			break;
		// End of the program
		case 5:
			try {
				clientSocketOnServer.close();
			} catch (IOException e) {
				myLogger.log(Level.SEVERE, "User " + loginReceived + " has failed to close the connection.");
			}
			myLogger.log(Level.WARNING, "User " + loginReceived + " has closed the connection.");
			break;
		}
	}

	/**
	 * Method to write text in a file (for the txt which contain the files
	 * passwords)
	 * 
	 * @param filename
	 * @param text
	 */
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
