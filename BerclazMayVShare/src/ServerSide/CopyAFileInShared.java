/*
 * Project VSShare, CopyAFileInShared
 * Author: B. Berclaz x A. May
 * Date creation: 06.01.2020
 * Date last modification: 06.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CopyAFileInShared {

	static Logger myLogger;

	public CopyAFileInShared(Socket serverSocket, String loginReceived, Logger myLogger) {

		this.myLogger = myLogger;

		try {
			// Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Enter the file you want to shared :";
			pw.println(question);

			// Getting the file
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			// Getting the filename to copy in Shared
			String fileName;
			fileName = buffin.readLine();
			System.out.println("File name received :" + fileName);

			// DEVELOPPEMENT
			String question2 = "Enter a password for the file";
			pw.println(question2);

			String password;
			password = buffin.readLine();

			append(".\\VSShareCloud\\PWDShared.txt", fileName);
			append(".\\VSShareCloud\\PWDShared.txt", password);

			String sourceLocation = ".\\VSShareCloud\\" + loginReceived + "\\" + fileName;
			String targetLocation = ".\\VSShareCloud\\Shared\\" + fileName;
			copyFile(sourceLocation, targetLocation);

		} catch (Exception e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE, "Failed to receive file.");
		}
	}

	public static void copyFile(String from, String to) {
		try {
			Path src = Paths.get(from);
			Path dest = Paths.get(to);
			Files.copy(src, dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void append(String filename, String text) {
		BufferedWriter bufWriter = null;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename, true);
			bufWriter = new BufferedWriter(fileWriter);
			// Insert a line break
			bufWriter.newLine();
			bufWriter.write(text);
			bufWriter.close();
		} catch (IOException ex) {
			myLogger.log(Level.SEVERE, "Method append - Failed to list file");
		} finally {
			try {
				bufWriter.close();
				fileWriter.close();
			} catch (IOException ex) {
				myLogger.log(Level.SEVERE, "Method append - Failed to close the writers");
			}
		}
	}
}
