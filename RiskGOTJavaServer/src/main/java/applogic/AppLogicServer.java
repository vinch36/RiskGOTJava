package applogic;

import common.util.Etat;
import common.ClientCommandes;
import common.objects.*;
import network.JoueurServer;
import network.ServerListener;

import java.util.*;

import static java.lang.Thread.sleep;
import static java.util.Arrays.sort;

public class AppLogicServer {

    public boolean debugMode = false;
    private ArrayList<JoueurServer> joueurServers;

    public Familles getRiskGOTFamilles() {
        return riskGOTFamilles;
    }

    private Familles riskGOTFamilles;
    private Regions riskGOTregions;

    public Regions getRiskGOTregions() {
        return riskGOTregions;
    }

    public Territoires getRiskGOTterritoires() {
        return riskGOTterritoires;
    }

    private Territoires riskGOTterritoires;

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
        System.out.println("Veuillez entrer le nombre de joueurs + enter. Le nombre de joueur doit être un chiffre compris entre [3 et 5]");
        String input = entree.next();
        try {
            this.nbJoueurs = Integer.parseInt(input.trim());
        } catch (NumberFormatException nfe) {
            System.out.println("On vous a dit un Chiffre !!");
            saisieNbJoueurs();
        }
        if (nbJoueurs < 3 || nbJoueurs > 5) {
            System.out.println("On vous a dit entre 3 et 5 !!");
            saisieNbJoueurs();
        }
        joueurServers = new ArrayList<>(nbJoueurs);
        riskGOTFamilles = new Familles(nbJoueurs);
        riskGOTregions = new Regions();
        riskGOTterritoires = new Territoires(riskGOTregions, riskGOTFamilles);


