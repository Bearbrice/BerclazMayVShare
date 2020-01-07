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

			readServerMessage(serverMessage, 3);

//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
//			System.out.println(serverMessage.readLine());
			String welcome = scan.nextLine();
			printWriter.println(welcome);

			// Login request (register or connect)
			readServerMessage(serverMessage, 1);
//			System.out.println(serverMessage.readLine());
			String myLogin = scan.nextLine();
			printWriter.println(myLogin);

			/* Password request (register or connect) */
			readServerMessage(serverMessage, 1);
//			System.out.println(serverMessage.readLine());
			String myPwd = scan.nextLine();
			printWriter.println(myPwd);

			String x = serverMessage.readLine();

			if (x.equals("Success")) {
				System.out.println("You are now connected");
			} else {
				System.out.println("Failed to connect. You have been kicked from the server. Please reload it.");
				clientSocket.close();
				return;
			}

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

			// devBBE
			int myChoice = scan.nextInt();
			printWriter.println(myChoice);

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
			case 2:
				ReceiveList rl = new ReceiveList(clientSocket);
				chooseAction();
				break;
			case 3:
				ReceiveList rl2 = new ReceiveList(clientSocket);
				DeleteFileOnServer dfos = new DeleteFileOnServer(clientSocket);
				chooseAction();
				break;
			// Download file from the server
			case 4:
				ReceiveList rl3 = new ReceiveList(clientSocket);
				DownloadAFile daf1 = new DownloadAFile(clientSocket);
				chooseAction();
				break;
			// Share a file
			case 5:

				ReceiveList rl4 = new ReceiveList(clientSocket);
				PutAFileOnShared pafos = new PutAFileOnShared(clientSocket);
				chooseAction();
				break;
			// Receive share list
			case 6:
				ReceiveList rl5 = new ReceiveList(clientSocket);
				chooseAction();
				break;
			// Delete from the share
			case 7:
				ReceiveList rl6 = new ReceiveList(clientSocket);
				DeleteSharedFileOnServer dsfos = new DeleteSharedFileOnServer(clientSocket);
				// HERE

				chooseAction();
				break;
			// Download a file
			case 8:
				ReceiveList rl7 = new ReceiveList(clientSocket);
				DownloadAFile daf2 = new DownloadAFile(clientSocket);
				chooseAction();
				break;
			// End of the programm
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
