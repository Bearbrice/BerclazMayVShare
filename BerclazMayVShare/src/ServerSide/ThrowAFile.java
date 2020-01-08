/*
 * Project VSShare, ThrowAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that sends a file stored on the server to a client
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class ThrowAFile {

	Scanner scan = new Scanner(System.in);

	/**
	 * Constructor
	 * 
	 * @param serverSocket
	 * @param login
	 * @param shared
	 * @param myLogger
	 */
	public ThrowAFile(Socket serverSocket, String login, boolean shared, Logger myLogger) {
		throwAFile(serverSocket, login, shared, myLogger);
	}

	public void throwAFile(Socket serverSocket, String login, boolean shared, Logger myLogger) {
		try {
			// Allows to read and print
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			// Asking the client for the file
			String question = "Enter the file you want to download (enter q to quit) : ";
			pw.println(question);

			// Reading the file name asked by the client
			String fileName;
			fileName = buffin.readLine();
			myLogger.log(Level.INFO, login + "File name to throw :" + fileName);

			// Check if the location is the user or the shared folder
			File fileToSend = null;
			if (shared == true) {
				fileToSend = new File(".\\VSShareCloud\\Shared\\" + fileName);
			} else {
				fileToSend = new File(".\\VSShareCloud\\" + login + "\\" + fileName);
			}

			String name = null;
			long length = 0;

			// Checking if the file asked exists
			if (fileToSend.exists()) {
				name = fileToSend.getName();
				length = fileToSend.length();
			} else {
				pw.println("WRONG");
				myLogger.log(Level.INFO, login + "has aborted or typed file name wrong.");
				return;
			}

			// Sending the file name to the client
			pw.println(name);

			// Sending the file length to the client
			pw.println(length);

			// Set up the streams
			InputStream in = new FileInputStream(fileToSend);
			OutputStream out = serverSocket.getOutputStream();

			// Creating the bytes array
			byte[] bytes = new byte[(int) length];
			int count;

			// Reading the file and writing/sending the file to the client
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
			}

			String x = "";

			// Display the bytes sended
			for (int i = 0; i < bytes.length; i++) {
				x += bytes[i] + " ";
			}

			myLogger.log(Level.INFO, "File has been sent to the client. // Name " + name + " // Length " + length
					+ " // Bytes sended " + x);

		} catch (Exception e) {
			myLogger.log(Level.SEVERE, "Fatal error when trying to send a file to the client.");
		}
	}
}
