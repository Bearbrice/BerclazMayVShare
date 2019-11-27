package Old_Server;

import java.io.IOException;
import java.net.*;

public class AcceptClientD implements Runnable {

	private Socket clientSocketOnServer;
	private int clientNumber;

	//Constructor
	public AcceptClientD (Socket clientSocketOnServer, int clientNo)
	{
		this.clientSocketOnServer = clientSocketOnServer;
		this.clientNumber = clientNo;

	}
	//overwrite the thread run()
	public void run() {

		try {
				System.out.println("Client Nr "+clientNumber+ " is connected");
				System.out.println("Socket is available for connection"+ clientSocketOnServer);
				clientSocketOnServer.close();	
				Thread.sleep(3000);
				System.out.println("end of connection to the client " + clientNumber);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}