/*
 * Projet VSShare, SendAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ClientSide;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SendAFile {

	Scanner scan = new Scanner(System.in);

	public SendAFile(Socket clientSocket) {
		try {
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println(serverMessage.readLine());

			File fileToSend = new File(scan.nextLine());
//			File fileToSend = new File("C:\\temp\\test.txt");

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
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);

			pw.println(name);
			System.out.println("File name sended \t:\t" + name);
			pw.println(length);
			System.out.println("File length sended \t:\t" + length);
			// pw.close();

			// File sender
			while (true) {
//				OutputStream os = clientSocket.getOutputStream();
//
//				byte[] myByteArray = new byte[(int) fileToSend.length()];
//
//				// os.write(Integer.parseInt(fileToSend.getName()));
//
//				FileInputStream fis = new FileInputStream(fileToSend);
//
//				BufferedInputStream bis = new BufferedInputStream(fis);
//
//				bis.read(myByteArray, 0, myByteArray.length);
//
//				os.flush();
//
////				System.out.print("Data file sended : ");
//				for (int i = 0; i < myByteArray.length; i++) {
//					System.out.println(myByteArray[i]);
//				}

				InputStream in = new FileInputStream(fileToSend);
				OutputStream out = clientSocket.getOutputStream();

				byte[] bytes = new byte[(int) length];
				int count;
				while ((count = in.read(bytes)) > 0) {
					out.write(bytes, 0, count);
				}

				for (int i = 0; i < bytes.length; i++) {
					System.out.print(bytes[i]);
				}

				clientSocket.close();

//				System.out.println();
				System.out.println("Closing Socket...");
//				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
