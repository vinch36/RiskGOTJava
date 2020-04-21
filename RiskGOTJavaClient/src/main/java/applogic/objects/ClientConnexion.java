package applogic.objects;

import common.ClientCommandes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientConnexion extends JoueurClient implements Runnable  {
    private Socket connexion = null;
    private PrintWriter writer = null;
    private Scanner reader = null;
    private static int count = 0;



    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientConnexion(String host, int port) throws IOException {
        super();
        this.name = "INCONNU";
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
        switch (pCom.toString()) {
            case "JOUEUR_A_FAIT_CHOIX_FAMILLE":
            case "SEND_NAME":
            case "LANCE_1DE_START":
                toSend += ";" + message;
                writeInChat=true;
                break;
            case "CLOSE":
                toSend = "Communication terminée";
                //closeConnexion = true;
                break;
            case "CHAT":
                toSend += ";" + message;
                break;
            case "RESULTAT_ORDRE_FAMILLE":
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
        if (!commande.equals(ClientCommandes.CHAT.name()) && !commande.equals(ClientCommandes.INFO.name()))
        {
            this.updateChatText("[FROM_SERVER]:"+pCommande);

        }
        switch (commande) {
            case "WELCOME":
                commandeWelcome(pCommande);
                break;
            case "TOUS_CONNECTES":
                commandeTousConnecte(pCommande);
                break;
            case "CHAT":
                updateChatText(pCommande.substring(5));
                break;
            case "CONNECT":
                commandeNouvelleConnection(pCommande);
                break;
            case "RESULTAT_1_DE":
                break;
            case "INFO":
                updateChatText(pCommande.substring(5).replaceAll("#","\n"));
                break;
            case "JOUEUR_A_FAIT_CHOIX_FAMILLE":
                commandeMiseAJourFamilleJoueur(pCommande);
                break;
            case "FAIRE_CHOIX_FAMILLE":
                commandeFaireChoixFamille(pCommande);
                break;
            default:
                System.out.println("Commande inconnue");
                break;
        }

    }

    public void lanceUnDe() {
        Integer de=(int)(1+6*Math.random());
        sendCommand(ClientCommandes.LANCE_1DE_START,de.toString());
    }




}


