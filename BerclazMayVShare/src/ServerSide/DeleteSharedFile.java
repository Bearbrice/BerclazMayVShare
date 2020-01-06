/*
 * Project VSShare, DeleteSharedFile
 * Author: B. Berclaz x A. May
 * Date creation: 06.01.2020
 * Date last modification: 06.01.2020
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

	String fileToDelete;

	// Constructor
	public DeleteSharedFile(Socket serverSocket, String login, Logger myLogger) {

		PrintWriter pw;

		try {
			pw = new PrintWriter(serverSocket.getOutputStream(), true);

			String instruction = "Enter the name of the file you want to delete :";
			pw.println(instruction);

			// Getting the file to delete
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			fileToDelete = buffin.readLine();
			System.out.println("The file to delete is :" + fileToDelete);

			// The file to delete
			File fToDelete = new File(".\\VSShareCloud\\Shared" + fileToDelete);

			// buffin.close();
			// pw.close();

			// Delete the file
			fToDelete.delete();

			pw.println("Success");
			myLogger.log(Level.INFO, "The file " + fileToDelete + " has been deleted by " + login);

		} catch (IOException e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE,
					"Fatal error when trying to delete file : " + fileToDelete + "from user :" + login);
		}
	}
}