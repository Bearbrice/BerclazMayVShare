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

public class ReceivedAFile {

	static Logger myLogger;

	// Constructor
	public ReceivedAFile(Socket serverSocket, String loginReceived, Logger myLogger) {
		receivedAFile(serverSocket, loginReceived, myLogger);
	}

	// Method called in the constructor
	public void receivedAFile(Socket serverSocket, String loginReceived, Logger myLogger) {

		this.myLogger = myLogger;

		try {
			// Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Enter the path of the file :";
			pw.println(question);

			// Getting the file
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			String fileName;
			fileName = buffin.readLine();

			if (fileName.equals("FileNotChosen")) {
				myLogger.log(Level.WARNING, "No file has been chosen in the JFileChooser. Operation cancelled.");
				return;
			}

			System.out.println("File name received :" + fileName);

			int fileLength = Integer.parseInt(buffin.readLine());
			System.out.println("File length received :" + fileLength);

//			// DEVELOPPEMENT
//			String question2 = "Enter a password for the file";
//			pw.println(question2);
//
//			String password;
//			password = buffin.readLine();
//
//			System.out.println(loginReceived + fileName + password);
//
//			append(".\\VSShareCloud\\" + loginReceived + "\\PWD.txt", fileName);
//			append(".\\VSShareCloud\\" + loginReceived + "\\PWD.txt", password);

			System.out.println(loginReceived + fileName);

			InputStream in = null;
			OutputStream out = null;

			try {
				in = serverSocket.getInputStream();
			} catch (IOException ex) {
				System.out.println("Can't get socket input stream. ");
			}

			try {
				out = new FileOutputStream(".\\VSShareCloud\\" + loginReceived + "\\" + fileName);
			} catch (FileNotFoundException ex) {
				System.out.println("File not found. ");
				myLogger.log(Level.SEVERE, "File not found.");
			}

			byte[] myByteArray = new byte[fileLength];

			in.read(myByteArray, 0, myByteArray.length);

			out.write(myByteArray);

			out.flush();

			System.out.println("TEST IF GOES OR NOT");

			System.out.print("Bytes received : ");
			for (int i = 0; i < myByteArray.length; i++) {
				System.out.print(myByteArray[i] + "-");
			}
		} catch (Exception e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE, "Failed to receive file.");
		}
	}
}
