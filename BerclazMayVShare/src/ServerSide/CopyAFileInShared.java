package ServerSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CopyAFileInShared {

	static Logger myLogger;

	public CopyAFileInShared(Socket serverSocket, String loginReceived, Logger myLogger) {

		this.myLogger = myLogger;

		try {
			// Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Enter the file you want to shared :";
			pw.println(question);

			// Getting the file
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			// Getting the filename to copy in Shared
			String fileName;
			fileName = buffin.readLine();
			System.out.println("File name received :" + fileName);

			String sourceLocation = ".\\VSShareCloud\\" + loginReceived + "\\" + fileName;
			String targetLocation = ".\\VSShareCloud\\Shared\\" + fileName;
			copyFile(sourceLocation, targetLocation);

		} catch (Exception e) {
			e.printStackTrace();
			myLogger.log(Level.SEVERE, "Failed to receive file.");
		}
	}

	public static void copyFile(String from, String to) {
		try {
			Path src = Paths.get(from);
			Path dest = Paths.get(to);
			Files.copy(src, dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
