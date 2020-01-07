/*
 * Project VSShare, DeleteFileOnServer
 * Author: B. Berclaz x A. May
 * Date creation: 03.01.2020
 * Date last modification: 03.01.2020
 */

package ClientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DeleteFileOnServer {

	public DeleteFileOnServer(Socket clientSocket) {

		PrintWriter pw;
		Scanner scan = new Scanner(System.in);

		try {
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			pw = new PrintWriter(clientSocket.getOutputStream(), true);

			// Reading and display the message of the server
			System.out.println(serverMessage.readLine());

			// Sending the file name to delete
			String fileName = scan.nextLine();
			pw.println(fileName);

			// Reading and display the message of the server
			System.out.println(serverMessage.readLine());

			// Sending the password to delete the file
			String password = scan.nextLine();
			pw.println(password);

			/*
			 * If the password to delete the file is correct, the server send a "Success" If
			 * not, the file is not delete
			 */
			if (serverMessage.readLine().equals("Success")) {
				System.out.println("The file : " + fileName + " has been succesfully deleted.");
			} else {
				System.out.println("Wrong password or file name.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
