package network;

import applogic.objects.JoueurClient;
import common.ClientCommandes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static common.ClientCommandes.CHOIX_TERRITOIRES_DEMARRAGE_TERMINE;

public class ClientConnexion extends JoueurClient implements Runnable  {
    private Socket connexion = null;
    private PrintWriter writer = null;
    private Scanner reader = null;
    private static int count = 0;



    public ClientConnexion(String host, int port) throws IOException {
        super();
        nom = "INCONNU";
        try {
            connexion = new Socket(host, port);
            System.out.println("Nouvelle connexion créé: sur le serveur" + host + ":" + port);
            writer = new PrintWriter(connexion.getOutputStream(), true);
            reader = new Scanner(connexion.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("CONNECTION REFUSEE SUR LE SERVEUR");
            throw e;
        }
    }


    public void sendCommand(ClientCommandes pCom, String message) {
        String toSend = "";
        toSend += pCom.toString();
        boolean writeInChat=false;
        switch (pCom) {
            case JOUEUR_A_FAIT_CHOIX_FAMILLE:
            case SEND_NAME:
            case JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE:
            case JOUEUR_A_AJOUTE_UNE_TROUPE_DEMARRAGE:
            case LANCE_1DE_START:
                toSend += ";" + message;
                writeInChat=true;
                break;
            case CHAT:
                toSend += ";" + message;
                break;
            default:
                toSend = "Commande inconnue !";
                break;
        }
        if (writeInChat)
        {
            this.updateChatText("[TO_SERVER]:"+toSend);
        }
        writeCommande(toSend);

    }

    private void writeCommande(String pToSend) {
        //On envoie la commande au serveur
        writer.println(pToSend);
        //TOUJOURS UTILISER flush() POUR ENVOYER RÉELLEMENT DES INFOS AU SERVEUR
        writer.flush();
        System.out.println("COMMANDE ENVOYEE AU SERVEUR : " + pToSend);
        //On affiche la commande dans le chat
    }


    public void closeConnexion() {
        try {
            this.writer.close();
            this.reader.close();
            this.connexion.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if( connexion!=null) {
            while (!connexion.isClosed())
                try {
                    while (reader.hasNextLine()) {
                        String message = reader.nextLine();
                        System.out.println("COMMANDE RECUE DU SERVEUR : " + message);
                        this.traiterCommande(message);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
        }
        System.err.println("Connection fermée !");
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
        if (!com.equals(ClientCommandes.CHAT) && !com.equals(ClientCommandes.INFO))
        {
            this.updateChatText("[FROM_SERVER]:"+pCommande);

        }
        switch (com) {
            case WELCOME:
                commandeWelcome(pCommande);
                break;
            case TOUS_CONNECTES:
                commandeTousConnecte(pCommande);
                break;
            case CHAT:
                updateChatText(pCommande.substring(5));
                break;
            case CONNECT:
                commandeNouvelleConnection(pCommande);
                break;
            case RESULTAT_1_DE:
                break;
            case INFO:
                updateChatText(pCommande.substring(5).replaceAll("#","\n"));
                break;
            case JOUEUR_A_FAIT_CHOIX_FAMILLE:
                commandeMiseAJourFamilleJoueur(pCommande);
                break;
            case FAIRE_CHOIX_FAMILLE:
                commandeFaireChoixFamille(pCommande);
                break;
            case CHOIX_FAMILLE_TERMINE:
                commandeChoixFamilleTerminee(pCommande);
                break;
            case CHOISIR_UN_TERRITOIRE_DEMARRAGE:
                commandeChoisirUnTerritoireDemarrage(pCommande);
                break;
            case JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE:
                commandeJoueurAChoisiUnTerritoireDemarrage(pCommande);
                break;
            case CHOIX_TERRITOIRES_DEMARRAGE_TERMINE:
                break;
            case AJOUTER_UNE_TROUPE_DEMARRAGE:
                commandePlacerUneTroupeDemarrage(pCommande);
                break;
            case JOUEUR_A_AJOUTE_UNE_TROUPE_DEMARRAGE:
                commandeJoueurAChoisiUneTroupeDemarrage(pCommande);
                break;
            case PLACEMENT_DEMARRAGE_TERMINE:
                break;
            default:
                System.out.println("Commande inconnue");
                break;
        }

    }

    public void lanceUnDe(Integer de) {
        sendCommand(ClientCommandes.LANCE_1DE_START,de.toString());
    }




}


