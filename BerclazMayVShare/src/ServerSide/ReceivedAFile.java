/*
 * Project VSShare, ReceivedAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that receives a file from a client and store it in his folder
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class ReceivedAFile {

	static Logger myLogger;

	/**
	 * Constructor
	 * 
	 * @param serverSocket
	 * @param loginReceived
	 * @param myLogger
	 */
	public ReceivedAFile(Socket serverSocket, String loginReceived, Logger myLogger) {
		receivedAFile(serverSocket, loginReceived, myLogger);
	}

	/**
	 * Main method of the class which is called in the constructor
	 * 
	 * @param serverSocket
	 * @param loginReceived
	 * @param myLogger
	 */
	public void receivedAFile(Socket serverSocket, String loginReceived, Logger myLogger) {

		this.myLogger = myLogger;

		try {
			// Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Please choose a file to import on the server in the window that just opened...";
			pw.println(question);

			// Getting the file
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			String fileName;
			fileName = buffin.readLine();

			/* Handle error if the user close the JFileChooser */
			if (fileName.equals("FileNotChosen")) {
				myLogger.log(Level.WARNING, "No file has been chosen in the JFileChooser. Operation cancelled.");
				return;
			}

			int fileLength = Integer.parseInt(buffin.readLine());
			myLogger.log(Level.INFO, " File name : " + fileName + "," + " File length : " + fileLength
					+ ", received from : " + loginReceived);

			InputStream in = null;
			OutputStream out = null;

			try {
				in = serverSocket.getInputStream();
			} catch (IOException ex) {
				myLogger.log(Level.SEVERE, "Can't get socket input stream. ");
			}

			try {
				out = new FileOutputStream(".\\VSShareCloud\\" + loginReceived + "\\" + fileName);
			} catch (FileNotFoundException ex) {
				myLogger.log(Level.SEVERE, "File not found.");
			}

			byte[] myByteArray = new byte[fileLength];

			in.read(myByteArray, 0, myByteArray.length);

			out.write(myByteArray);

			out.flush();

			myLogger.log(Level.INFO, "File received : " + fileName + ", from user : " + loginReceived);

			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE, "Failed to receive file.");
		}
	}
}
