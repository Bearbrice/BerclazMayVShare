/*
 * Projet VSShare, ServerSide
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 31.10.2019
 */

package Server;

import java.io.*;
import java.net.*;

public class ProjServerSide {
	
	public static void main(String[] args) {
		ServerSocket myListeningSocket;
		Socket servSocket = null;
		InetAddress localAddress=null;
		String interfaceName = "wlan2";
		int port = 45000;
		
		try {
			System.out.println("--> Starting listening <--");
			myListeningSocket = new ServerSocket(port);
			
			servSocket = myListeningSocket.accept();
			
			/*---------------------*/
			/* Reading the communication from the client */
			/*---------------------*/
			BufferedReader buffin = new BufferedReader(new InputStreamReader(servSocket.getInputStream()));
			
			System.out.println("Communication du client :");
			
			/* Retrieving the name of the file */
			String in = buffin.readLine();
			String fileName=in;

//x			/* Retrieving the length of byte of the file */
			in = buffin.readLine();
			int fileLength= Integer.parseInt(in);
			
//DEVTEST	//DEV
			System.out.println(fileName);
			System.out.println(fileLength);
			
			/*---------------------*/
//x			/* File part */
			/*---------------------*/

			//Creating a directory for our drive
			String cloudPath="C:\\temp\\VSShareDev";
			new File(cloudPath).mkdirs();			
			
			//TEACHER TIP : send the size before creating the byte array normaly
			byte[] mybytesarray = new byte [fileLength];
			
			InputStream is = servSocket.getInputStream();
		
			// TEACHER TIP : Must now the name of the file before send it
			FileOutputStream fos = new FileOutputStream(cloudPath+"\\" + fileName);
		
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			//when we know the length we can add it into the parameters of is.read
//			int bytesRead = is.read(mybytesarray);
//			int current = bytesRead;
//			
//			do {
//				bytesRead = is.read(mybytesarray, current, (mybytesarray.length-current));
//				
//				if(bytesRead>=0) {
//					current += bytesRead;
//				}
//	
//			}while(bytesRead > -1);
			
			/* RETRIEVE FROM : https://stackoverflow.com/questions/27117567/java-transfer-a-file-from-server-to-client-and-from-client-to-server */
			int count;
			while ((count = is.read(mybytesarray)) >= 0) {
	        	bos.write(mybytesarray, 0, count);
	        }
			/*END OF THE RETRIEVING*/
			
			//we can put mybytesarray instead of current if we have decided the final length
			//bos.write(mybytesarray, 0, current);
			bos.flush();
			bos.close();
			
			servSocket.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
