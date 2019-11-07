/*
 * Projet VSShare, ClientSide
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 31.10.2019
 */

package Client;

import java.io.*;
import java.net.*;

public class ProjClientSide {

	public static void main(String[] args) {
				
		Socket clientSocket;
		InetAddress serverAddress;
		String Servername="192.168.43.190";
		int port=45000;
		
		try {
			serverAddress = InetAddress.getByName(Servername);
			clientSocket = new Socket(serverAddress,port);
			System.out.println("Successfully connected to the server : " + serverAddress);
			
			/* Sending file information*/
			/* Name + extension */
			File fileToSend = new File("C:\\temp\\IntroductionTB.pdf");
			String name=null;
			int length=0;
			
			/* Control if the file exists*/
			if(fileToSend.exists()) {
				name = fileToSend.getName();
				length= (int) fileToSend.length();
			}
			else {
				System.out.println("File does not exists!");
			}

			System.out.println("Nom du fichier : " + name);
			System.out.println("Longueur en bytes :" + length);
			
			//Writing
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
			
			pw.println(name);
			System.out.println("Sent to the server : "+ name);
						
			pw.println(length);
			System.out.println("Sent to the server : "+ length);
			
			while(true) {
				
				//dev
				int i=0;
				i++;
				
				OutputStream os = clientSocket.getOutputStream();

				System.out.println("Transfer of the file : " + fileToSend);
				
				byte[] mybytesarray = new byte [(int) fileToSend.length()];
		
				FileInputStream fis = new FileInputStream(fileToSend);
				
				BufferedInputStream bis = new BufferedInputStream(fis);
				
				//the byte array, where you start, where you end
				bis.read(mybytesarray, 0, mybytesarray.length);
				
				os.write(mybytesarray);
				os.flush();
				
				
				clientSocket.close();	
				
				System.out.println(i);
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}

}
