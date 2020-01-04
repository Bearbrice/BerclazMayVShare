/*
 * Projet VSShare, ReceiveAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ClientSide;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DownloadAFile {

	Scanner scan = new Scanner(System.in);

	public DownloadAFile(Socket serverSocket) {

		try {
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);

			// Question from the server to choose the file to download
			System.out.println(buffin.readLine());

			// Getting the file name entered and send it to the server
			pw.println(scan.nextLine());

			String fileName = buffin.readLine();
			int fileLength = Integer.parseInt(buffin.readLine());
			System.out.println("File name : " + fileName + " | length : " + fileLength);

			// Downloading the file
			InputStream in = null;
			FileOutputStream out = null;

			try {
				in = serverSocket.getInputStream();
			} catch (IOException ex) {
				System.out.println("Can't get socket input stream. ");
			}

			try {
				out = new FileOutputStream("C:\\Users\\brice\\Downloads\\" + fileName);
			} catch (FileNotFoundException ex) {
				System.out.println("File not found. ");
			}

			byte[] myByteArray = new byte[fileLength];

			in.read(myByteArray, 0, myByteArray.length);

			// dev
			System.out.print("Bytes received : ");
			for (int i = 0; i < myByteArray.length; i++) {
				System.out.print(myByteArray[i]);
			}

			out.write(myByteArray);

			out.flush();

//			System.out.print("Bytes received : ");
//			for (int i = 0; i < myByteArray.length; i++) {
//				System.out.print(myByteArray[i] + "-");
//			}

			System.out.println();
			System.out.println("The file will be download in your download folder.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
