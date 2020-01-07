/*
 * Project VSShare, ReceiveList
 * Author: B. Berclaz x A. May
 * Date creation: 02.01.2020
 * Date last modification: 02.01.2020
 */

package ClientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveList {

	public ReceiveList(Socket clientSocket) {
		String temp = "";

		try {
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// Reading the file list sended by the server
			while (true) {
				temp = serverMessage.readLine();

				// If the list is all sended, the server send a "DONE" to quit the loop
				if (temp.equals("DONE")) {
					break;
				}
				System.out.println(temp);
			}

			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
