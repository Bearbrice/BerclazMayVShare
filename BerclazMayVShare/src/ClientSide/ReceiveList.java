/*
 * Projet VSShare, SendList
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

			while (true) {
				temp = serverMessage.readLine();
				if(temp.equals("DONE")){
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
