/*
 * Project VSShare, PutAFileOnShared
 * Author: B. Berclaz x A. May
 * Date creation: 06.01.2020
 * Date last modification: 06.01.2020
 */

package ClientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class to send information to the server about a file to copy in the share
 * 
 * @author Brice Berclaz
 * @author Aurelien May
 */
public class PutAFileOnShared {

	Scanner scan = new Scanner(System.in);

	/**
	 * Constructor
	 * 
	 * @param clientSocket
	 */
	public PutAFileOnShared(Socket clientSocket) {
		try {
			// Message from the server to choose the file to shared
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println(serverMessage.readLine());

			// File information sender
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);

			// Sending the file name to upload on the share
			String name = scan.nextLine();
			pw.println(name);
			System.out.println("File name sended \t:\t" + name);

			// Scanning and sending the file password (for the deletion) to the server
			System.out.println(serverMessage.readLine());
			String pwd = scan.nextLine();
			pw.println(pwd);
			System.out.println("Password sended \t:\t" + pwd);

			System.out.println();
			System.out.println("--> File has been transfered in the Shared <--");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
