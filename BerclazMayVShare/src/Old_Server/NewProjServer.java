package Old_Server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NewProjServer {
	
	public static void main(String[] args) {
		
		InetAddress localAddress=null;
		String interfaceName = "wlan2";
		int port = 5555;
		
		int ClientNo = 1;
		
		try {
			System.out.println("--> Starting listening <--");
			ServerSocket servSocket = new ServerSocket(port);

			while(true) {
				Socket sock=servSocket.accept();
				System.out.println("connection request received");
	               
				Thread t = new Thread(new AcceptClientD(sock, ClientNo));
	            ClientNo++;
	     		//starting the thread
	    		t.start();
				
				System.out.println("--> Someone is connected : "+ sock +" <--");
				
				OutputStream os = sock.getOutputStream();
				
				File myFile = new File("C:\\temp\\IntroductionTB.pdf");
				
				byte[] mybytesarray = new byte [(int) myFile.length()];
		
				FileInputStream fis = new FileInputStream(myFile);
				
				BufferedInputStream bis = new BufferedInputStream(fis);
				
				//the byte array, where you start, where you end
				bis.read(mybytesarray, 0, mybytesarray.length);
				
				os.write(mybytesarray);
				os.flush();

				sock.close();				
			}
			
//			servSocket.close();
		

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
