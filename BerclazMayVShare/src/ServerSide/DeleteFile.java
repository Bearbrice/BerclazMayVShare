/*
 * Project VSShare, DeleteSharedFile
 * Author: B. Berclaz x A. May
 * Date creation: 06.01.2020
 * Date last modification: 07.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that deletes a user file on the server
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class DeleteFile {

	/**
	 * Constructor
	 * 
	 * @param serverSocket
	 * @param login
	 * @param myLogger
	 */
	public DeleteFile(Socket serverSocket, String login, Logger myLogger) {
		PrintWriter pw;
		String fileToDelete = null;
		try {
			pw = new PrintWriter(serverSocket.getOutputStream(), true);

			// Asking the client the file to delete
			String instruction = "Enter the name of the file you want to delete from the Shared :";
			pw.println(instruction);

			// Opening the reader
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			// Reading the client message, the file name to delete
			fileToDelete = buffin.readLine();
			System.out.println("The file to delete is :" + fileToDelete);

			// The file to delete
			File fToDelete = new File(".\\VSShareCloud\\" + login + "\\" + fileToDelete);

			// Delete the file
			deleteFile(fToDelete, myLogger, login);

		} catch (IOException e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE,
					"Fatal error when trying to delete file : " + fileToDelete + "from user :" + login);
		}
	}

	/**
	 * Methods to delete a file on the user folder of the server
	 * 
	 * @param f        the file you want to delete
	 * @param myLogger the logger to keep a trace of what is done
	 * @param login    the login of the person who wants to delete the file
	 */
	public void deleteFile(File f, Logger myLogger, String login) {
		if (f.exists()) {
			System.out.print("THE FILE EXISTS");
			f.delete();
			myLogger.log(Level.INFO, "The file " + f.toString() + " has been deleted by " + login);
		} else {
			System.out.print("THE FILE DOES NOT EXISTS");
			myLogger.log(Level.INFO,
					"The file " + f.toString() + " does not exists or the name has been typed wrong by " + login);
		}

	}
}
