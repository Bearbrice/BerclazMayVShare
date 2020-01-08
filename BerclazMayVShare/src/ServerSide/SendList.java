/*
 * Project VSShare, SendList
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

/**
 * Class that sends a list of files stored on the server
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class SendList {

	/**
	 * Constructor
	 * 
	 * @param serverSocket
	 * @param login
	 * @param myLogger
	 * @param shared
	 */
	public SendList(Socket serverSocket, String login, Logger myLogger, boolean shared) {

		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
		} catch (IOException e) {
			myLogger.log(Level.SEVERE, "Impossible to initialise the PrintWriter.");
			e.printStackTrace();
		}

		String dirName;

		if (shared == true) {
			dirName = ".\\VSShareCloud\\Shared";
		} else {
			dirName = ".\\VSShareCloud\\" + login;
		}

		File fileName = new File(dirName);
		File[] fileList = fileName.listFiles();

		printWriter.println("************************************");
		if (shared == true) {
			printWriter.println("Here are the documents on the Shared folder of VSShare :");
		} else {
			printWriter.println("Here are your documents on VSShare :");
		}

		/* Handle error if there is only the pwd file in the folder of the user */
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
