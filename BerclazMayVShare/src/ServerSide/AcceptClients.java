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
import java.util.Scanner;

public class AcceptClients implements Runnable {

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

				String path = "C:\\temp\\VSShareServer\\Users.txt";

				// Add a blank line
				append(path, "");

				// Login and password
				append(path, newLoginReceived);
				append(path, newPwdReceived);

//				fw.write("\n"); // forcer le passage � la ligne
//				fw.write(newLoginReceived); // �crire une ligne dans le fichier Users.txt
//				fw.write("\n"); // forcer le passage � la ligne
//				fw.write(newPwdReceived);
//
//				fw.close();

				String newUserFolder = "C:\\temp\\VSShareServer\\" + newLoginReceived;
				new File(newUserFolder).mkdirs();

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
				File users = new File("C:\\temp\\VSShareServer\\Users.txt");

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
							break;
						}
					}
				}

				if (isCorrect == false) {
					pwFirst.println("Refused");
					clientSocketOnServer.close();
				}

			}

			String first = "Voici les actions disponibles :\n" + "1. Uploader un fichier\n"
					+ "2. Supprimer un fichier\n" + "3. Quitter le server\n"
					+ "Tapper 1,2 ou 3 pour effectuer une action : ";
			pwFirst.println(first);

			// devBBE
			String choice = serverMessage.readLine();

			// devBBE
			int choosen = Integer.parseInt(choice);
			executeAction(choosen);

		} catch (

		Exception e) {
			e.printStackTrace();
		}

		// ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, clientNumber);
	}

	// DEV BRICE
	public void executeAction(int choosen) {
		switch (choosen) {
		// Receive a file
		case 1:
			ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, clientNumber);
			break;
		// Send the list of file
		case 2:
			SendList sl = new SendList(clientSocketOnServer, loginReceived);
			break;
		case 3:
			break;
		}
	}

	public static void append(String filename, String text) {
		BufferedWriter bufWriter = null;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename, true);
			bufWriter = new BufferedWriter(fileWriter);
			// Ins�rer un saut de ligne
			bufWriter.newLine();
			bufWriter.write(text);
			bufWriter.close();
		} catch (IOException ex) {
			// Logger.getLogger(TextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				bufWriter.close();
				fileWriter.close();
			} catch (IOException ex) {
				// Logger.getLogger(TextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
