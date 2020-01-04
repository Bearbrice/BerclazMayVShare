/*
 * Projet VSShare, SendList
 * Author: B. Berclaz x A. May
 * Date creation: 02.01.2020
 * Date last modification: 02.01.2020
 */

package ServerSide;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendList {

	// Constructor
	public SendList(Socket serverSocket, String login, Logger myLogger) {

		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
		} catch (IOException e) {
			myLogger.log(Level.SEVERE, "Impossible to initialise the PrintWriter.");
			e.printStackTrace();
		}

		String dirName = ".\\VSShareCloud\\" + login;

		File fileName = new File(dirName);
		File[] fileList = fileName.listFiles();

		printWriter.println("************************************");
		printWriter.println("Here are your documents on VSShare :");

		// handle error if there is only the pwd file in the folder of the user
		if (fileList.length == 1) {
			printWriter.println("!! No file has been found. !!");
			printWriter.println("DONE");
			myLogger.log(Level.INFO, "No files are available for user " + login);
			return;
		} else {
			for (File file : fileList) {
				// Test for not displaying the file with the passwords to the user
				if (!(file.getName().equals("PWD.txt"))) {
					printWriter.println("- " + file.getName());
				}
			}
			// Tell the client you sent everything
			myLogger.log(Level.INFO, "The list of available files for the user " + login + " has been correctly sent");
			printWriter.println("DONE");
		}

	}

}
