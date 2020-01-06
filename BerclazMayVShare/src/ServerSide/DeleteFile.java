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

public class DeleteFile {

	String fileToDelete;

	// Constructor
	public DeleteFile(Socket serverSocket, String login, Logger myLogger) {

		PrintWriter pw;
		// ProcessBuilder builder;
		try {
			pw = new PrintWriter(serverSocket.getOutputStream(), true);

			String instruction = "Enter the name of the file you want to delete :";
			pw.println(instruction);

			// Getting the file to delete
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			fileToDelete = buffin.readLine();
			System.out.println("The file to delete is :" + fileToDelete);

			String askPassword = "Please enter the password assigned to the file to confirm deletion";
			pw.println(askPassword);

			String pass;
			pass = buffin.readLine();

			// Control of the password
			// Search in the PWD text file of the user
			File pwd = new File(".\\VSShareCloud\\" + login + "\\PWD.txt");
			BufferedReader br = new BufferedReader(new FileReader(pwd));

			// The file to delete
			File fToDelete = new File(".\\VSShareCloud\\" + login + "\\" + fileToDelete);

			String path2 = fToDelete.getCanonicalPath();
			// System.out.println(path2);

			Boolean isCorrect = false;
			String line;

			while ((line = br.readLine()) != null) {

				if (line.equals(fileToDelete)) {

					// read next line
					String x = br.readLine();
					if (x.equals(pass)) {
						isCorrect = true;

						// Delete the file
						fToDelete.delete();

						// Delete lines in PWD.txt for the user connected
						deleteLine(pwd.toString(), getLinesToDelete(pwd.toString(), fileToDelete));
						deleteLine(pwd.toString(), getLinesToDelete(pwd.toString(), pass));

						pw.println("Success");
						myLogger.log(Level.INFO, "The file " + fileToDelete + " has been deleted by " + login);
						break;
					}
				}
			}

			if (isCorrect == false) {
				pw.println("Fail");
				myLogger.log(Level.INFO,
						"Wrong file name or password for the file " + fileToDelete + "from user :" + login);
			}

		} catch (IOException e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE,
					"Fatal error when trying to delete file : " + fileToDelete + "from user :" + login);
		}

	}

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
