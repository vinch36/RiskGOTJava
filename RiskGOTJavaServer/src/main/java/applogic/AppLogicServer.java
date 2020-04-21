package applogic;

import applogic.objects.Etat;
import applogic.objects.JoueurServer;
import common.ClientCommandes;
import common.Famille;
import common.Familles;
import network.ClientProcessor;
import network.ServerListener;

import java.security.Timestamp;
import java.util.*;

import static java.util.Arrays.sort;

public class AppLogicServer {

    private ArrayList<JoueurServer> joueurServers;

    public Familles getRiskGOTFamilles() {
        return riskGOTFamilles;
    }

    private Familles riskGOTFamilles;

    public int getNbJoueurs() {
        return nbJoueurs;
    }

    int nbJoueurs;

    public Etat getEtatprincipal() {
        return etatprincipal;
    }

    public void setEtatprincipal(Etat etatprincipal) {
        this.etatprincipal = etatprincipal;
        System.out.println("################################################################");
        System.out.println("Changement de l'état du serveur -->" + etatprincipal.toString());
        System.out.println("################################################################");
    }

    private Etat etatprincipal;

    //Constructeur de l'AppLogic
    public AppLogicServer() {
        this.setEtatprincipal(Etat.DEMARRE);

    }

    //
    public void saisieNbJoueurs() {
        Scanner entree = new Scanner(System.in);
        System.out.println("Veuillez entrer le nombre de joueurs + enter. Le nombre de joueur doit être un chiffre compris entre [2 et 4]");
        String input = entree.next();
        try
        {
            this.nbJoueurs = Integer.parseInt(input.trim());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("NumberFormatException: " + nfe.getMessage());
            Runtime. getRuntime(). exit(1);
        }
        if (nbJoueurs<2 || nbJoueurs>4)
        {
            System.out.println("Nombre de joueurs = " + nbJoueurs);
            Runtime. getRuntime(). exit(1);
        }
        joueurServers = new ArrayList<>(nbJoueurs);
        riskGOTFamilles = new Familles(nbJoueurs);

        System.out.println("Nombre de joueurs = " + nbJoueurs);
    }



    public void connexionRecu(ClientProcessor clientProcessor) {
        JoueurServer j = new JoueurServer();
        clientProcessor.setJoueurServer(j);
        j.setClientProcessor(clientProcessor);
        joueurServers.add(j);
    }

    public void demarreChoixFamille() {
        setEtatprincipal(Etat.CHOIX_FAMILLE);
        //Tous les joueurs ont joué. On tri maintenant es joueurs par leur ordre de dé décroissant, et on envoie le classement à tous le monde.
        tri_selection_ordre_par_de(this.joueurServers);
        String message = ClientCommandes.INFO.name() + ";";
        int i = 1;
        for (JoueurServer j : joueurServers) {
            message = message + i + ". " + j.getNom() + " a fait " + j.getCurrentDeStartResult() + " et son heure de connection était " + j.getCreationTime() + "#";
            i++;
        }
        //On informe du résultat
        envoieMessageATous(message);
        demandeProchainJoueurDeFaireChoixFamille();
    }

    public void demandeProchainJoueurDeFaireChoixFamille() {
        String message;
        JoueurServer prochainJoueurAFaireLeChoix = null;
        for (JoueurServer j : joueurServers) {
            //Envoyer l'info au premier joueur dans l'ordre n'a pas encore fait son choix de faire le choix de la famille, avec la liste des familles disponibles restantes
            if (prochainJoueurAFaireLeChoix == null) {
                if (j.getFamille() == null)
                    prochainJoueurAFaireLeChoix = j;
            }
        }

        if (prochainJoueurAFaireLeChoix != null) {
            message = ClientCommandes.FAIRE_CHOIX_FAMILLE.name();
            for (Famille f : riskGOTFamilles.getFamilles()) {
                if (!f.isaUnJoueurAssocie()) {
                    message = message + ";" + f.getFamilyName().name();
                }
            }
            prochainJoueurAFaireLeChoix.envoieMessage(message);
        }
        else //Dans ce cas, prochainJoueurAFaireLeChoix = null, donc on en déduit que tout le monde à fait un choix de famille. On peut passer à la phase suivante, le démarrage du jeu !
        {
            envoieMessageATous(ClientCommandes.INFO.name()+";Tous les joueurs ont choisis une maison#On va pouvoir démarrer !");
        }
    }


    public void attenteConnectionDesJoueurs() {
        ServerListener server = new ServerListener("127.0.0.1", 7777);
        server.attenteConnexions(this);
        //quand on arrive là, on sait que les n joueurs attendus


    }



    public void verifieSiToutLeMondeEstLa()
    {
        if (joueurServers.size()==nbJoueurs) {
            envoieMessageATous(ClientCommandes.TOUS_CONNECTES.name());
        }
    }


    public void envoieMessageATous(String message){

        for (JoueurServer j: joueurServers) {
            j.envoieMessage(message);
        }

    }

    public void envoieMessageATousSaufMoi(String message, JoueurServer joueurServer) {
        for (JoueurServer j: joueurServers) {
            if (j!=joueurServer)
            j.envoieMessage(message);
        }
    }


    public void envoieLesJoueursDejaConnectes(JoueurServer pJoueurServer){
        for (JoueurServer j: joueurServers) {
            if (j!=pJoueurServer)
            {
                pJoueurServer.envoieMessage(ClientCommandes.CONNECT.name() + ";"+ j.getNom());
            }
        }

    }



    public static void tri_selection_ordre_par_de(ArrayList<JoueurServer> tab)
    {
        for (int i = 0; i < tab.size() - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j < tab.size(); j++)
            {
                if (tab.get(j).getCurrentDeStartResult() >= tab.get(index).getCurrentDeStartResult()){
                    if (tab.get(j).getCurrentDeStartResult() == tab.get(index).getCurrentDeStartResult()){
                        if(tab.get(j).getCreationTime().isBefore(tab.get(index).getCreationTime())){
                            index = j;
                        }
                    }
                    else {
                        index = j;
                    }
                }

            }

            JoueurServer max = tab.get(index);
            tab.set(index,tab.get(i));
            tab.set(i,max);
        }
    }

    public void aLanceUnDeStart(int pDeVal, JoueurServer pJoueurServer) {
        pJoueurServer.setCurrentDeStartResult(pDeVal);
        pJoueurServer.setHasPlayed(true);
        envoieMessageATous("RESULTAT_1_DE;"+pJoueurServer.getNom()+";"+pDeVal);
        //Si tout le monde à lancé son dé, on va passer à l'étape d'après, qui est de choisir la famille
        if(verifieSiToutLeMondeALanceSonDeStart())
        {
            this.demarreChoixFamille();
        }
    }

    private boolean verifieSiToutLeMondeALanceSonDeStart() {
        for (JoueurServer j : joueurServers) {
            if (!j.isHasPlayed()) {
                return false;
            }
        }
        return true;

    }

}