        System.out.println("Nombre de joueurs = " + nbJoueurs);
    }


    public void attenteConnectionDesJoueurs() {
        ServerListener server = new ServerListener("127.0.0.1", 7777);
        server.attenteConnexions(this);
        //quand on arrive là, on sait que les n joueurs attendus


    }

    public void connexionRecu(JoueurServer joueurServer) {
        joueurServers.add(joueurServer);
    }


    public void verifieSiToutLeMondeEstLa() {
        if (joueurServers.size() == nbJoueurs) {
            if (!debugMode) {
                envoieMessageATous(ClientCommandes.TOUS_CONNECTES, "NORMAL");
            } else {
                envoieMessageATous(ClientCommandes.TOUS_CONNECTES, "DEBUG");
            }
        }
    }

    public void aLanceUnDeStart(int pDeVal, JoueurServer pJoueurServer) {
        pJoueurServer.setCurrentDeStartResult(pDeVal);
        pJoueurServer.setHasPlayed(true);
        envoieMessageATous(ClientCommandes.RESULTAT_1_DE, pJoueurServer.getNom() + ";" + pDeVal);
        //Si tout le monde à lancé son dé, on va passer à l'étape d'après, qui est de choisir la famille
        if (verifieSiToutLeMondeALanceSonDeStart()&&etatprincipal!=Etat.CHOIX_FAMILLE) {
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

    public void demarreChoixFamille() {
        setEtatprincipal(Etat.CHOIX_FAMILLE);
        //Tous les joueurs ont joué. On tri maintenant es joueurs par leur ordre de dé décroissant, et on envoie le classement à tous le monde.
        tri_selection_ordre_par_de(this.joueurServers);
        String message = "";
        int i = 1;
        for (JoueurServer j : joueurServers) {
            message = message + i + ". " + j.getNom() + " a fait " + j.getCurrentDeStartResult() + " et son heure de connection était " + j.getCreationTime() + "#";
            i++;
        }
        //On informe du résultat
        envoieMessageATous(ClientCommandes.INFO, message);
        if (!debugMode) {
            demandeProchainJoueurDeFaireChoixFamille();
        } else {
            initChoixFamille4Debug();
        }

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
            message = "";
            for (Famille f : riskGOTFamilles.getFamillesActives()) {
                if (!f.isaUnJoueurAssocie()) {
                    message = message + f.getFamilyName().name() + ";";
                }
            }
            message = message.substring(0, message.length() - 1);
            envoieMessage(prochainJoueurAFaireLeChoix, ClientCommandes.FAIRE_CHOIX_FAMILLE, message);
        } else //Dans ce cas, prochainJoueurAFaireLeChoix = null, donc on en déduit que tout le monde à fait un choix de famille. On peut passer à la phase suivante, le démarrage du jeu !
        {
            envoieMessageATous(ClientCommandes.INFO, "Tous les joueurs ont choisis une maison#On va pouvoir démarrer !");
            joueurCourant = this.riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Stark).getJoueur();
            demarrePlacementDesTroupes();

        }
    }


    public void demarrePlacementDesTroupes() {
        setEtatprincipal(Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE);
        int nbTroupesRestantAPlacer = riskGOTFamilles.initCapitales(nbJoueurs);
        envoieMessageATous(ClientCommandes.INFO, "Le placement des troupes peut  démarrer.#Chaque joueur est doté de " + nbTroupesRestantAPlacer + " à placer, à tour de rôle.");
        envoieMessageATous(ClientCommandes.CHOIX_FAMILLE_TERMINE, ((Integer) nbTroupesRestantAPlacer).toString());

        //On démarre le cycle de placement des troupes, au début, on choisit les territoires.
        demandeProchainJoueurDeChoisirUnTerritoire();
    }


    public void demandeProchainJoueurDeChoisirUnTerritoire() {

        if (riskGOTterritoires.isEncoreAuMoinsUnTerritoireLibre()) {
            String listeDesTerritoireRestants = riskGOTterritoires.getTerritoiresNonAttribuesAsString();
            String message = joueurCourant.getNbTroupeAPlacer() + ";" + listeDesTerritoireRestants;
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.CHOISIR_UN_TERRITOIRE_DEMARRAGE, message);
            prochainJoueur();
        } else {
            envoieMessageATous(ClientCommandes.CHOIX_TERRITOIRES_DEMARRAGE_TERMINE, "");
            demarrePlacementDesTroupesDemarrage();
        }
    }

    public void demarrePlacementDesTroupesDemarrage()
    {
        setEtatprincipal(Etat.PLACER_LES_TROUPES_DEMARRAGE);
        demandeProchainJoueurDePlacerUneTroupe();
    }

    public void demandeProchainJoueurDePlacerUneTroupe() {
        if (joueurCourant.getNbTroupeAPlacer() > 0) {
            //String listeDesTerritoireRestants = riskGOTterritoires.getTerritoiresNonAttribuesAsString();
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.AJOUTER_UNE_TROUPE_DEMARRAGE, String.valueOf(joueurCourant.getNbTroupeAPlacer()));
            prochainJoueur();
        } else {
            envoieMessageATous(ClientCommandes.PLACEMENT_DEMARRAGE_TERMINE, "C'EST LA GUERRE !!!");
            //DEMARAGE DE LA GUERRE !!!
        }
    }

    public void joueurAChoisiUnTerritoire(JoueurServer joueur, Territoire ter) {
        if (ter.getAppartientAJoueur() == null) {
            ter.setAppartientAJoueur(joueur);
        }
        ter.ajouteDesTroupes(1);
        envoieMessageATous(ClientCommandes.JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE, joueur.getNom() + ";" + ter.getNom().name());
    }

    public void joueurAAjouteUneTroupeDemarrage(JoueurServer joueur, Territoire ter) {
        ter.ajouteDesTroupes(1);
        envoieMessageATous(ClientCommandes.JOUEUR_A_AJOUTE_UNE_TROUPE_DEMARRAGE, joueur.getNom() + ";" + ter.getNom().name());
    }


    public void envoieMessageATous(ClientCommandes cmd, String message) {

        for (JoueurServer j : joueurServers) {
            j.write(cmd.name() + ";" + message);
        }

    }

    public void envoieMessage(JoueurServer joueurServer, ClientCommandes cmd, String message) {
        joueurServer.write(cmd.name() + ";" + message);
    }

    public void envoieMessageATousSaufMoi(ClientCommandes cmd, String message, JoueurServer joueurServer) {
        for (JoueurServer j : joueurServers) {
            if (j != joueurServer)
                j.write(cmd.name() + ";" + message);
        }
    }


    public void envoieLesJoueursDejaConnectes(JoueurServer pJoueurServer) {
        for (JoueurServer j : joueurServers) {
            if (j != pJoueurServer) {
                pJoueurServer.write(ClientCommandes.CONNECT.name() + ";" + j.getNom());
            }
        }

    }

    private Joueur joueurCourant;

    public void prochainJoueur() {
        Famille familleSuivante = riskGOTFamilles.getFamilleSuivante(joueurCourant.getFamille());
        joueurCourant = familleSuivante.getJoueur();
        System.out.println("PROCHAIN JOUEUR = " + joueurCourant.getNom() + " - [" + joueurCourant.getFamille().getFamilyName().name() + "]");
    }


    public static void tri_selection_ordre_par_de(ArrayList<JoueurServer> tab) {
        for (int i = 0; i < tab.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < tab.size(); j++) {
                if (tab.get(j).getCurrentDeStartResult() >= tab.get(index).getCurrentDeStartResult()) {
                    if (tab.get(j).getCurrentDeStartResult() == tab.get(index).getCurrentDeStartResult()) {
                        if (tab.get(j).getCreationTime().isBefore(tab.get(index).getCreationTime())) {
                            index = j;
                        }
                    } else {
                        index = j;
                    }
                }

            }

            JoueurServer max = tab.get(index);
            tab.set(index, tab.get(i));
            tab.set(i, max);
        }
    }


    //DEBUG AREA

    public void initChoixTerritoire4Debug() {
        if (etatprincipal != Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE) {
            System.out.println("Le serveur n'est pas dans un état permettant l'initialisation");
        } else {
            for (int i = 1; i < nbJoueurs; i++) {
                prochainJoueur();
            }
            while (riskGOTterritoires.isEncoreAuMoinsUnTerritoireLibre()) {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Territoire ter = riskGOTterritoires.getTerritoiresNonAttribues().get((int) (this.riskGOTterritoires.getTerritoiresNonAttribues().size() * Math.random()));
                ter.setAppartientAJoueur(joueurCourant);
                ter.ajouteDesTroupes(1);
                envoieMessageATous(ClientCommandes.JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE, joueurCourant.getNom() + ";" + ter.getNom().name());
                prochainJoueur();
            }
            envoieMessageATous(ClientCommandes.CHOIX_TERRITOIRES_DEMARRAGE_TERMINE, "");
            //demarrePlacementDesTroupesDemarrage();
        }

    }


    public void initPlacementDesTroupes4Debug() {
        if (etatprincipal != Etat.PLACER_LES_TROUPES_DEMARRAGE) {
            System.out.println("Le serveur n'est pas dans un état permettant l'initialisation");
        } else {
            for (int i = 1; i < nbJoueurs; i++) {
                prochainJoueur();
            }
            boolean cpasfini = true;
            while (cpasfini) {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cpasfini = false;
                if (joueurCourant.getNbTroupeAPlacer() > 0) {
                    cpasfini = true;
                    Territoire ter = joueurCourant.territoires.get((int) (joueurCourant.territoires.size() * Math.random()));
                    ter.ajouteDesTroupes(1);
                    envoieMessageATous(ClientCommandes.JOUEUR_A_AJOUTE_UNE_TROUPE_DEMARRAGE, joueurCourant.getNom() + ";" + ter.getNom().name());
                    prochainJoueur();
                }
            }
            envoieMessageATous(ClientCommandes.PLACEMENT_DEMARRAGE_TERMINE, "C'EST LA GUERRE !!!");
            //DEMARAGE DE LA GUERRE !!!
        }
    }

    public void init4Debug()
    {
        if (etatprincipal != Etat.ATTENTE_CONNECTION_JOUEURS) {
            System.out.println("Le serveur n'est pas dans un état permettant l'initialisation");
        }
        else{
            debugMode=true;
        }

    }

    private  void initChoixFamille4Debug()
    {
        Famille famille = riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Stark);
        for (JoueurServer j : joueurServers) {
            j.setFamille(famille);
            envoieMessageATous(ClientCommandes.JOUEUR_A_FAIT_CHOIX_FAMILLE, j.getNom()+";"+famille.getFamilyName().name());
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            famille = riskGOTFamilles.getFamilleSuivante(famille);
        }

        envoieMessageATous(ClientCommandes.INFO, "Tous les joueurs ont choisis une maison#On va pouvoir démarrer !");
        joueurCourant = this.riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Stark).getJoueur();
        int nbTroupesRestantAPlacer = riskGOTFamilles.initCapitales(nbJoueurs);
        envoieMessageATous(ClientCommandes.INFO, "Le placement des troupes peut  démarrer.#Chaque joueur est doté de " + nbTroupesRestantAPlacer + " à placer, à tour de rôle.");
        envoieMessageATous(ClientCommandes.CHOIX_FAMILLE_TERMINE, ((Integer) nbTroupesRestantAPlacer).toString());
        setEtatprincipal(Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE);
        initChoixTerritoire4Debug();
        setEtatprincipal(Etat.PLACER_LES_TROUPES_DEMARRAGE);
        initPlacementDesTroupes4Debug();

    }


}

