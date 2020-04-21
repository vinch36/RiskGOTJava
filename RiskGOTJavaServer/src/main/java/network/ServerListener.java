package network;

import applogic.AppLogicServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerListener {


    //On initialise des valeurs par défaut
    private AppLogicServer appLogicServer;
    private int port;
    private String host;
    private ServerSocket server = null;
    private boolean isRunning = true;

    public int getNbConnexions() {
        return nbConnexions;
    }

    private int nbConnexions = 0;

    public ServerListener(String pHost, int pPort){
        host = pHost;
        port = pPort;
        try {
            server = new ServerSocket(port, 100, InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    //On lance notre serveur
    public void attenteConnexions(AppLogicServer app) {

        this.appLogicServer = app;
        //Toujours dans un thread à part vu qu'il est dans une boucle infinie
        System.out.println("Serveur en attente de connexion clientes");
        for (int i = 0; i < appLogicServer.getNbJoueurs(); i++) {


            try {
                //On attend une connexion d'un client
                //Une fois reçue, on la traite dans un thread séparé
                Socket clientSock = server.accept();
                System.out.println("Connexion cliente reçue de " + clientSock.getInetAddress() + ":" + clientSock.getPort());
                ClientProcessor clientProc = new ClientProcessor(clientSock, appLogicServer);
                Thread t = new Thread(clientProc);
                appLogicServer.connexionRecu(clientProc);
                nbConnexions++;
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
            server = null;
        }
    }
}

