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

public class DeleteSharedFile {

	public DeleteSharedFile(Socket serverSocket, String login, Logger myLogger) {
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

			// buffin.close();
			// pw.close();

			// The file to delete
			File fToDelete = new File(".\\VSShareCloud\\Shared\\" + fileToDelete);

			// Delete the file
			deleteFile(fToDelete);

			myLogger.log(Level.INFO, "The file " + fileToDelete + " has been deleted by " + login);

		} catch (IOException e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE,
					"Fatal error when trying to delete file : " + fileToDelete + "from user :" + login);
		}
	}

	public void deleteFile(File f) {
		if (f.exists()) {
			System.out.print("THE FILE EXISTS");
			f.delete();
		} else {
			System.out.print("THE FILE DOES NOT EXISTS");
		}

	}
}
