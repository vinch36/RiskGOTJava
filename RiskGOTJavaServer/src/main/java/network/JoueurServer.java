package network;

import applogic.AppLogicServer;
import common.ClientCommandes;
import common.objects.Joueur;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class JoueurServer extends Joueur implements Runnable  {
    private Socket sock;
    private PrintWriter writer = null;
    private Scanner reader = null;

    private boolean hasPlayed;

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    private LocalDateTime creationTime;

    public int getCurrentDeStartResult() {
        return currentDeStartResult;
    }

    public void setCurrentDeStartResult(int currentDeStartResult) {
        this.currentDeStartResult = currentDeStartResult;
    }

    int currentDeStartResult;

    public boolean isHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }


    public AppLogicServer getApp() {
        return app;
    }

    private AppLogicServer app;

    public JoueurServer(Socket pSock, AppLogicServer pApp) {
        super();
        this.nom="";
        this.hasPlayed=false;
        this.creationTime =LocalDateTime.now();
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
                System.err.println("[" + LocalDateTime.now().toString() + "] - Message reçu du client" + this.getNom() + " : " + response);
                traiterCommande(response);
            }

        }
        System.err.println("LA CONNEXION A ETE INTERROMPUE POUR " + Thread.currentThread().getName() + " !");

    }


    public void  write(String message)
    {
        System.out.println("[" + LocalDateTime.now().toString() + "] - Envoie du message au client "+ this.getNom() + " : " + message);
        writer.println(message);
        writer.flush();
    }

    private void traiterCommande(String pCommande) {
        String commande = pCommande.split(";")[0];
        ClientCommandes com;
        try{
            com = ClientCommandes.valueOf(commande);
        }
        catch (IllegalArgumentException iae) {
            com =  ClientCommandes.INCONNUE;
        }

        switch(com){
            case SEND_NAME:
                this.setNom(pCommande.split(";")[1]);
                app.envoieMessage(this, ClientCommandes.WELCOME, ((Integer)app.getNbJoueurs()).toString());
                app.envoieLesJoueursDejaConnectes(this);
                app.envoieMessageATousSaufMoi(ClientCommandes.CONNECT, this.getNom(), this);
                app.verifieSiToutLeMondeEstLa();
                break;
            case JOUEUR_A_FAIT_CHOIX_FAMILLE:
                this.setFamille(app.getRiskGOTFamilles().getFamilleParNomString(pCommande.split(";")[1]));
                app.envoieMessageATous(ClientCommandes.JOUEUR_A_FAIT_CHOIX_FAMILLE, this.getNom()+";"+this.getFamille().getFamilyName().name());
                app.demandeProchainJoueurDeFaireChoixFamille();
                break;
                case CHAT:
                app.envoieMessageATous(ClientCommandes.CHAT,pCommande.split(";")[1]);
                break;
            case LANCE_1DE_START:
                app.aLanceUnDeStart(Integer.parseInt(pCommande.split(";")[1]),this);
                break;
            case JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE:
                app.joueurAChoisiUnTerritoire(this,app.getRiskGOTterritoires().getTerritoireParNomStr(pCommande.split(";")[2]));
                app.demandeProchainJoueurDeChoisirUnTerritoire();
                break;
            case JOUEUR_A_AJOUTE_UNE_TROUPE_DEMARRAGE:
                app.joueurAAjouteUneTroupeDemarrage(this,app.getRiskGOTterritoires().getTerritoireParNomStr(pCommande.split(";")[2]));
                app.demandeProchainJoueurDePlacerUneTroupe();
                break;
            default:
                System.out.println("Commande inconnue");
                break;
        }
    }


}