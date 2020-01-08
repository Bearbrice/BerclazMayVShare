/*
 * Project VSShare, DeleteFile
 * Author: B. Berclaz x A. May
 * Date creation: 03.01.2020
 * Date last modification: 03.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that deletes a shared file on the server according to a password
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class DeleteSharedFile {

	String fileToDelete;

	/**
	 * Constructor
	 * 
	 * @param serverSocket
	 * @param login
	 * @param myLogger
	 */
	public DeleteSharedFile(Socket serverSocket, String login, Logger myLogger) {

		PrintWriter pw;

		try {
			pw = new PrintWriter(serverSocket.getOutputStream(), true);

			// Asking for the name of the file
			String instruction = "Enter the name of the file you want to delete from the Shared :";
			pw.println(instruction);

			// Opening the reader
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			// Read the file to delete sended
			fileToDelete = buffin.readLine();
			System.out.println("The file to delete is :" + fileToDelete);

			// Asking for the password to delete the file
			String askPassword = "Please enter the password assigned to the file to confirm deletion";
			pw.println(askPassword);

			// Reading the password sended
			String pass;
			pass = buffin.readLine();

			// Setting the txt file of the files passwords
			File pwd = new File(".\\VSShareCloud\\PWDShared.txt");
			BufferedReader br = new BufferedReader(new FileReader(pwd));

			// The file to delete
			File fToDelete = new File(".\\VSShareCloud\\Shared\\" + fileToDelete);

			String path2 = fToDelete.getCanonicalPath();

			Boolean isCorrect = false;
			String line;

			/* Loop which read the txt file with files passwords */
			while ((line = br.readLine()) != null) {
				// Test if a line contain the filename
				if (line.equals(fileToDelete)) {
					// Read next line (first line is the filename, second the password)
					String x = br.readLine();

					/* Test if the password is correct */
					if (x.equals(pass)) {
						isCorrect = true;

						// Delete the file
						fToDelete.delete();

						// Delete lines in PWD.txt for the user connected
						deleteLine(pwd.toString(), getLinesToDelete(pwd.toString(), fileToDelete));
						deleteLine(pwd.toString(), getLinesToDelete(pwd.toString(), pass));

						// Send the confirmation message
						pw.println("Success");
						myLogger.log(Level.INFO, "The file " + fileToDelete + " has been deleted by " + login);
						break;
					}
				}
			}
			// If the password is not matching
			if (isCorrect == false) {
				pw.println("Fail");
				myLogger.log(Level.INFO,
						"Wrong file name or password for the file " + fileToDelete + "from user :" + login);
			}
		} catch (IOException e) {
			myLogger.log(Level.SEVERE,
					"Fatal error when trying to delete file : " + fileToDelete + "from user :" + login);
		}
	}

	/**
	 * Method to get the line number of a file which contains a specific word
	 * 
	 * @param file
	 * @param word
	 * @return
	 */
	public static int getLinesToDelete(String file, String word) {
		try {
			BufferedReader buf = new BufferedReader(
					new InputStreamReader(new DataInputStream(new FileInputStream(file))));

			String line;
			int lineNumber = 0;
			while ((line = buf.readLine()) != null) {
				lineNumber++;
				if (word.equals(line)) {
					return lineNumber - 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Method to delete a line in a file
	 * 
	 * @param fileName
	 * @param lineNumber
	 * @return true or false
	 */
	public static boolean deleteLine(String fileName, int lineNumber) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

			StringBuffer sb = new StringBuffer();
			String line;
			int nbLinesRead = 0;
			while ((line = reader.readLine()) != null) {
				if (nbLinesRead != lineNumber) {
					sb.append(line + "\n");
				}
				nbLinesRead++;
			}
			reader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.write(sb.toString());
			out.close();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
