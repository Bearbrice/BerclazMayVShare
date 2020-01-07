/*
 * Project VSShare, SendShareList
 * Author: B. Berclaz x A. May
 * Date creation: 06.01.2020
 * Date last modification: 06.01.2020
 */

package ServerSide;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendShareList {

	// Constructor
	public SendShareList(Socket serverSocket, String login, Logger myLogger) {

		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
		} catch (IOException e) {
			myLogger.log(Level.SEVERE, "Impossible to initialise the PrintWriter.");
			e.printStackTrace();
		}

		String dirName = ".\\VSShareCloud\\Shared";

		File fileName = new File(dirName);
		File[] fileList = fileName.listFiles();

		printWriter.println("************************************");
		printWriter.println("Here are the documents on the Share :");

		// handle error if there is only the pwd file in the folder of the user
		for (File file : fileList) {
			// Test for not displaying the file with the passwords to the user
			printWriter.println("- " + file.getName());
		}
		// Tell the client you sent everything
		myLogger.log(Level.INFO,
				"The list of available files in the Share for the user " + login + " has been correctly sent");
		printWriter.println("DONE");
	}
}
