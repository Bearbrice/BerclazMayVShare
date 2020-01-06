/*
 * Projet VSShare, ServerSideMain
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 21.10.2019
 */

package ServerSide;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSideMain {

	public static void main(String[] args) {

		ServerSocket myListeningSocket;
		Socket serverSocket = null;
		InetAddress localAddress = null;
		int port = 45003;
		ServerSocket mySkServer;
		int clientCpt = 1;

		Logger myLogger = null;

		try {
			/*-----------------------
			 * LOGGER
			 * INITIALISATION
			 * --------------------*/

			// Initialize logger
			myLogger = Logger.getLogger("testing");

			FileHandler fh = null;

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime now = LocalDateTime.now();

			String logFileName = ".\\Logs\\LOG_" + dtf.format(now) + ".log";

			try {
				fh = new FileHandler(logFileName, true);
			} catch (SecurityException e1) {
				e1.printStackTrace();
				e1.getMessage();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			myLogger.addHandler(fh);

			Logging.SocketFormatter formatter = new Logging.SocketFormatter();
			fh.setFormatter(formatter);

			/*-----------------------
			 * LOGGER
			 * INITIALISED
			 * --------------------*/

			myLogger.log(Level.INFO, "<--- Start listening to clients --->");

			mySkServer = new ServerSocket(port, 10, localAddress);

			while (true) {
				Socket clientSocket = mySkServer.accept();
				Thread t = new Thread(new AcceptClients(clientSocket, clientCpt, myLogger));
				clientCpt++;

				// Starting the thread
				t.start();

			}

		} catch (Exception e) {
			myLogger.log(Level.SEVERE, "Exception occured in main programm. Port used : " + port);
			e.printStackTrace();
		}

	}
}
