/*
 * Projet VSShare, SendAFile
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

public class ThrowAFile {

	Scanner scan = new Scanner(System.in);

	public ThrowAFile(Socket serverSocket, String login) {
		try {
			// Allows to read and print
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			// Asking the client for the file
			String question = "Enter the file you want to download : ";
			pw.println(question);

			// Reading the file name asked by the client
			String fileName;
			fileName = buffin.readLine();
			System.out.println("File name to throw :" + fileName);

			// File fileToSend = new File("BerclazMayVShare\\VSShareCloud\\" + login + "\\"
			// + fileName);
			File fileToSend = new File(".\\VSShareCloud\\" + login + "\\" + fileName);

			String name = null;
			long length = 0;

			// Checking if the file asked exists
			if (fileToSend.exists()) {
				name = fileToSend.getName();
				length = fileToSend.length();
			} else {
				System.out.println("Selected file unlocatable.");
			}

			// Display
			System.out.println("Selected file name \t:\t" + name);
			System.out.println("Selected file length \t:\t" + length);

			// Sending the file name to the client
			pw.println(name);
			System.out.println("File name sended \t:\t" + name);

			// Sending the file length to the client
			pw.println(length);
			System.out.println("File length sended \t:\t" + length);

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

			// Display the bytes sended
			for (int i = 0; i < bytes.length; i++) {
				System.out.print(bytes[i]);
			}

//				clientSocket.close();

			// Display
			System.out.println();
			System.out.println("--> File has been sent to the client <--");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
