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
			// Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Enter the file you want to download : ";
			pw.println(question);

			// Getting the file
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			String fileName;
			fileName = buffin.readLine();
			System.out.println("File name to throw :" + fileName);

			// File fileToSend = new File("BerclazMayVShare\\VSShareCloud\\" + login + "\\"
			// + fileName);
			File fileToSend = new File(".\\VSShareCloud\\" + login + "\\" + fileName);

			// Confirm the name and send the length
//			pw.println(fileName);
//			pw.println(fileToSend.length());

			String name = null;
			long length = 0;

			if (fileToSend.exists()) {
				name = fileToSend.getName();
				length = fileToSend.length();
			} else {
				System.out.println("Selected file unlocatable.");
			}

			System.out.println("Selected file name \t:\t" + name);
			System.out.println("Selected file length \t:\t" + length);

			// File information sender
			pw.println(name);
			System.out.println("File name sended \t:\t" + name);

			pw.println(length);
			System.out.println("File length sended \t:\t" + length);

			InputStream in = new FileInputStream(fileToSend);
			OutputStream out = serverSocket.getOutputStream();

			byte[] bytes = new byte[(int) length];
			int count;
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
			}

			for (int i = 0; i < bytes.length; i++) {
				System.out.print(bytes[i]);
			}

//				clientSocket.close();

			System.out.println();
			System.out.println("--> File has been sent to the client <--");
			// break;
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
