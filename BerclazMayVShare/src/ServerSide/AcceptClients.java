/*
 * Projet VSShare, AcceptClients
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 27.11.2019
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AcceptClients implements Runnable {

	private Socket clientSocketOnServer;
	private int clientNumber;
	boolean stay = true;
	Scanner scan = new Scanner(System.in);

	public AcceptClients(Socket clientSocketOnServer, int clientCpt) {
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientCpt;
	}

	public void run() {
		System.out.println("Client number " + clientNumber + " is connected.");

		try {
			PrintWriter pwFirst = new PrintWriter(clientSocketOnServer.getOutputStream(), true);
			String first = "Voici les actions qui te sont disponibles :\n" + "1. Uploader un fichier\n"
					+ "2. Supprimer un fichier\n" + "3. Quitter le server\n"
					+ "Tapper 1,2 ou 3 pour effectuer une action : ";
			pwFirst.println(first);

			// devBBE
			BufferedReader serverMessage = new BufferedReader(
					new InputStreamReader(clientSocketOnServer.getInputStream()));
			String choice = serverMessage.readLine();
			// devBBE
			int choosen = Integer.parseInt(choice);
			executeAction(choosen);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, clientNumber);
	}

	// DEV BRICE
	public void executeAction(int choosen) {
		switch (choosen) {
		case 1:
			ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, clientNumber);
			break;
		case 2:
			break;
		case 3:
			break;
		}
	}
}
