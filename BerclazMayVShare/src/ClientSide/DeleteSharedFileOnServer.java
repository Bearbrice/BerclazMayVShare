/*
 * Project VSShare, DeleteSharedFileOnServer
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

public class DeleteSharedFileOnServer {

	public DeleteSharedFileOnServer(Socket clientSocket) {

		PrintWriter pw;
		Scanner scan = new Scanner(System.in);

		try {
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			pw = new PrintWriter(clientSocket.getOutputStream(), true);

			// Listen to the server question
			System.out.println(serverMessage.readLine());

			// Scanning the file name entered/choosed by the client
			String fileName = scan.nextLine();

			// Sending the file name to delete to the server
			pw.println(fileName);

			System.out.println("The file : " + fileName + " has been deleted successfully from the Shared.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
