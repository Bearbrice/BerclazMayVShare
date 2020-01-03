/*
 * Projet VSShare, AcceptClients
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 27.11.2019
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptClients implements Runnable {

	static Logger myLogger;

	private Socket clientSocketOnServer;
	private int clientNumber;
	boolean stay = true;
	Scanner scan = new Scanner(System.in);

	String loginReceived;

	public AcceptClients(Socket clientSocketOnServer, int clientCpt) {
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientCpt;

	}

	public void run() {

		/*-----------------------
		 * LOGGER
		 * INITIALISATION
		 * --------------------*/
		myLogger = Logger.getLogger("testing");
		FileHandler fh = null;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(dtf.format(now));

		String logFileName = ".\\Logs\\LOG_" + dtf.format(now) + ".log";
		// String logFileName = "./my.log";

		try {
			fh = new FileHandler(logFileName, true);
			// fh = new FileHandler(logFileName, true); //if you want to add log to a file :
			// false, for overwrite : true
		} catch (SecurityException e1) {
			e1.printStackTrace();
			e1.getMessage();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		myLogger.addHandler(fh);

		Logging.SocketFormatter formatter = new Logging.SocketFormatter();
		fh.setFormatter(formatter);

		/*-----------------------
		 * LOGGER
		 * INITIALISED
		 * --------------------*/

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

			String first = "Voici les actions disponibles :\n" + "1. Upload a file\n"
					+ "2. Display list of accessible files\n" + "3. Quit server\n"
					+ "Tapper 1,2 ou 3 pour effectuer une action : ";
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
				e.printStackTrace();
			}

			String first = "Voici les actions disponibles :\n" + "1. Upload a file\n"
					+ "2. Display list of accessible files\n" + "3. Quit server\n"
					+ "Tapper 1,2 ou 3 pour effectuer une action : ";
			pwFirst.println(first);

			// devBBE
			String choice = serverMessage.readLine();

			// devBBE
			int choosen = Integer.parseInt(choice);
			executeAction(choosen);

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
			ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, loginReceived);
			performAction();
			// SendList sl1 = new SendList(clientSocketOnServer, loginReceived);
			break;
		// Send the list of file
		case 2:
			@SuppressWarnings("unused")
			SendList sl2 = new SendList(clientSocketOnServer, loginReceived);
			// working
			performAction();
			break;
		case 3:
			DeleteFile df = new DeleteFile(clientSocketOnServer, loginReceived);
			performAction();
			break;
		}
	}

	public static void append(String filename, String text) {
		BufferedWriter bufWriter = null;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename, true);
			bufWriter = new BufferedWriter(fileWriter);
			// Insérer un saut de ligne
			bufWriter.newLine();
			bufWriter.write(text);
			bufWriter.close();
		} catch (IOException ex) {
			// Logger.getLogger(TextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
			myLogger.log(Level.SEVERE, "Method append - Failed to list file");
		} finally {
			try {
				bufWriter.close();
				fileWriter.close();
			} catch (IOException ex) {
				// Logger.getLogger(TextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
				myLogger.log(Level.SEVERE, "Method append - Failed to close the writers");
			}
		}
	}
}
