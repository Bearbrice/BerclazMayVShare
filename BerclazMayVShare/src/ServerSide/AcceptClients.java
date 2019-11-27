package ServerSide;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AcceptClients implements Runnable{

    private Socket clientSocketOnServer;
    private int clientNumber;
    boolean stay = true ;
    Scanner scan = new Scanner(System.in);

    public AcceptClients(Socket clientSocketOnServer, int clientCpt)
    {
        this.clientSocketOnServer = clientSocketOnServer;
        this.clientNumber = clientCpt;
    }

    public void run(){
        System.out.println("Client number " + clientNumber + " is connected.");

        try {
            PrintWriter pwFirst = new PrintWriter(clientSocketOnServer.getOutputStream(),true);
            String first = "Voici les actions qui te sont disponibles :\n" +
                    "1. Uploader un fichier\n" +
                    "2. Supprimer un fichier\n" +
                    "3. Quitter le server\n" +
                    "Tapper 1,2 ou 3 pour l'action : ";
            pwFirst.println(first);
        }catch (Exception e){
            e.printStackTrace();
        }

        //ReceivedAFile fr = new ReceivedAFile(clientSocketOnServer, clientNumber);
    }
}
