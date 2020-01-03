/*
 * Projet VSShare, DeleteFileOnServer
 * Author: B. Berclaz x A. May
 * Date creation: 03.01.2020
 * Date last modification: 03.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

			// Path path = Paths.get(".\\VSShareCloud\\" + login + "\\PWD.txt");

			// Search in the PWD text file of the user
			File pwd = new File(".\\VSShareCloud\\" + login + "\\PWD.txt");
			BufferedReader br = new BufferedReader(new FileReader(pwd));

			// The file to delete
			File fToDelete = new File(".\\VSShareCloud\\" + login + "\\" + fileToDelete);

//			String path = pwd.getAbsolutePath();
//			System.out.println(path);

			String path2 = fToDelete.getCanonicalPath();
			System.out.println(path2);

//			String path3 = pwd.getParent();
//			System.out.println(path3);

			Boolean isCorrect = false;
			String line;

			while ((line = br.readLine()) != null) {

				if (line.equals(fileToDelete)) {

					// read next line
					String x = br.readLine();
					if (x.equals(pass)) {
						isCorrect = true;

						// delete the file from the server repository
						// pw.close();

						// String command = "del " + path2;

//						builder = new ProcessBuilder("cmd.exe", "/c", command);
//
//						builder.redirectErrorStream(true);
//						Process p = null;
//						try {
//							p = builder.start();
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}

						// Delete the file on exit
						fToDelete.deleteOnExit();

						// System.out.print(success);

						// Files.delete(path);

						pw = new PrintWriter(serverSocket.getOutputStream(), true);
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
}
