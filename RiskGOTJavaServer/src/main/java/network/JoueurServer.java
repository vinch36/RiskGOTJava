package network;

import applogic.AppLogicServer;
import common.ClientCommandes;
import common.objects.Joueur;
import common.objects.Territoire;
import common.objects.cartes.CarteTerritoire;

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
        String message ="";
        try{
            com = ClientCommandes.valueOf(commande);
            message = pCommande.substring(commande.length()+1);
        }
        catch (IllegalArgumentException iae) {
            com =  ClientCommandes.INCONNUE;
        }

        switch(com){
            case SEND_NAME:
                this.setNom(message);
                app.envoieMessage(this, ClientCommandes.WELCOME, ((Integer)app.getNbJoueurs()).toString());
                app.envoieLesJoueursDejaConnectes(this);
                app.envoieMessageATousSaufMoi(ClientCommandes.CONNECT, this.getNom(), this);
                app.verifieSiToutLeMondeEstLa();
                break;
            case JOUEUR_A_FAIT_CHOIX_FAMILLE:
                app.joueurAFaitChoixFamille(this, app.getRiskGOTFamilles().getFamilleParNomString(message));
                break;
                case CHAT:
                app.envoieMessageATous(ClientCommandes.CHAT,message);
                break;
            case LANCE_1DE_START:
                app.aLanceUnDeStart(Integer.parseInt(message),this);
                break;
            case JOUEUR_A_CHOISI_SES_OBJECTIFS_DEMARRAGE:
                //Message = Id de la carte objectif rejetée
                app.joueurAChoisiSesCartesObjectifsDemarrage(this,message);
                break;
            case JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE:
                app.joueurAChoisiUnTerritoire(this,app.getRiskGOTterritoires().getTerritoireParNomStr(message.split(";")[1]));
                app.demandeProchainJoueurDeChoisirUnTerritoire(false);
                break;
            case JOUEUR_A_RENFORCE_UN_TERRITOIRE:
                app.joueurARenforceUnTerritoire(this,app.getRiskGOTterritoires().getTerritoireParNomStr(message.split(";")[1]));
                break;
            case JOUEUR_LANCE_UNE_INVASION:
                app.joueurALanceUneInvasion(this,app.getRiskGOTterritoires().getTerritoireParNomStr(message.split(";")[1]), app.getRiskGOTterritoires().getTerritoireParNomStr(message.split(";")[2]));
                break;
            case JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_ATTAQUE:
                app.joueurAValideNombreDeTroupesEnAttaque(this, Integer.parseInt(message.split(";")[0]), Integer.parseInt(message.split(";")[1]), Integer.parseInt(message.split(";")[2]));
                break;
            case JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_DEFENSE:
                app.joueurAValideNombreDeTroupesEnDefense(this, Integer.parseInt(message.split(";")[0]), Integer.parseInt(message.split(";")[1]), Integer.parseInt(message.split(";")[2]), Integer.parseInt(message.split(";")[3]));
                break;
            case JOUEUR_A_LANCE_LES_DES_EN_ATTAQUE:
                app.joueurALanceLesDesEnAttaque(this, message);
                break;
            case JOUEUR_A_LANCE_LES_DES_EN_DEFENSE:
                app.joueurALanceLesDesEnDefense(this, message);
                break;
            case JOUEUR_EFFECTUE_UNE_MANOEUVRE_EN_FIN_DINVASION:
                app.joueurAManoeuvrerEnFinDinvasion(this, Integer.parseInt(message));
                break;
            case JOUEUR_CONTINUE_INVASION:
                app.joueurContinueInvasion();
                break;
            case JOUEUR_ARRETE_UNE_INVASION:
                app.joueurArreteInvasion();
                break;
            case JOUEUR_ATTAQUANT_REALISE_SA_DEFAITE:
                app.envoieMessage(this,ClientCommandes.LANCER_INVASION,"");
                break;
            case JOUEUR_A_EFFECTUE_UNE_MANOEUVRE:
                app.joueurAManoeuvreEnFinDeTour(this, app.getRiskGOTterritoires().getTerritoireParNomStr(message.split(";")[1]), app.getRiskGOTterritoires().getTerritoireParNomStr(message.split(";")[2]), Integer.parseInt(message.split(";")[3]));
                break;
            case JOUEUR_PASSE_LA_MANOEUVRE:
                app.joueurPasseLaManoeuvreEnFinDeTour(this);
                break;
            case JOUEUR_A_CONVERTI_TROIS_CARTES_TERRITOIRE_EN_TROUPES_SUPPLEMENTAIRES:
                app.joueurAConvertiTroisCartesTerritoiresEnTroupesSupplementaires(this, app.getRiskGOTCartesTerritoires().getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[0])), app.getRiskGOTCartesTerritoires().getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[1])), app.getRiskGOTCartesTerritoires().getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[2])));
                break;
            case JOUEUR_A_CONVERTI_UNE_CARTE_TERRITOIRE_EN_UNITE_SPECIALE:
                app.joueurAConvertiUneCarteTerritoireEnUniteSpeciale(this, app.getRiskGOTCartesTerritoires().getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[0])));
                break;
            case JOUEUR_PASSE_LA_CONVERSION_DE_CARTES_TERRITOIRES:
                app.joueurPasseLaConversionDeCartesTerritoires(this);
                break;
            case JOUEUR_A_DEPLOYE_UNE_UNITE_SPECIALE:
                app.joueurADeployeUneUniteSpeciale(this, app.getRiskGOTterritoires().getTerritoireParNomStr(message.split(";")[0]), CarteTerritoire.UniteSpeciale.valueOf(message.split(";")[1]));
                break;
            default:
                System.out.println("Commande inconnue");
                break;
        }
    }


}