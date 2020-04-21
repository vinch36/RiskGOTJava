package network;

import applogic.AppLogicServer;
import applogic.objects.JoueurServer;
import common.ClientCommandes;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ClientProcessor implements Runnable  {
    private Socket sock;
    private PrintWriter writer = null;
    private Scanner reader = null;

    public JoueurServer getJoueurServer() {
        return joueurServer;
    }

    public void setJoueurServer(JoueurServer joueurServer) {
        this.joueurServer = joueurServer;
    }

    private JoueurServer joueurServer;

    public AppLogicServer getApp() {
        return app;
    }

    private AppLogicServer app;

    public  ClientProcessor(Socket pSock, AppLogicServer pApp) {
        sock = pSock;
        app = pApp;
        try {
            reader = new Scanner(sock.getInputStream());
            writer = new PrintWriter(sock.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Lancement du traitement de la connexion cliente dans le thread" + Thread.currentThread().getName() + " - en attente de commandes");
        boolean closeConnexion = false;

        while (!sock.isClosed()) {


            //On attend la demande du client
            while (reader.hasNextLine()) {
                String response = reader.nextLine();
                //On affiche quelques infos, pour le débuggage
                System.err.println("[" + LocalDateTime.now().toString() + "] - Message reçu du client" + this.getJoueurServer().getNom() + " : " + response);
                traiterCommande(response);
            }

        }
        System.err.println("LA CONNEXION A ETE INTERROMPUE POUR " + Thread.currentThread().getName() + " !");

    }


    public void  write(String message)
    {
        System.out.println("[" + LocalDateTime.now().toString() + "] - Envoie du message au client "+ this.joueurServer.getNom() + " : " + message);
        writer.println(message);
        writer.flush();
    }

    private void traiterCommande(String pCommande) {
    String commande = pCommande.split(";")[0];
        switch(commande){
            case "SEND_NAME":
                joueurServer.setNom(pCommande.split(";")[1]);
                joueurServer.envoieMessage(ClientCommandes.WELCOME.name()+";"+app.getNbJoueurs());
                app.envoieLesJoueursDejaConnectes(joueurServer);
                app.envoieMessageATousSaufMoi(ClientCommandes.CONNECT.name()+";" +joueurServer.getNom(), joueurServer);
                app.verifieSiToutLeMondeEstLa();
                break;
            case "JOUEUR_A_FAIT_CHOIX_FAMILLE":
                joueurServer.setFamille(app.getRiskGOTFamilles().getFamilleParNom(pCommande.split(";")[1]));
                System.out.println("Le nom de la famille attribuée au joueur " + joueurServer.getNom() + " est " + joueurServer.getFamille().getFamilyName().name());
                app.envoieMessageATousSaufMoi(ClientCommandes.JOUEUR_A_FAIT_CHOIX_FAMILLE.name()+";" +joueurServer.getNom()+";"+joueurServer.getFamille().getFamilyName().name(), joueurServer);
                app.demandeProchainJoueurDeFaireChoixFamille();
                break;
                case "CHAT":
                app.envoieMessageATous(pCommande);
                break;
            case"LANCE_1DE_START":
                app.aLanceUnDeStart(Integer.parseInt(pCommande.split(";")[1]),joueurServer);
                break;
            default:
                System.out.println("Commande inconnue");
                break;
        }
    }


}