/*
 * Project VSShare, ShareMenu
 * Author: B. Berclaz x A. May
 * Date creation: 06.01.2020
 * Date last modification: 06.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShareMenu {

	// Constructor
	public ShareMenu(Socket serverSocket, String login, Logger myLogger) {

		PrintWriter printWriter = null;
		BufferedReader serverMessage = null;
		try {
			printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
			serverMessage = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		} catch (IOException e) {
			myLogger.log(Level.SEVERE, "Impossible to initialise the PrintWriter.");
			e.printStackTrace();
		}

		printWriter.println("************************************");
		String shareMenu = "Welcome to the share menu. Choose an action to execute :\n"
				+ "1. Share a file from your folder\n" + "2. Display available files on the share"
				+ "3. Delete a file on the share";
		printWriter.println(shareMenu);

		String clientChoice = null;
		try {
			clientChoice = serverMessage.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int userChoice = Integer.parseInt(clientChoice);

		// executeAction(userChoice);

	}

	/*
	 * public void performAction() { PrintWriter pwFirst; BufferedReader
	 * serverMessage;
	 * 
	 * try { pwFirst = new PrintWriter(serverSocket.getOutputStream(), true);
	 * 
	 * serverMessage = new BufferedReader(new
	 * InputStreamReader(serverSocket.getInputStream()));
	 * 
	 * // It is a function to prevent the program to go too fast and let the client
	 * // interract try { Thread.sleep(1500);
	 * 
	 * } catch (InterruptedException e) { myLogger.log(Level.SEVERE,
	 * "Thread failed to sleep."); e.printStackTrace(); }
	 * 
	 * pwFirst.println(actions);
	 * 
	 * String choice = serverMessage.readLine(); int choosen =
	 * Integer.parseInt(choice);
	 * 
	 * executeAction(choosen);
	 * 
	 * serverSocket.close();
	 * 
	 * } catch (IOException e) {
	 * 
	 * e.printStackTrace(); System.out.print("performAction error"); } }
	 * 
	 * public void executeAction(int userChoice) { switch (userChoice) { case 1: //
	 * Share a file
	 * 
	 * break; case 2: // Display list
	 * 
	 * break; case 3: // Delete
	 * 
	 * break; case 4: // Exit share menu
	 * 
	 * break; } }
	 */
}
