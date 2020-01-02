/*
 * Projet VSShare, ClientSide
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 27.11.2019
 */

package ClientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide {

	public static void main(String[] args) {
		Socket clientSocket;
		InetAddress serverAddress;
		String serverName = "172.22.22.150";
		serverName = "192.168.1.110";
		int port = 45000;
		Scanner scan = new Scanner(System.in);

		try {
			serverAddress = InetAddress.getByName(serverName);
			clientSocket = new Socket(serverAddress, port);
			System.out.println("Successfully connected to the server : " + serverAddress);

			// First, listen the server
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println(serverMessage.readLine());
			System.out.println(serverMessage.readLine());
			System.out.println(serverMessage.readLine());
			System.out.println(serverMessage.readLine());
			System.out.println(serverMessage.readLine());

			// devBBE
			int myChoice = scan.nextInt();
			PrintWriter pwFirst = new PrintWriter(clientSocket.getOutputStream(), true);
			pwFirst.println(myChoice);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
