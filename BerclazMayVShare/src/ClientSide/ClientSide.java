/*
 * Project VSShare, ClientSide
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide {

	public static Scanner scan = new Scanner(System.in);
	public static Socket clientSocket;
	public static InetAddress serverAddress;
	public static String serverName = "172.22.22.150";
	public static int port = 45003;

	public static void main(String[] args) {
		// The IP address given is the localhost
		serverName = "127.0.0.1";

		try {
			serverAddress = InetAddress.getByName(serverName);
			clientSocket = new Socket(serverAddress, port);
			System.out.println("Successfully connected to the server : " + serverAddress);

			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

			// Choose wisely (register or connect)

			// Reading the server message (to log or to register)
			readServerMessage(serverMessage, 3);

			// Sending the choice (login or register, 1 or 2) to the server
			String welcome = scan.nextLine();
			printWriter.println(welcome);

			// Reading the login request (register or connect)
			readServerMessage(serverMessage, 1);
//			System.out.println(serverMessage.readLine());

			// Sending to the server the user login entered
			String myLogin = scan.nextLine();
			printWriter.println(myLogin);

			// Reading the password request (register or connect)
			readServerMessage(serverMessage, 1);
//			System.out.println(serverMessage.readLine());

			// Sending to the server the user password entered
			String myPwd = scan.nextLine();
			printWriter.println(myPwd);

			// Reading the confirmation message from the server
			String x = serverMessage.readLine();

			/*
			 * If the login and password is correct, the client received a "Success" If not,
			 * the client is kicked from the server
			 */
			if (x.equals("Success")) {
				System.out.println("You are now connected");
			} else {
				System.out.println("Failed to connect. You have been kicked from the server. Please reload it.");
				clientSocket.close();
				return;
			}

			/*
			 * Action reader (to perform an interaction with the server) Read the actions
			 * available for the client
			 */
			readServerMessage(serverMessage, 10);

//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//
//			// +2
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());

			// Sending the choice to the server (the action choosed by the client)
			int myChoice = scan.nextInt();
			printWriter.println(myChoice);

			// Calling the method for the chosen action
			executeAction(myChoice);

		} catch (Exception e) {
			System.out.println(
					"Cannot reach the server, the server is inactive or you have tried wrong username or password. Reload the server.");
			// e.printStackTrace();
		}
	}

	public static void chooseAction() {
		BufferedReader serverMessage;
		PrintWriter printWriter = null;

		try {
			serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

			// Action reader (to perform an interaction with the server)
			readServerMessage(serverMessage, 10);
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//
//			// +2
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}

		// devBBE
		int myChoice = scan.nextInt();
		printWriter.println(myChoice);

		executeAction(myChoice);
	}

	public static void readServerMessage(BufferedReader serverMessage, int loop) {
		try {
			for (int i = 0; i < loop; i++) {
				System.out.println(serverMessage.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void executeAction(int myChoice) {
		try {
			switch (myChoice) {
			// Send a file
			case 1:
				SendAFile saf = new SendAFile(clientSocket);
				chooseAction();
				break;

			// Receive the list of files of the user connected
			case 2:
				ReceiveList rl = new ReceiveList(clientSocket);
				chooseAction();
				break;

			// Delete a file (+Received the list of files)
			case 3:
				ReceiveList rl2 = new ReceiveList(clientSocket);
				DeleteFileOnServer dfos = new DeleteFileOnServer(clientSocket);
				chooseAction();
				break;

			// Download file from the server (+Received the list of files)
			case 4:
				ReceiveList rl3 = new ReceiveList(clientSocket);
				DownloadAFile daf1 = new DownloadAFile(clientSocket);
				chooseAction();
				break;

			// Share a file (+Received the list of files on the share)
			case 5:
				ReceiveList rl4 = new ReceiveList(clientSocket);
				PutAFileOnShared pafos = new PutAFileOnShared(clientSocket);
				chooseAction();
				break;

			// Received the list of files on the share
			case 6:
				ReceiveList rl5 = new ReceiveList(clientSocket);
				chooseAction();
				break;

			// Delete from the share (+Received the list of files on the share)
			case 7:
				ReceiveList rl6 = new ReceiveList(clientSocket);
				DeleteSharedFileOnServer dsfos = new DeleteSharedFileOnServer(clientSocket);
				// HERE
				chooseAction();
				break;

			// Download a file from the share (+Received the list of files on the share)
			case 8:
				ReceiveList rl7 = new ReceiveList(clientSocket);
				DownloadAFile daf2 = new DownloadAFile(clientSocket);
				chooseAction();
				break;

			// End the connexion with the server
			case 9:
				System.out.println("==> The connection has been gracefully closed <==");
				clientSocket.close();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
