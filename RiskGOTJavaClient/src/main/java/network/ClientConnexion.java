package network;

import applogic.objects.ChatMessage;
import applogic.objects.JoueurClient;
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
        toSend += ";"+message;
        if (false) {
            this.updateChatText(new ChatMessage(toSend, ChatMessage.ChatMessageType.TO_SERVEUR_DEBUG));
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
                        System.err.println("COMMANDE RECUE DU SERVEUR : " + message);
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
        String message ="";
        try{
            com = ClientCommandes.valueOf(commande);
            message = pCommande.substring(commande.length()+1);
        }
        catch (IllegalArgumentException iae) {
            com =  ClientCommandes.INCONNUE;
        }
        if (false) {
            this.updateChatText(new ChatMessage(pCommande, ChatMessage.ChatMessageType.FROM_SERVEUR_DEBUG));
        }
        switch (com) {
            case WELCOME:
                commandeWelcome(message);
                break;
            case TOUS_CONNECTES:
                commandeTousConnecte(message);
                break;
            case CHAT:
                commandeChat(message);
                break;
            case CONNECT:
                commandeNouvelleConnection(message);
                break;
            case RESULTAT_1_DE:
                commandeResultat1DE(message);
                break;
            case INFO:
                updateChatText(new ChatMessage(message.replaceAll("#", "\n"), ChatMessage.ChatMessageType.INFO));
                break;
            case JOUEUR_ACTIF:
                commandeJoueurActif(message);
                break;
            case JOUEUR_A_FAIT_CHOIX_FAMILLE:
                commandeMiseAJourFamilleJoueur(message);
                break;
            case FAIRE_CHOIX_FAMILLE:
                commandeFaireChoixFamille(message);
                break;
            case CHOIX_FAMILLE_TERMINE:
                commandeChoixFamilleTerminee(message);
                break;
            case CHOISIR_LES_CARTES_OBJECTIFS_DEMARRAGE:
                commandeChoisirLesCartesObjectifDemarrage(message);
                break;
            case JOUEUR_A_CHOISI_SES_OBJECTIFS_DEMARRAGE:
                commandeJoueurAChoisiLesCartesObjectifsDemarrage(message);
                break;
            case CHOIX_CARTES_OBJECTIFS_DEMARRAGE_TERMINE:
                commandeChoixCartesObjectifDemarrageTermine(message);
                break;
            case CHOISIR_UN_TERRITOIRE_DEMARRAGE:
                commandeChoisirUnTerritoireDemarrage(message);
                break;
            case JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE:
                commandeJoueurAChoisiUnTerritoireDemarrage(message);
                break;
            case CHOIX_TERRITOIRES_DEMARRAGE_TERMINE:
                commandeChoixTerritoireTermine(message);
                break;
            case JOUEUR_A_RENFORCE_UN_TERRITOIRE:
                commandeJoueurARenforceUnTerritoire(message);
                break;
            case DEPLOYEZ_UNE_TROUPE:
                commandeDeployez(message);
                break;
            case PLACEMENT_DEMARRAGE_TERMINE:
                commandePlacementTermine(message);
                break;
            case TOUR_1_RENFORCEZ:
                commandeRenforcez(message);
                break;
            case TOUR_1_ACHETEZ_DES_CARTES:
                commandeAchetezDesCartes(message);
                break;
            case JOUEUR_DEMANDE_A_ACHETER_UN_OBJECTIF:
                commandeJoueurDemandeAAcheterUnObjectif(message);
                break;
            case CHOISIR_UN_OBJECTIF:
                commandeChoisirUneCarteObjectif(message);
                break;
            case JOUEUR_A_CHOISI_UN_OBJECTIF:
                commandeJoueurAChoisiUnObjectif(message);
                break;
            case JOUEUR_PASSE_ACHAT_DE_CARTES:
                commandeJoueurPasseAchatDeCartes(message);
                break;
            case TOUR_1_ENVAHISSEZ:
                commandeEnvahissez(message);
                break;
            case LANCER_INVASION:
                commandeLancerInvasion(message);
                break;
            case JOUEUR_LANCE_UNE_INVASION:
                commandeJoueurLanceUneInvasion(message);
                break;
            case JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_ATTAQUE:
                commandeJoueurAValideSesTroupesEnAttaque(message);
                break;
            case JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_DEFENSE:
                commandeJoueurAValideSesTroupesEnDefense(message);
                break;
            case LANCEZ_VOS_DES_POUR_LA_BATAILLE:
                commandeLancezVosDesPourLaBataille(message);
                break;
            case JOUEUR_A_LANCE_LES_DES_EN_ATTAQUE:
                commandeJoueurALanceLesDesEnAttaque(message);
                break;
            case JOUEUR_A_LANCE_LES_DES_EN_DEFENSE:
                commandeJoueurALanceLesDesEnDefense(message);
                break;
            case JOUEUR_A_VALIDE_LE_RESULTAT_DE_LA_BATAILLE:
                commandeJoueurAValideLeResultatDeLaBataille(message);
                break;
            case LA_BATAILLE_EST_TERMINEE:
                commandeLaBatailleEstTerminee(message);
                break;
            case LA_BATAILLE_EST_TERMINEE_ET_LE_RESULTAT_EST_VALIDE:
                commandeLaBatailleEstTermineeEtLeResultatEstValide(message);
                break;
            case INVASION_TERMINEE_DEFAITE_DEFENSEUR:
                commandeInvasionTermineeDefaiteDefenseur(message);
                break;
            case INVASION_TERMINEE_DEFAITE_ATTAQUANT:
                commandeInvasionTermineeDefaiteAttaquant(message);
                break;
            case INVASION_PEUT_CONTINUER:
                commandeInvasionPeutContinuer(message);
                break;
            case JOUEUR_EFFECTUE_UNE_MANOEUVRE_EN_FIN_DINVASION:
            case JOUEUR_A_EFFECTUE_UNE_MANOEUVRE:
                commandeJoueurEffectueUneManoeuvre(message);
                break;
            case JOUEUR_CONTINUE_INVASION:
                commandeJoueurContinueUneInvasion(message);
                break;
            case JOUEUR_ARRETE_UNE_INVASION:
                commandeJoueurArreteUneInvasion(message);
                break;
            case MANOEUVREZ_EN_FIN_DE_TOUR:
                commandeManoeuvrezEnFinDeTour(message);
                break;
            case JOUEUR_PASSE_LA_MANOEUVRE:
                commandeJoueurPasseLaManoeuvre(message);
                break;
            case ATTEIGNEZ_UN_OBJECTIF_EN_FIN_DE_TOUR:
                commandeAtteignezUnObjectifEnFinDeTour(message);
                break;
            case JOUEUR_A_ATTEINT_UN_OBJECTIF_EN_FIN_DE_TOUR:
                commandeJoueurAAtteintUnObjectifEnFinDeTour(message);
                break;
            case JOUEUR_EST_VICTORIEUX:
                commandeJoueurEstVictorieux(message);
                break;
            case JOUEUR_NATTEINT_PAS_DOBJECTIF:
                commandeJoueurNatteintPasDobjectif(message);
                break;
            case JOUEUR_PEUT_PIOCHER_UNE_CARTE_TERRITOIRE:
                commandeJoueurPeutPiocheUneCarteTerritoire(message);
                break;
            case JOUEUR_NE_PEUT_PAS_PIOCHER_UNE_CARTE_TERRITOIRE:
                commandeJoueurNePeutPasPiocherDeCarteTerritoireEnFinDeTour(message);
                break;
            case JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE:
                commandeJoueurAPiocheUneCarteTerritoire(message);
                break;
            case JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE_EN_FIN_DE_TOUR:
                commandeJoueurAPiocheUneCarteTerritoireEnFinDeTour(message);
                break;
            case JOUEUR_TERMINE_SON_TOUR:
                commandeJoueurTermineSonTour(message);
                break;
            case CONVERTISSEZ_VOS_CARTES_TERRITOIRES_EN_TROUPES_OU_UNITE_SPECIALES:
                commandeConvertissezVosCartesTerritoires();
                break;
            case JOUEUR_A_CONVERTI_UNE_CARTE_TERRITOIRE_EN_UNITE_SPECIALE:
                commandeJoueurAConvertiUneCarteTerritoireEnUniteSpeciale(message);
                break;
            case JOUEUR_A_CONVERTI_TROIS_CARTES_TERRITOIRE_EN_TROUPES_SUPPLEMENTAIRES:
                commandeJoueurAConvertiTroisCartesTerritoiresEnTroupesSupplementaires(message);
                break;
            case JOUEUR_A_DEPLOYE_UNE_UNITE_SPECIALE:
                commandeJoueurADeployeUneUniteSpeciale(message);
                break;
            case JOUEUR_VA_UTILISER_UNE_CARTE_PERSONNAGE:
                commandeJoueurVaUtiliserUneCartePersonnage(message);
                break;
            case CARTE_PERSONNAGE_NON_UTILISABLE:
                commandeCartePersonnageNonUtilisable(message);
                break;
            case JOUEUR_NA_PAS_ASSEZ_DARGENT_POUR_JOUER_SA_CARTE_PERSONNAGE:
                commandePasAssezDargentPourUtiliserCartePersonnage(message);
                break;
            case JOUEUR_PEUT_METTRE_UN_DES_DES_A_SA_VALEUR_MAXIMALE:
                commandeJoueurPeutMettreUnDesDesASaValeurMaximale(message);
                break;
            case JOUEUR_REMPACE_DES_DES_SIX_PAR_DES_DES_HUIT_PENDANT_LINVASION:
                commandeJoueurRemplaceDesDesSixParDesDesHuit(message);
                break;
            case ATTAQUANT_GAGNE_LES_EGALITES_PENDANT_LINVASION:
                commandeAttaquantGagneLesEgalitesPendantLinvasion();
                break;
            case JOUEUR_VOL_DE_LARGENT:
                commandeJoueurVolDeLargent(message);
                break;
            default:
                System.out.println("Commande inconnue");
                break;
        }

    }




}


