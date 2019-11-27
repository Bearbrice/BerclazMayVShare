package ServerSide;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSideMain {

    public static void main(String[] args){

        ServerSocket myListeningSocket;
        Socket serverSocket = null;
        InetAddress localAddress = null;
        int port = 45000;
        ServerSocket mySkServer;
        int clientCpt = 1;

        try {
            System.out.println("<--- Start listening to clients --->");

            mySkServer = new ServerSocket(45000,10,localAddress);

            while (true){
                Socket clientSocket = mySkServer.accept();
                Thread t = new Thread(new AcceptClients(clientSocket, clientCpt));
                clientCpt++;

                //Starting the thread
                t.start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
