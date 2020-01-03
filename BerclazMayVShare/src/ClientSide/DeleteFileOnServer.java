/*
 * Projet VSShare, DeleteFileOnServer
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

			// File to delete
			System.out.println(serverMessage.readLine());
			String fileName = scan.nextLine();
			pw.println(fileName);

			// Password
			System.out.println(serverMessage.readLine());
			String password = scan.nextLine();
			pw.println(password);

			// Password
			if (serverMessage.readLine().equals("Success")) {
				System.out.println("The file : " + fileName + " has been successfully deleted.");
			} else {
				System.out.println("Wrong password or file name, please try again");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
