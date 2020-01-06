package ClientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PutAFileOnShared {

	Scanner scan = new Scanner(System.in);

	public PutAFileOnShared(Socket clientSocket) {
		try {
			// Message from the server to choose the file to shared
			BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println(serverMessage.readLine());

//            String fileName = scan.nextLine();
//            File fileToSend = new File("");
//
//            String name = null;
//
//            if (!fileToSend.exists()) {
//                System.out.println("Selected file unlocatable.");
//            }
//
//            System.out.println("Selected file name \t:\t" + name);

			// File information sender
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);

			String name = scan.nextLine();
			pw.println(name);
			System.out.println("File name sended \t:\t" + name);

			System.out.println();
			System.out.println("--> File has been transfered in the Shared <--");
			// break;
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
