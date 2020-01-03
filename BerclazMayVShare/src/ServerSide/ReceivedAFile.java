/*
 * Projet VSShare, ReceiveAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 02.01.2020
 */

package ServerSide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceivedAFile {

	public ReceivedAFile(Socket serverSocket, String loginReceived) {

		try {
			// Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Enter the path of the file :";
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

			// DEVELOPPEMENT
			String question2 = "Enter a password for the file";
			pw.println(question2);

			String password;
			password = buffin.readLine();

			append(".\\VSShareCloud\\" + loginReceived + "\\PWD.txt", fileName);
			append(".\\VSShareCloud\\" + loginReceived + "\\PWD.txt", password);

			InputStream in = null;
			OutputStream out = null;

			try {
				in = serverSocket.getInputStream();
			} catch (IOException ex) {
				System.out.println("Can't get socket input stream. ");
			}

//			// Creating a directory for our drive if not existing
//			try {
//				String cloudPath = "BerclazMayVShare\\VSShareCloud";
//				new File(cloudPath).mkdirs();
//			} catch (Exception e) {
//				System.out.println("Can't create the directory for the drive ");
//			}

			try {
				out = new FileOutputStream(".\\VSShareCloud\\" + loginReceived + "\\" + fileName);
			} catch (FileNotFoundException ex) {
				System.out.println("File not found. ");
			}

			byte[] myByteArray = new byte[fileLength];

//			FileInputStream fis = new FileInputStream(myFile);
//			
//			BufferedInputStream bis = new BufferedInputStream(fis);
//			
//			//the byte array, where you start, where you end
			in.read(myByteArray, 0, myByteArray.length);

			out.write(myByteArray);
//			
//			os.write(mybytesarray);
//			os.flush();

//			int count;
//			while ((count = in.read(myByteArray)) > 0) {
//				out.write(myByteArray, 0, count);
//				System.out.println("BOUCLE");
//			}

			out.flush();

			System.out.println("TEST IF GOES OR NOT");

			System.out.print("Bytes received : ");
			for (int i = 0; i < myByteArray.length; i++) {
				System.out.print(myByteArray[i] + "-");
			}

//			out.close();
//			in.close();

			// serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void append(String filename, String text) {
		BufferedWriter bufWriter = null;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename, true);
			bufWriter = new BufferedWriter(fileWriter);
			// Insérer un saut de ligne
			bufWriter.newLine();
			bufWriter.write(text);
			bufWriter.close();
		} catch (IOException ex) {
			// Logger.getLogger(TextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
			// myLogger.log(Level.SEVERE, "Method append - Failed to list file");
		} finally {
			try {
				bufWriter.close();
				fileWriter.close();
			} catch (IOException ex) {
				// Logger.getLogger(TextFileWriter.class.getName()).log(Level.SEVERE, null, ex);
				// myLogger.log(Level.SEVERE, "Method append - Failed to close the writers");
			}
		}
	}
}
