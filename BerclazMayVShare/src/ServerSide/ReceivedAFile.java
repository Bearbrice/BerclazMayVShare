/*
 * Projet VSShare, ReceiveAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceivedAFile {

	public ReceivedAFile(Socket serverSocket, int clientNumber) {

		try {
			// Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Entrer le chemin du fichier :";
			pw.println(question);

			// Getting the file
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			String fileName;
			fileName = buffin.readLine();
			System.out.println("File name received :" + fileName);

			int fileLength = Integer.parseInt(buffin.readLine());
			System.out.println("File length received :" + fileLength);
			// System.out.println("File bytes\t:\t" + buffin.readLine());
			// buffin.close();

			InputStream in = null;
			OutputStream out = null;

			try {
				in = serverSocket.getInputStream();
			} catch (IOException ex) {
				System.out.println("Can't get socket input stream. ");
			}

			// Creating a directory for our drive if not existing
			try {
				String cloudPath = "C:\\temp\\VSShareDev";
				new File(cloudPath).mkdirs();
			} catch (Exception e) {
				System.out.println("Can't create the directory for the drive ");
			}

			try {
				out = new FileOutputStream("C:\\temp\\VSShareDev\\test.txt");
			} catch (FileNotFoundException ex) {
				System.out.println("File not found. ");
			}

			byte[] myByteArray = new byte[fileLength];

			int count;
			while ((count = in.read(myByteArray)) > 0) {
				out.write(myByteArray, 0, count);
			}

			out.flush();

			System.out.print("Bytes received : ");
			for (int i = 0; i < myByteArray.length; i++) {
				System.out.print(myByteArray[i] + "-");
			}

			out.close();
			in.close();

			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
