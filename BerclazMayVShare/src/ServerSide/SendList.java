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

		String dirName = "C:\\temp\\VSShareServer\\" + login;

		File fileName = new File(dirName);
		File[] fileList = fileName.listFiles();

		for (File file : fileList) {
			printWriter.println(file.getName());
		}

	}

}
