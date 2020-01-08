/*
 * Project VSShare, DownloadAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ClientSide;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class to download a file from the server
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class DownloadAFile {

	Scanner scan = new Scanner(System.in);

	/**
	 * Constructor
	 * 
	 * @param serverSocket
	 */
	public DownloadAFile(Socket serverSocket) {
		downloadAFile(serverSocket);
	}

	public void downloadAFile(Socket serverSocket) {

		try {
			// Allows to read and print
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);

			// Reading the question from the server to choose the file to download
			System.out.println(buffin.readLine());

			// Getting the file name entered and send it to the server
			pw.println(scan.nextLine());

			// Reading the file name and length sended by the server
			String fileName = buffin.readLine();

			if (fileName.equals("WRONG")) {
				System.out.println("Aborted or file name typed wrong");
				return;
			}

			int fileLength = Integer.parseInt(buffin.readLine());

			// Set up the streams
			InputStream in = null;
			FileOutputStream out = null;

			// InputStream
			try {
				in = serverSocket.getInputStream();
			} catch (IOException ex) {
				System.out.println("Can't get socket input stream. ");
			}

			// OutputStream
			String home = null;
			try {
				home = System.getProperty("user.home");
				out = new FileOutputStream(home + "/Downloads/" + fileName);
			} catch (FileNotFoundException ex) {
				System.out.println("File not found. ");
			}

			// Creating the bytes array
			byte[] myByteArray = new byte[fileLength];

			// Reading the bytes array sent by the server
			in.read(myByteArray, 0, myByteArray.length);

			// Writing the byte array (OutputStream)
			out.write(myByteArray);
			out.flush();

			// Confirmation message
			System.out.println("\n The file " + fileName + " has been downloaded in your download folder. \n");

			// Opening a file explorer to show where the file has been downloaded
			Desktop desktop = Desktop.getDesktop();
			File dirToOpen = null;
			try {
				dirToOpen = new File(home + "/Downloads/");
				desktop.open(dirToOpen);
			} catch (IllegalArgumentException iae) {
				System.out.println("File Not Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
