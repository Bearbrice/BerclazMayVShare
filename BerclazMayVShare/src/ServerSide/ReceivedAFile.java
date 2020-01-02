/*
 * Projet VSShare, ReceiveAFile
 * Author: B. Berclaz x A. May
 * Date creation: 24.10.2019
 * Date last modification: 21.10.2019
 */

package ServerSide;

import java.io.*;
import java.net.Socket;

public class ReceivedAFile {

	public ReceivedAFile(Socket serverSocket, int clientNumber) {

		try {
			//Asking for the file
			PrintWriter pw = new PrintWriter(serverSocket.getOutputStream(), true);
			String question = "Entrer le chemin du fichier :";
			pw.println(question);

			//Getting the file
			BufferedReader buffin = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String fileName = buffin.readLine();
            System.out.println("File name received\t\t:\t" + fileName);
            int fileLength = Integer.parseInt(buffin.readLine());
            System.out.println("File length received\t:\t" + fileLength);
            //System.out.println("File bytes\t:\t" + buffin.readLine());
            //buffin.close();

			byte[] myByteArray = new byte[100]; // need to get the length

			InputStream is = serverSocket.getInputStream();

			FileOutputStream fos = new FileOutputStream("C:\\temp\\Remake\\test.txt");

			BufferedOutputStream bos = new BufferedOutputStream(fos);

			int bytesRead = is.read(myByteArray);
			int current = bytesRead;

			do {
				bytesRead = is.read(myByteArray, current, (myByteArray.length - current));
				if (bytesRead >= 0) {
					current += bytesRead;
				}
			} while (bytesRead > -1);

			System.out.print("File data received from client " + clientNumber + " : ");
			for (int i = 0; i < myByteArray.length; i++) {
				System.out.print(myByteArray[i]);
			}

			bos.write(myByteArray, 0, current);
			bos.flush();
			bos.close();
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
