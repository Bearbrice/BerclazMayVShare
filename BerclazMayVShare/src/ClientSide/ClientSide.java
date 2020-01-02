/*
 * Projet VSShare, ClientSide
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 27.11.2019
 */

package ClientSide;

import ServerSide.ReceivedAFile;

import java.io.BufferedReader;
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

    public static int port = 45000;

	public static void main(String[] args) {
        serverName = "192.168.1.110";
        serverName = "192.168.43.190"; //brice
        serverName = "192.168.43.154"; //me

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
			PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            printWriter.println(myChoice);

			executeAction(myChoice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void executeAction(int myChoice) {
	    try{
            switch (myChoice) {
                //Send a file
                case 1:
                    SendAFile saf = new SendAFile(clientSocket);
                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
        } catch (Exception e){
	        e.printStackTrace();
        }

    }
}
