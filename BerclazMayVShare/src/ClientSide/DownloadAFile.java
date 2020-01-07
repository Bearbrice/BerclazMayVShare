/*
 * Project VSShare, DownloadAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ClientSide;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.HeadlessException;
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

import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class DownloadAFile {

	Scanner scan = new Scanner(System.in);

	public DownloadAFile(Socket serverSocket) {

		try {
			// Allows to read and print
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);

			// ????????????????
			JFileChooser fc = new JFileChooser() {
				@Override
				protected JDialog createDialog(Component parent) throws HeadlessException {
					// intercept the dialog created by JFileChooser
					JDialog dialog = super.createDialog(parent);
					dialog.setModal(true); // set modality (or setModalityType)
					return dialog;
				}
			};

			// Reading the question from the server to choose the file to download
			System.out.println(buffin.readLine());

			// Getting the file name entered and send it to the server
			pw.println(scan.nextLine());

			// Reading the file name and length sended by the server
			String fileName = buffin.readLine();
			int fileLength = Integer.parseInt(buffin.readLine());
			System.out.println("File name : " + fileName + " | length : " + fileLength);

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

			// Reading the bytes array sended by the server
			in.read(myByteArray, 0, myByteArray.length);

			// Display of the bytes received
			System.out.print("Bytes received : ");
			for (int i = 0; i < myByteArray.length; i++) {
				System.out.print(myByteArray[i]);
			}

			// Writing the byte array (OutputStream)
			out.write(myByteArray);
			out.flush();

			// Confirmation message
			System.out.println();
			System.out.println("The file has been downloaded in your download folder.");

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
