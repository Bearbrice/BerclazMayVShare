/*
 * Projet VSShare, AcceptClients
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

	public AcceptClients(Socket clientSocketOnServer, int clientCpt, Logger myLogger) {
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientCpt;
		this.myLogger = myLogger;
	}

	public void run() {

		/* CONNECTION FROM A CLIENT */
		System.out.println("Client number " + clientNumber + " is connected.");

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

//				File cloudUsers = new File("C:\\temp\\VSShareServer\\Users.txt");

//				FileWriter fw = new FileWriter(cloudUsers);

				String path = ".\\VSShareCloud\\Users.txt";

				// Add a blank line
				append(path, "");

				// Login and password
				append(path, newLoginReceived);
				append(path, newPwdReceived);
				myLogger.log(Level.INFO, "New user added in DB : " + newLoginReceived);

//				fw.write("\n"); // forcer le passage à la ligne
//				fw.write(newLoginReceived); // écrire une ligne dans le fichier Users.txt
//				fw.write("\n"); // forcer le passage à la ligne
//				fw.write(newPwdReceived);
//
//				fw.close();

				String newUserFolder = ".\\VSShareCloud\\" + newLoginReceived;
				new File(newUserFolder).mkdirs();
				myLogger.log(Level.INFO, "New folder created for the new user : " + newLoginReceived);

				// Tell the client he is connected
				pwFirst.println("Success");
				break;
			case 2:
				/* --- CONNEXION --- */
				String login = "Enter your username";
				pwFirst.println(login);
				loginReceived = serverMessage.readLine();

				// System.out.println(loginReceived);

				String pwd = "Enter your password";
				pwFirst.println(pwd);
				String pwdReceived = serverMessage.readLine();

				// System.out.println(pwdReceived);

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
				}

			}

			String first = "Here are the following actions :\n" + "1. Upload a file\n"
					+ "2. Display list of accessible files\n" + "3. Delete a file\n"
					+ "4. Download a file from the server\n" + "5. Quit the server\n"
					+ "Please, enter 1,2,3,4 or 5 to perform an action : ";
			pwFirst.println(first);

			// devBBE
			String choice = serverMessage.readLine();

			// devBBE
			int choosen = Integer.parseInt(choice);
			executeAction(choosen);

		} catch (Exception e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE, "Failed to receive menu choice from client : " + loginReceived);
		}

		// ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, clientNumber);
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
				Thread.sleep(500);
			} catch (InterruptedException e) {
				myLogger.log(Level.SEVERE, "Thread failed to sleep.");
				e.printStackTrace();
			}

			String first = "Here are the following actions :\n" + "1. Upload a file\n"
					+ "2. Display list of accessible files\n" + "3. Delete a file\n"
					+ "4. Download a file from the server\n" + "5. Quit the server\n"
					+ "Please, enter 1,2,3,4 or 5 to perform an action : ";
			pwFirst.println(first);

			// devBBE
			String choice = serverMessage.readLine();

			// devBBE
			int choosen = Integer.parseInt(choice);
			executeAction(choosen);

			clientSocketOnServer.close();

			// performAction();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.print("performAction error");
		}
	}

	// DEV BRICE
	public void executeAction(int choosen) {
		switch (choosen) {
		// Receive a file
		case 1:
			@SuppressWarnings("unused")
			ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			// SendList sl1 = new SendList(clientSocketOnServer, loginReceived);
			break;
		// Send the list of file
		case 2:
			@SuppressWarnings("unused")
			SendList sl2 = new SendList(clientSocketOnServer, loginReceived, myLogger);
			// working
			performAction();
			break;
		// Delete a file
		case 3:
			DeleteFile df = new DeleteFile(clientSocketOnServer, loginReceived, myLogger);
			performAction();
			break;
		// Send a file to the client
		case 4:
			// ADD HERE

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
