/*
 * Projet VSShare, SendList
 * Author: B. Berclaz x A. May
 * Date creation: 02.01.2020
 * Date last modification: 02.01.2020
 */

package ServerSide;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendList {

	public SendList(Socket serverSocket, String login) {

		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String dirName = ".\\VSShareCloud\\" + login;

		File fileName = new File(dirName);
		File[] fileList = fileName.listFiles();

		for (File file : fileList) {
			// Test for not displaying the file with the passwords to the user
			if (!(file.getName().equals("PWD.txt"))) {
				printWriter.println("- " + file.getName());
			}
		}
		// Tell the client you sent everything
		printWriter.println("DONE");

	}

}
