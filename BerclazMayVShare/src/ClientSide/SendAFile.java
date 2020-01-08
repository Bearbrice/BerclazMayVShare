/*
 * Project VSShare, SendAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ClientSide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class to send a file to the server
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class SendAFile {

	Scanner scan = new Scanner(System.in);

	/**
	 * Constructor
	 * 
	 * @param clientSocket
	 */
	public SendAFile(Socket clientSocket) {
		try {
			// Read the server message
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println(serverMessage.readLine());

			String fileType = null;
			String sourcePath = null;

			// Opening a file chooser to get the file to upload on the server
			javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
			fc.setAcceptAllFileFilterUsed(true);
			fc.setVisible(true);
			// Change the button "Open" to "Choose"
			int u = fc.showDialog(fc, "Choose");
			// Get the selected file path
			if (u == javax.swing.JFileChooser.APPROVE_OPTION) {
				fileType = fc.getSelectedFile().toString();
			}

			// File information sender
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);

			// If the file is not chosen, end of the action
			if (fileType == null) {
				pw.println("FileNotChosen");
				System.out.println("!! You have aborted the action !!");
				return;
			}

			// Creating the file to send with the path got
			File fileToSend = new File(fileType);

			String name = null;
			long length = 0;

			// Test if the file exists
			if (fileToSend.exists()) {
				name = fileToSend.getName();
				length = fileToSend.length();
			} else {
				System.out.println("Selected file unlocatable.");
			}

			System.out.println("Selected file name \t:\t" + name);
			System.out.println("Selected file length \t:\t" + length);

			// Sending the file name to the server
			pw.println(name);
			System.out.println("File name sended \t:\t" + name);

			// Sending the file length to the server
			pw.println(length);
			System.out.println("File length sended \t:\t" + length);

			InputStream in = new FileInputStream(fileToSend);
			OutputStream out = clientSocket.getOutputStream();

			// Reading and sending the file bytes to the server
			byte[] bytes = new byte[(int) length];
			int count;
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
			}

			for (int i = 0; i < bytes.length; i++) {
				System.out.print(bytes[i]);
			}

			System.out.println();
			System.out.println("--> File has been sent to the server <--");

		} catch (Exception e) {
			System.out.println("An error occured, please try again.");
		}
	}
}
