package ClientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveList {
	public ReceiveList(Socket clientSocket) {
		try {
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			while (true) {
				System.out.println(serverMessage.readLine());
			}

		} catch (Exception e) {

		}

	}
}
