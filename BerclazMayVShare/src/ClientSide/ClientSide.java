package ClientSide;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide {

    public static void main(String[] args){
        Socket clientSocket;
        InetAddress serverAddress;
        String serverName = "192.168.1.110";
        int port = 45000;
        Scanner scan = new Scanner(System.in);

        try{
            serverAddress = InetAddress.getByName(serverName);
            clientSocket = new Socket(serverAddress, port);
            System.out.println("Successfully connected to the server : " + serverAddress);

            //First, listen the server
            BufferedReader serverMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(serverMessage.readLine());
            System.out.println(serverMessage.readLine());
            System.out.println(serverMessage.readLine());
            System.out.println(serverMessage.readLine());
            System.out.println(serverMessage.readLine());


        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
