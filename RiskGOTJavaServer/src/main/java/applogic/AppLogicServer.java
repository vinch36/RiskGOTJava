package applogic;

import common.objects.cartes.*;
import common.util.*;
import common.ClientCommandes;
import common.objects.*;
import network.JoueurServer;
import network.ServerListener;

import java.util.*;


import static common.ClientCommandes.*;
import static java.lang.Thread.sleep;
import static java.util.Arrays.sort;

public class AppLogicServer {


    private Manoeuvre manoeuvreEnCours;
    private Invasion invasionEnCours;
    public boolean debugMode = false;
    private ArrayList<JoueurServer> joueurServers;

    public Familles getRiskGOTFamilles() {
        return riskGOTFamilles;
    }

    private Familles riskGOTFamilles;
    private Regions riskGOTregions;

    public CartesTerritoires getRiskGOTCartesTerritoires() {
        return riskGOTCartesTerritoires;
    }

    private CartesTerritoires riskGOTCartesTerritoires;

    public Regions getRiskGOTregions() {
        return riskGOTregions;
    }

    public Territoires getRiskGOTterritoires() {
        return riskGOTterritoires;
    }

    private Territoires riskGOTterritoires;

    public CartesObjectifs getRiskGOTCartesObjectifs() {
        return riskGOTCartesObjectifs;
    }

    private CartesObjectifs riskGOTCartesObjectifs;


    public int getNbJoueurs() {
        return nbJoueurs;
    }

    int nbJoueurs;

    public Etat getEtatprincipal() {
        return etatprincipal;
    }

    public void setEtatprincipal(Etat etatprincipal) {
        this.etatprincipal = etatprincipal;
        System.out.println("#########################################################################");
        System.out.println("Changement de l'état du serveur -->" + etatprincipal.toString());
        System.out.println("#########################################################################");
    }

    private Etat etatprincipal;

    public SousEtat getSousEtatPrincipal() {
        return sousEtatPrincipal;
    }

    public void setSousEtatPrincipal(SousEtat sousEtatPrincipal) {
        this.sousEtatPrincipal = sousEtatPrincipal;
        System.out.println("----------------------------------------------------------------");
        System.out.println("Changement de du sous état du serveur -->" + sousEtatPrincipal.toString());
        System.out.println("----------------------------------------------------------------");
    }
    private SousEtat sousEtatPrincipal;


    public SousEtatEnvahissez getSousEtatEnvahissez() {
        return sousEtatEnvahissez;
    }

    public void setSousEtatEnvahissez(SousEtatEnvahissez sousEtatEnvahissez) {
        this.sousEtatEnvahissez = sousEtatEnvahissez;
        System.out.println("................................................................");
        System.out.println("Changement de du sous état Envahissez du serveur -->" + sousEtatEnvahissez.toString());
        System.out.println("................................................................");
    }

    private SousEtatEnvahissez sousEtatEnvahissez;

    public SousEtatRenforcez getSousEtattRenforcez() {
        return sousEtatRenforcez;
    }

    public void setSousEtatRenforcez(SousEtatRenforcez sousEtatRenforcez) {
        this.sousEtatRenforcez = sousEtatRenforcez;
        System.out.println("................................................................");
        System.out.println("Changement de du sous état Renforcez du serveur -->" + sousEtatRenforcez.toString());
        System.out.println("................................................................");
    }

    private SousEtatRenforcez sousEtatRenforcez;

    public SousEtatManoeuvrez getSousEtattManoeuvrez() {
        return sousEtatManoeuvrez;
    }

    public void setSousEtatManoeuvrez(SousEtatManoeuvrez sousEtatManoeuvrez) {
        this.sousEtatManoeuvrez = sousEtatManoeuvrez;
        System.out.println("................................................................");
        System.out.println("Changement de du sous état Manoeuvrez du serveur -->" + sousEtatManoeuvrez.toString());
        System.out.println("................................................................");
    }

    private SousEtatManoeuvrez sousEtatManoeuvrez;


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
        riskGOTCartesTerritoires = new CartesTerritoires(riskGOTterritoires);
        riskGOTCartesObjectifs = new CartesObjectifs();
        System.out.println("Nombre de joueurs = " + nbJoueurs);
    }


    public void attenteConnectionDesJoueurs() {
        ServerListener server = new ServerListener("192.168.0.38", 7777);
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
        envoieMessageATous(ClientCommandes.RESULTAT_1_DE, pJoueurServer.getNom() + ";" + pDeVal+";"+pJoueurServer.getCreationTime());
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
        String message = "CLASSEMENT AU DU LANCE DE DE#";
        int i = 1;
        for (JoueurServer j : joueurServers) {
            message = message + i + ". " + j.getNom()+ "#";
            i++;
        }
        message=message+"Il est maintenant temps de choisir une Maison. Vous allez recevoir un message quand c'est votre tour.";
        //On informe du résultat
        envoieMessageATous(ClientCommandes.INFO, message);
        if (!debugMode) {
            demandeProchainJoueurDeFaireChoixFamille();
        } else {
            initChoixFamille4Debug();
        }

    }

    public void joueurAFaitChoixFamille(Joueur pJoueur, Famille pFamille){
        pJoueur.setFamille(pFamille);
        envoieMessageATous(ClientCommandes.JOUEUR_A_FAIT_CHOIX_FAMILLE, pJoueur.getNom()+";"+pJoueur.getFamille().getFamilyName().name());
        if (pFamille==riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Martell)||pFamille==riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Tyrell)) {
            CarteTerritoire carteTerritoire = riskGOTCartesTerritoires.piocher(pJoueur);
            envoieMessageATous(ClientCommandes.JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE, pJoueur.getNom()+";"+carteTerritoire.getTerritoire().getNom().name());
        }
        demandeProchainJoueurDeFaireChoixFamille();
    }

    private void demandeProchainJoueurDeFaireChoixFamille() {
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
        } else //Dans ce cas, prochainJoueurAFaireLeChoix = null, donc on en déduit que tout le monde à fait un choix de famille. On peut passer à la phase suivante, le choix des objectifs !
        {
            envoieMessageATous(ClientCommandes.CHOIX_FAMILLE_TERMINE,"");
            //envoieMessageATous(ClientCommandes.INFO, "Tous les joueurs ont choisis une maison#On va pouvoir choisir des objectifs !");
            demarrerChoixCartesObjectifDemarrage();
            //demarrePlacementDesTroupes();

        }
    }


    private void demarrerChoixCartesObjectifDemarrage()
    {
        setEtatprincipal(Etat.CHOISIR_LES_CARTES_OBJECTIFS_DEMARRAGE);
        for (JoueurServer joueur : joueurServers) {
            CarteObjectif carte1 = riskGOTCartesObjectifs.piocher(joueur);
            CarteObjectif carte2 = riskGOTCartesObjectifs.piocher(joueur);
            CarteObjectif carte3 = riskGOTCartesObjectifs.piocher(joueur);
            //On pioche 3 cartes, et on les envoie au joueur
            envoieMessage(joueur, ClientCommandes.CHOISIR_LES_CARTES_OBJECTIFS_DEMARRAGE, carte1.getIdAsStr()+";"+carte2.getIdAsStr()+";"+carte3.getIdAsStr());
        }

    }

    public void joueurAChoisiSesCartesObjectifsDemarrage(Joueur pJoueur, String message)
    {
        //message : "02,Y;05,N;34,Y"
        CarteObjectif carteNonSelectionnee = null;
        for (String str : message.split(";")){
            if (str.split(",")[1].equals("N")){
                carteNonSelectionnee = this.riskGOTCartesObjectifs.getCarteObjectifParIdStr((str.split(",")[0]));
            }
        }


        pJoueur.jetteUneCarteObjectif(carteNonSelectionnee);
        envoieMessageATous(ClientCommandes.JOUEUR_A_CHOISI_SES_OBJECTIFS_DEMARRAGE, pJoueur.getNom()+";"+message);
        if (verifieSiToutLeMondeAChoisiSesObjectifsDemarrage()){
            demarrePlacementDesTroupes();
        }
    }


    private boolean verifieSiToutLeMondeAChoisiSesObjectifsDemarrage() {
        for (JoueurServer j : joueurServers) {
            if (j.getCartesObjectif().size()!=2) { //On doit piocher 2 objectifs au démarrage, donc si on en a pas exactement 2, c'est qu'on a pas fait le boulot !
                return false;
            }
        }
        return true;

    }


    public void demarrePlacementDesTroupes() {
        setEtatprincipal(Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE);
        int nbTroupesRestantAPlacer = riskGOTFamilles.initCapitales(nbJoueurs);
        envoieMessageATous(ClientCommandes.INFO, "Le placement des troupes peut  démarrer.#Chaque joueur est doté de " + nbTroupesRestantAPlacer + " à placer, à tour de rôle.");
        envoieMessageATous(ClientCommandes.CHOIX_CARTES_OBJECTIFS_DEMARRAGE_TERMINE, ((Integer) nbTroupesRestantAPlacer).toString());

        //On démarre le cycle de placement des troupes, au début, on choisit les territoires.
        demandeProchainJoueurDeChoisirUnTerritoire(true);
    }


    public void demandeProchainJoueurDeChoisirUnTerritoire(boolean pDemarrage) {
        if (riskGOTterritoires.isEncoreAuMoinsUnTerritoireLibre()) {
            prochainJoueur(pDemarrage);
            String listeDesTerritoireRestants = riskGOTterritoires.getTerritoiresNonAttribuesAsString();
            String message = joueurCourant.getNbTroupeAPlacer() + ";" + listeDesTerritoireRestants;
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.CHOISIR_UN_TERRITOIRE_DEMARRAGE, message);
        } else {
            envoieMessageATous(ClientCommandes.CHOIX_TERRITOIRES_DEMARRAGE_TERMINE, "");
            demarrePlacementDesTroupesDemarrage();
        }
    }
    public void joueurAChoisiUnTerritoire(JoueurServer joueur, Territoire ter) {
        if (ter.getAppartientAJoueur() == null) {
            ter.setAppartientAJoueur(joueur);
        }
        ter.ajouteDesTroupesAPlacer(1);
        envoieMessageATous(ClientCommandes.JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE, joueur.getNom() + ";" + ter.getNom().name());
    }


    public void demarrePlacementDesTroupesDemarrage()
    {
        setEtatprincipal(Etat.PLACER_LES_TROUPES_DEMARRAGE);
        demandeProchainJoueurDePlacerUneTroupeDemarrage(true);
    }

    private boolean verifieSiIlResteDesTroupeAPlacerDemarrage()
    {
        for (JoueurServer j : joueurServers){
            if(j.getNbTroupeAPlacer()>0){
                return true;
            }
        }
        return false;
    }
    public void demandeProchainJoueurDePlacerUneTroupeDemarrage(boolean pDemarrage) {

        if (verifieSiIlResteDesTroupeAPlacerDemarrage()) {
            prochainJoueur(pDemarrage);
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.DEPLOYEZ_UNE_TROUPE, String.valueOf(joueurCourant.getNbTroupeAPlacer()));

        } else {
            envoieMessageATous(ClientCommandes.PLACEMENT_DEMARRAGE_TERMINE,"");
            //DEMARAGE DE LA GUERRE !!!
            demarrerUnTour(true);
        }
    }

    public void demarrerUnTour(boolean pDemarrage)
    {
        prochainJoueur(pDemarrage);
        setEtatprincipal(Etat.TOUR_DE_JEU);
        setSousEtatPrincipal(SousEtat.RENFORCEZ);
        volDargentEnCours=0;
        nbCartePiochables=0;
        //1.RENFORCEZ
        //1.1 CALCULER LES RENFORTS
        int renforts = joueurCourant.calculerNombreDeRenfortsDeBase();
        //1.2 CALCULER LES BONUS REGIONS
        int bonus = joueurCourant.calculerBonusRegion();
        int nbPorts = joueurCourant.getNombreDePorts();

        joueurCourant.setNbTroupeAPlacer(renforts+bonus);
        //1.3 CONVERTIR EN PIECES D'OR
        joueurCourant.setArgent((bonus+renforts+nbPorts)*100);
        envoieMessageATous(ClientCommandes.TOUR_1_RENFORCEZ, joueurCourant.getNom() +";"+renforts+";"+bonus+";"+nbPorts+";"+joueurCourant.getArgent());
        //1.4 ECHANGER 3 CARTES TERRITOIRES POUR TROUPES SUPP + 1 PAR TERRITOIRE CONTROLE
        //1.5 ECHANGER UNE CARTE TERRITOIRE EN UNITE SPECIALE
        convertissezVosCartesTerritoires();
        //1.6 DEPLOYER LES ARMEES


    }


    private void  convertissezVosCartesTerritoires(){

        if (joueurCourant.getCartesTerritoires().size()>0) {
            this.setSousEtatRenforcez(SousEtatRenforcez.ECHANGEZ_DES_CARTES_TERRITOIRE);
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.CONVERTISSEZ_VOS_CARTES_TERRITOIRES_EN_TROUPES_OU_UNITE_SPECIALES, "");
        }
        else {
            this.setSousEtatRenforcez(SousEtatRenforcez.DEPLOYEZ_DES_TROUPES);
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.DEPLOYEZ_UNE_TROUPE, String.valueOf(joueurCourant.getNbTroupeAPlacer()));
        }

    }





    public void joueurARenforceUnTerritoire(JoueurServer joueur, Territoire ter) {
        ter.ajouteDesTroupesAPlacer(1);
        envoieMessageATous(ClientCommandes.JOUEUR_A_RENFORCE_UN_TERRITOIRE, joueur.getNom() + ";" + ter.getNom().name());
        if (getEtatprincipal()==Etat.TOUR_DE_JEU)
        {
            if (getSousEtatPrincipal()==SousEtat.RENFORCEZ){
                continuerLeRenfort();
            }

        }
        if (getEtatprincipal()==Etat.PLACER_LES_TROUPES_DEMARRAGE){
            demandeProchainJoueurDePlacerUneTroupeDemarrage(false);
        }
    }

    private void continuerLeRenfort()
    {
        if (joueurCourant.getNbTroupeAPlacer()>0){
            envoieMessage((JoueurServer)joueurCourant, ClientCommandes.DEPLOYEZ_UNE_TROUPE, String.valueOf(joueurCourant.getNbTroupeAPlacer()));
        }
        else
        {
                acheterDesCartes();
        }

    }


    private void acheterDesCartes()
    {
        this.setSousEtatPrincipal(SousEtat.ACHETEZ_DES_CARTES);
        envoieMessageATous(ClientCommandes.TOUR_1_ACHETEZ_DES_CARTES, joueurCourant.getNom());
    }

    public void joueurDemandeAAcheterUnObjectif(JoueurServer pJoueur){
        CarteObjectif carte1 = riskGOTCartesObjectifs.piocher(pJoueur);
        CarteObjectif carte2 = riskGOTCartesObjectifs.piocher(pJoueur);
        pJoueur.setArgent(pJoueur.getArgent()-200);
        envoieMessageATous(ClientCommandes.JOUEUR_DEMANDE_A_ACHETER_UN_OBJECTIF, pJoueur.getNom());
        envoieMessage(pJoueur, ClientCommandes.CHOISIR_UN_OBJECTIF, carte1.getIdAsStr()+";"+carte2.getIdAsStr());

    }

    public void joueurAChoisiSonObjectif(Joueur pJoueur, String message) {
        //message : "02,Y;05,N
        CarteObjectif carteNonSelectionnee = null;
        String carteStr1 = message.split(";")[0];
        String carteStr2 = message.split(";")[1];

        if (carteStr1.split(",")[1].equals("N")) {

            carteNonSelectionnee = this.riskGOTCartesObjectifs.getCarteObjectifParIdStr((carteStr1.split(",")[0]));
        } else if (carteStr2.split(",")[1].equals("N")) {
            carteNonSelectionnee = this.riskGOTCartesObjectifs.getCarteObjectifParIdStr((carteStr2.split(",")[0]));
        } else {
            System.err.println("GROS BUG joueurAChoisiSonObjectif - aucune carte selectionnée  !!");
        }
        pJoueur.jetteUneCarteObjectif(carteNonSelectionnee);
        envoieMessageATous(ClientCommandes.JOUEUR_A_CHOISI_UN_OBJECTIF, pJoueur.getNom() + ";" + message);
        if (this.sousEtatPrincipal == SousEtat.ACHETEZ_DES_CARTES) {
            acheterDesCartes();
        } else if (this.sousEtatPrincipal == SousEtat.ATTEIGNEZ_UN_OBJECTIF) {
            piocherUneCarteTerritoireEnFinDeTour();
        }
        else {
            System.err.println("GROS BUG joueurAChoisiSonObjectif - SousEtat icompatible ??  !!");
        }
    }


    public void joueurPasseAchatDeCarte(JoueurServer pJoueur)
    {
        envoieMessageATous(ClientCommandes.JOUEUR_PASSE_ACHAT_DE_CARTES, pJoueur.getNom());
        envahir();
    }

    private void envahir(){

        //ACtualisation des personnages
        for (CartePersonnage cartePersonnage :joueurCourant.getFamille().getCartesPersonnages()){
            cartePersonnage.setUtilisee(false);
        }
        invasionEnCours = new Invasion();
        this.setSousEtatPrincipal(SousEtat.ENVAHISSEZ);
        this.setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_TERRITOIRES);
        envoieMessageATous(ClientCommandes.TOUR_1_ENVAHISSEZ, joueurCourant.getNom());
        envoieMessage((JoueurServer)joueurCourant, ClientCommandes.LANCER_INVASION,"");

    }


    public void joueurALanceUneInvasion(JoueurServer pJoueurServer, Territoire pTerritoireSource, Territoire pTerritoireCible)
    {
        invasionEnCours.setTerritoireSource(pTerritoireSource);
        invasionEnCours.setTerritoireCible(pTerritoireCible);
        if (volDargentEnCours>0&&pTerritoireCible.getAppartientAJoueur().getArgent()>=volDargentEnCours){
            pTerritoireCible.getAppartientAJoueur().setArgent(pTerritoireCible.getAppartientAJoueur().getArgent()-volDargentEnCours);
            pTerritoireSource.getAppartientAJoueur().setArgent(pTerritoireSource.getAppartientAJoueur().getArgent()+volDargentEnCours);
            envoieMessageATous(ClientCommandes.JOUEUR_VOL_DE_LARGENT,pTerritoireSource.getAppartientAJoueur().getNom()+";"+pTerritoireCible.getAppartientAJoueur().getNom()+";"+volDargentEnCours);
        }
        this.setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_NB_TROUPES);
        String message = pJoueurServer.getNom()+";"+pTerritoireSource.getNom()+";"+pTerritoireCible.getNom();
        envoieMessageATous(ClientCommandes.JOUEUR_LANCE_UNE_INVASION,message);

    }



    public void joueurAValideNombreDeTroupesEnAttaque(JoueurServer pJoueurServer, int pNbToupes, int pNbChevaliers, int pNbEnginsDeSiege)
    {
        //Check que c'est bien le bon joueur:)
        if (invasionEnCours.getTerritoireSource().getAppartientAJoueur() == pJoueurServer){
            String message = pJoueurServer.getNom()+";"+pNbToupes+";"+pNbChevaliers+";"+pNbEnginsDeSiege;
            invasionEnCours.getTerritoireSource().setArmeeEngagees(pNbToupes);
            invasionEnCours.getTerritoireSource().setChevaliersEngagesDansLaBataille(pNbChevaliers);
            invasionEnCours.getTerritoireSource().setEnginsDeSiegeEngagesDansLaBataille(pNbEnginsDeSiege);
            invasionEnCours.setJoueurSourceAValideSesTroupes(true);
            envoieMessageATous(ClientCommandes.JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_ATTAQUE,message);
            if (invasionEnCours.toutLeMondeAValideSesTroupes()){
                envoieMessageATous(ClientCommandes.LANCEZ_VOS_DES_POUR_LA_BATAILLE,"");
            }
        }
        else
            {
            System.err.println("Olala ya un bug (joueurAValideNombreDeTroupesEnAttaque)");
        }

    }



    public void joueurAValideNombreDeTroupesEnDefense(JoueurServer pJoueurServer, int pNbToupes, int pNbChevaliers, int pNbEnginsDeSiege, int pNbFortifications)
    {
        //Check que c'est bien le bon joueur:)
        if (invasionEnCours.getTerritoireCible().getAppartientAJoueur() == pJoueurServer) {
            String message = pJoueurServer.getNom()+";"+pNbToupes+";"+pNbChevaliers+";"+pNbEnginsDeSiege+";"+pNbFortifications;
            invasionEnCours.getTerritoireCible().setArmeeEngagees(pNbToupes);
            invasionEnCours.getTerritoireCible().setChevaliersEngagesDansLaBataille(pNbChevaliers);
            invasionEnCours.getTerritoireCible().setEnginsDeSiegeEngagesDansLaBataille(pNbEnginsDeSiege);
            invasionEnCours.getTerritoireCible().setFortificationsEngagesDansLaBataille(pNbFortifications);
            invasionEnCours.setJoueurCibleAValideSesTroupes(true);
            envoieMessageATous(ClientCommandes.JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_DEFENSE, message);
            if (invasionEnCours.toutLeMondeAValideSesTroupes()) {
                envoieMessageATous(ClientCommandes.LANCEZ_VOS_DES_POUR_LA_BATAILLE, "");
            }
        } else {
            System.err.println("Olala ya un bug (joueurAValideNombreDeTroupesEnDefense)");
        }
    }


    public void joueurALanceLesDesEnAttaque(JoueurServer pJoueur, String message) {
        if (invasionEnCours.getTerritoireSource().getAppartientAJoueur() == pJoueur) {
            calculerLesDesEtLeursBonus(pJoueur, message);
            String message2 = "";

            for (DeTypeValeur deTypeValeur: invasionEnCours.getResultatsDesAttaquant()) {
                message2 = message2 + deTypeValeur.getTypeDe().name() + ","+ deTypeValeur.getValeur() + "," +deTypeValeur.getBonus() + ";";
            }

            invasionEnCours.setJoueurSourceALanceLesDes(true);
            envoieMessageATous(ClientCommandes.JOUEUR_A_LANCE_LES_DES_EN_ATTAQUE,pJoueur.getNom()+";"+ message2);

            if (invasionEnCours.resoudreLaBatailleEnCoursApresLeLancerDeDes()) {
                envoieMessageATous(ClientCommandes.LA_BATAILLE_EST_TERMINEE, "");
                this.setSousEtatEnvahissez(SousEtatEnvahissez.BATAILLE_TERMINEE);
            }
        } else {
            System.err.println("Olala ya un bug (joueurALanceLesDesEnAttaque)");
        }

    }


    public void joueurALanceLesDesEnDefense(JoueurServer pJoueur, String message)
    {
        if (invasionEnCours.getTerritoireCible().getAppartientAJoueur() == pJoueur){
            calculerLesDesEtLeursBonus(pJoueur, message);

            String message2 = "";

            for (DeTypeValeur deTypeValeur: invasionEnCours.getResultatsDesDefenseur()) {
                message2 = message2 + deTypeValeur.getTypeDe().name() + ","+ deTypeValeur.getValeur() + "," +deTypeValeur.getBonus() + ";";
            }

            invasionEnCours.setJoueurCibleALanceLesDes(true);
            envoieMessageATous(ClientCommandes.JOUEUR_A_LANCE_LES_DES_EN_DEFENSE,pJoueur.getNom()+";"+ message2);
            if (invasionEnCours.resoudreLaBatailleEnCoursApresLeLancerDeDes()) {

                envoieMessageATous(ClientCommandes.LA_BATAILLE_EST_TERMINEE, invasionEnCours.getNbTroupesPerduesEnAttaque()+";"+invasionEnCours.getNbTroupesPerduesEnDefense());
                this.setSousEtatEnvahissez(SousEtatEnvahissez.BATAILLE_TERMINEE);
            }
        } else
        {
            System.err.println("Olala ya un bug (joueurALanceLesDesEnDefense)");
        }
    }

    private void calculerLesDesEtLeursBonus(JoueurServer pJoueur, String message)
    {
        if (invasionEnCours.getJoueurDefenseur() == pJoueur){
            invasionEnCours.resetResultatDesDefenseur();
            boolean chevaliersTraites = false;
            for (String s: message.split(";")) //Hypothèse, ils sont déjà triés dans l'ordre
            {
                DeTypeValeur deTypeValeur = null;
                if (!chevaliersTraites) {
                    deTypeValeur = new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), invasionEnCours.getTerritoireCible().getChevaliersEngagesDansLaBataille()+invasionEnCours.getTerritoireCible().getFortificationsEngagesDansLaBataille()+invasionEnCours.getBonusDefenseur());
                    chevaliersTraites=true;
                }
                else{
                    deTypeValeur = new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), invasionEnCours.getTerritoireCible().getFortificationsEngagesDansLaBataille()+invasionEnCours.getBonusDefenseur());
                }
                invasionEnCours.getResultatsDesDefenseur().add(deTypeValeur);
            }
        }
        if (invasionEnCours.getJoueurAttaquant() == pJoueur){
            invasionEnCours.resetResultatDesAttaquant();
            boolean chevaliersTraites = false;
            for (String s: message.split(";")) //Hypothèse, ils sont déjà triés dans l'ordre
            {
                DeTypeValeur deTypeValeur = null;
                if (!chevaliersTraites) {
                    deTypeValeur = new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), invasionEnCours.getTerritoireSource().getChevaliersEngagesDansLaBataille()+invasionEnCours.getTerritoireSource().getFortificationsEngagesDansLaBataille()+invasionEnCours.getBonusAttaquant());
                    chevaliersTraites=true;
                }
                else{
                    deTypeValeur = new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), invasionEnCours.getTerritoireSource().getFortificationsEngagesDansLaBataille()+invasionEnCours.getBonusAttaquant());
                }
                invasionEnCours.getResultatsDesAttaquant().add(deTypeValeur);
            }
        }

    }

    public void joueurAValideLeResultatDeLaBataille(JoueurServer pJoueur, String message)
    {
        if (invasionEnCours.getTerritoireCible().getAppartientAJoueur() == pJoueur) {
            invasionEnCours.setJoueurSourceAValideLeResultatDeLaBataille(true);
        }
        if (invasionEnCours.getTerritoireSource().getAppartientAJoueur() == pJoueur){
            invasionEnCours.setJoueurCibleAValideLeResultatDeLaBataille(true);
        }
        envoieMessageATous(JOUEUR_A_VALIDE_LE_RESULTAT_DE_LA_BATAILLE,pJoueur.getNom());
        if (invasionEnCours.resoudreLaBatailleEnCoursApresToutesLesValidations()){
            envoieMessageATous(ClientCommandes.LA_BATAILLE_EST_TERMINEE_ET_LE_RESULTAT_EST_VALIDE, invasionEnCours.getNbTroupesPerduesEnAttaque()+";"+invasionEnCours.getNbTroupesPerduesEnDefense());
            preparerLaProchaineBataille();
        }
    }


    private void preparerLaProchaineBataille()
    {
        String message = invasionEnCours.getTerritoireSource().getNom().name()+";"+invasionEnCours.getTerritoireSource().getNombreDeTroupes()+";"+invasionEnCours.getTerritoireCible().getNom().name()+";"+invasionEnCours.getTerritoireCible().getNombreDeTroupes();
        if (invasionEnCours.getTerritoireCible().getNombreDeTroupes()==0){
            //Dans ce cas, le joueur défenseur a perdu le territoire - l'invasion se termine donc. Il faut gérer le changement de territoire, et la répartition nouvelle des troupes (manoeuvre).

            invasionEnCours.victoireAttaquant();
            if (invasionEnCours.isManoeuvrerSansContrainte()) {
                envoieMessageATous(ClientCommandes.INVASION_TERMINEE_DEFAITE_DEFENSEUR, message + ";true");
            }
            else{
                envoieMessageATous(ClientCommandes.INVASION_TERMINEE_DEFAITE_DEFENSEUR, message + ";false");
            }
            setSousEtatEnvahissez(SousEtatEnvahissez.MANOEUVREZ);
            if (invasionEnCours.getJoueurAttaquant().getFamille().getCapitale()==invasionEnCours.getTerritoireCible()) {
                //On vérifie dans le cas ou l'attaquant reprend sa capitale si par hasard il n'a pas gagné
                verifierLaVictoireDunJoueur(invasionEnCours.getJoueurAttaquant());
            }

        }
        else if (invasionEnCours.getTerritoireSource().getNombreDeTroupes()<2){
            //Dans ce cas, le joueur attaquant n'a plus assez de troupe pour attaquer. Il faut arrêter l'invasion là.
            invasionEnCours.victoireDefenseur();
            envoieMessageATous(ClientCommandes.INVASION_TERMINEE_DEFAITE_ATTAQUANT, message);
        }
        else {
            //L'invasion peut continuer !
            envoieMessageATous(ClientCommandes.INVASION_PEUT_CONTINUER,message);
        }

    }


    public void joueurAManoeuvrerEnFinDinvasion(JoueurServer pJoueur, int nbTroupesEnManoeuvre) {

        if (invasionEnCours.getJoueurAttaquant() == pJoueur) {
            invasionEnCours.getTerritoireSource().ajouteDesTroupes(-nbTroupesEnManoeuvre);
            invasionEnCours.getTerritoireCible().ajouteDesTroupes(nbTroupesEnManoeuvre);
            envoieMessageATous(ClientCommandes.JOUEUR_EFFECTUE_UNE_MANOEUVRE_EN_FIN_DINVASION,pJoueur.getNom()+";"+invasionEnCours.getTerritoireSource().getNom().name()+";"+invasionEnCours.getTerritoireCible().getNom().name()+";"+nbTroupesEnManoeuvre);
            this.setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_TERRITOIRES);
            invasionEnCours = new Invasion();
            envoieMessage((JoueurServer)joueurCourant, ClientCommandes.LANCER_INVASION,"");
        } else {
            System.err.println("Olala ya un bug (joueurAManoeuvrerEnFinDinvasion)");

        }
    }


    public void joueurContinueInvasion()
    {
        Joueur joueur = invasionEnCours.getJoueurAttaquant();
        invasionEnCours.setNbBataillesTermineesDansLinvasion(invasionEnCours.getNbBataillesTermineesDansLinvasion()+1);
        invasionEnCours.resetPourProchaineBataille();
        String message = joueur.getNom()+";"+invasionEnCours.getTerritoireSource().getNom().name()+";"+invasionEnCours.getTerritoireCible().getNom().name() + ";"+invasionEnCours.getNbBataillesTermineesDansLinvasion();
        this.setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_NB_TROUPES);
        envoieMessageATous(ClientCommandes.JOUEUR_CONTINUE_INVASION,message);


    }

    public void joueurArreteInvasion()
    {
        envoieMessageATous(ClientCommandes.JOUEUR_ARRETE_UNE_INVASION,joueurCourant.getNom());
        this.setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_TERRITOIRES);
        invasionEnCours = new Invasion();
        envoieMessage((JoueurServer)joueurCourant, ClientCommandes.LANCER_INVASION,"");
    }

    public void joueurAttaquantRealiseSaDefaite(JoueurServer joueurServer)
    {
        setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_TERRITOIRES);
        invasionEnCours = new Invasion();
        envoieMessage(joueurServer,ClientCommandes.LANCER_INVASION,"");
    }

    private int volDargentEnCours=0;



    public void joueurArreteLesInvasions(JoueurServer pJoueur){
        setSousEtatPrincipal(SousEtat.MANOEUVREZ);
        envoieMessageATous(MANOEUVREZ_EN_FIN_DE_TOUR,pJoueur.getNom());
    }


    public void joueurAManoeuvreEnFinDeTour(JoueurServer pJoueur, Territoire pTerritoireSource, Territoire pTerritoireCible, int pNbTroupes)
    {
        manoeuvreEnCours = new Manoeuvre();
        manoeuvreEnCours.setTerritoireSource(pTerritoireSource);
        manoeuvreEnCours.setTerritoireCible(pTerritoireCible);
        manoeuvreEnCours.setNbTroupes(pNbTroupes);
        manoeuvreEnCours.getTerritoireSource().ajouteDesTroupes(-pNbTroupes);
        manoeuvreEnCours.getTerritoireCible().ajouteDesTroupes(pNbTroupes);
        envoieMessageATous(ClientCommandes.JOUEUR_A_EFFECTUE_UNE_MANOEUVRE,pJoueur.getNom()+";"+manoeuvreEnCours.getTerritoireSource().getNom().name()+";"+manoeuvreEnCours.getTerritoireCible().getNom().name()+";"+manoeuvreEnCours.getNbTroupes());
        pJoueur.setNombreDeManoeuvreEnFinDeTour(pJoueur.getNombreDeManoeuvreEnFinDeTour()-1);
        if (pJoueur.getNombreDeManoeuvreEnFinDeTour()>0){
            joueurArreteLesInvasions(pJoueur);
        }
        else{
            atteignezUnObjectifEnFinDeTour();
        }

    }

    public void joueurPasseLaManoeuvreEnFinDeTour(JoueurServer pJoueur)
    {
        envoieMessageATous(ClientCommandes.JOUEUR_PASSE_LA_MANOEUVRE,pJoueur.getNom());
        atteignezUnObjectifEnFinDeTour();
    }


    private void atteignezUnObjectifEnFinDeTour()
    {
        this.setSousEtatPrincipal(SousEtat.ATTEIGNEZ_UN_OBJECTIF);
        envoieMessageATous(ClientCommandes.ATTEIGNEZ_UN_OBJECTIF_EN_FIN_DE_TOUR, joueurCourant.getNom());

    }


    public void joueurAAtteintUnObjectifEnFinDeTour(JoueurServer pJoueur, CarteObjectif pCarteObjectif)
    {
        if (pCarteObjectif.getJoueur()==pJoueur) //Le joueur a atteint un objectif, pn vérifie juste côté serveur qu'il avait bien cette carte objectif :)
        {
            //Checker objectif atteint ?? Un jour peut être...
            pJoueur.atteintUnObjectif(pCarteObjectif);

            //On vérifie si le joueur a gagné et on informe les autres
            verifierLaVictoireDunJoueur(pJoueur);

            //Ensuite, on pioche 2 objectifs, et l'utilsateur en choisi un.
            CarteObjectif carte1 = riskGOTCartesObjectifs.piocher(pJoueur);
            CarteObjectif carte2 = riskGOTCartesObjectifs.piocher(pJoueur);
            envoieMessageATous(ClientCommandes.JOUEUR_A_ATTEINT_UN_OBJECTIF_EN_FIN_DE_TOUR, pJoueur.getNom()+";"+ pCarteObjectif.getIdAsStr());
            envoieMessage(pJoueur, ClientCommandes.CHOISIR_UN_OBJECTIF, carte1.getIdAsStr()+";"+carte2.getIdAsStr());
        }
        else
        {
            //Impossible ?
            System.err.println("Erreur joueurAAtteintUnObjectifEnFinDeTour(), le joueur a atteint un objectif qui ne lui appartenait pas !!");
        }
    }

    public void verifierLaVictoireDunJoueur(Joueur pJoueur){

        if (pJoueur.estVictorieux()){
            envoieMessageATous(ClientCommandes.JOUEUR_EST_VICTORIEUX,pJoueur.getNom());
        }
    }

    public void joueurNatteintPasDobjectif(JoueurServer pJoueur)
    {
        envoieMessageATous(ClientCommandes.JOUEUR_NATTEINT_PAS_DOBJECTIF,pJoueur.getNom());
        piocherUneCarteTerritoireEnFinDeTour();

    }


    private void piocherUneCarteTerritoireEnFinDeTour(){
        this.setSousEtatPrincipal(SousEtat.TIREZ_UNE_CARTE_TERRITOIRE);

        if (joueurCourant.nbTerritoiresGagnesPendantLeTour()>0)
        {
            nbCartePiochables++;
            envoieMessageATous(ClientCommandes.JOUEUR_PEUT_PIOCHER_UNE_CARTE_TERRITOIRE, joueurCourant.getNom());
        }
        else {
            envoieMessageATous(ClientCommandes.JOUEUR_NE_PEUT_PAS_PIOCHER_UNE_CARTE_TERRITOIRE, joueurCourant.getNom());
        }
    }




    private int nbCartePiochables = 0;
    public void joueurAPiocherUneCarteEnFinDeTour()
    {
        CarteTerritoire carteTerritoire = riskGOTCartesTerritoires.piocher(joueurCourant);
        nbCartePiochables--;
        envoieMessageATous(ClientCommandes.JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE_EN_FIN_DE_TOUR, joueurCourant.getNom() + ";" + carteTerritoire.getTerritoire().getNom().name());
        if (nbCartePiochables>0){
            envoieMessageATous(ClientCommandes.JOUEUR_PEUT_PIOCHER_UNE_CARTE_TERRITOIRE, joueurCourant.getNom());
        }
    }

    public void joueurTermineSonTour()
    {
        envoieMessageATous(JOUEUR_TERMINE_SON_TOUR, joueurCourant.getNom());
        demarrerUnTour(false);
    }

    public void joueurAConvertiTroisCartesTerritoiresEnTroupesSupplementaires(JoueurServer pJoueur, CarteTerritoire pCarteTerritoire1, CarteTerritoire pCarteTerritoire2, CarteTerritoire pCarteTerritoire3){
        int bonusTroupes = this.riskGOTCartesTerritoires.getBonusCombinaisonDeTroisCartesTerritoires(pCarteTerritoire1,pCarteTerritoire2,pCarteTerritoire3);
        int bonusTroupeSupplementairesCarJoueurPossedeTerritoire = 0;

        //Calcul du bonus supplémentaire que le joueur reçoit s'il possède au moins un des territoires correspondant aux cartes converties.
        if (pCarteTerritoire1.getTerritoire().getAppartientAJoueur()==pJoueur||pCarteTerritoire2.getTerritoire().getAppartientAJoueur()==pJoueur||pCarteTerritoire3.getTerritoire().getAppartientAJoueur()==pJoueur){
            bonusTroupeSupplementairesCarJoueurPossedeTerritoire = 2;
        }
        if (joueurCourant==pJoueur) {//Normalement c'est bon !!
            if (bonusTroupes > 0) {//Normalement c'est toujours le cas
                joueurCourant.setNbTroupeAPlacer(joueurCourant.getNbTroupeAPlacer()+bonusTroupes+bonusTroupeSupplementairesCarJoueurPossedeTerritoire);
                joueurCourant.utiliseUneCarteTerritoire(pCarteTerritoire1);
                joueurCourant.utiliseUneCarteTerritoire(pCarteTerritoire2);
                joueurCourant.utiliseUneCarteTerritoire(pCarteTerritoire3);
                envoieMessageATous(ClientCommandes.JOUEUR_A_CONVERTI_TROIS_CARTES_TERRITOIRE_EN_TROUPES_SUPPLEMENTAIRES, joueurCourant.getNom()+";"+pCarteTerritoire1.getTerritoire().getNom().name()+";"+pCarteTerritoire2.getTerritoire().getNom().name()+";"+pCarteTerritoire3.getTerritoire().getNom().name()+";"+ bonusTroupes+";"+bonusTroupeSupplementairesCarJoueurPossedeTerritoire);
                convertissezVosCartesTerritoires();
            }
            else{
                System.err.println("Olala ya un bug (joueurAConvertiTroisCartesTerritoiresEnTroupesSupplementaires) - bonus troupes = 0");

            }
        }
        else {
            System.err.println("Olala ya un bug (joueurAConvertiTroisCartesTerritoiresEnTroupesSupplementaires) - le joueur n'est pas le joueur courant !!");

        }
    }

    public void joueurAConvertiUneCarteTerritoireEnUniteSpeciale(JoueurServer pJoueur, CarteTerritoire pCarteTerritoire) {
        if (joueurCourant == pJoueur) {//Normalement c'est bon !!
            joueurCourant.utiliseUneCarteTerritoire(pCarteTerritoire);
            setSousEtatRenforcez(SousEtatRenforcez.DEPLOYEZ_DES_UNITES_SPECIALES);
            envoieMessageATous(ClientCommandes.JOUEUR_A_CONVERTI_UNE_CARTE_TERRITOIRE_EN_UNITE_SPECIALE, joueurCourant.getNom() + ";" + pCarteTerritoire.getTerritoire().getNom().name());
            //convertissezVosCartesTerritoires();
        } else {
            System.err.println("Olala ya un bug (joueurAConvertiUneCarteTerritoireEnUniteSpeciale) - le joueur n'est pas le joueur courant !!");

        }
    }

    public void joueurPasseLaConversionDeCartesTerritoires(JoueurServer pJoueur){
        if (joueurCourant==pJoueur) {//Normalement c'est bon !!
            envoieMessageATous(ClientCommandes.JOUEUR_PASSE_LA_CONVERSION_DE_CARTES_TERRITOIRES, joueurCourant.getNom());
            setSousEtatRenforcez(SousEtatRenforcez.DEPLOYEZ_DES_TROUPES);
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.DEPLOYEZ_UNE_TROUPE, String.valueOf(joueurCourant.getNbTroupeAPlacer()));
        }
        else {
            System.err.println("Olala ya un bug (joueurPasseLaConversionDeCartesTerritoires) - le joueur n'est pas le joueur courant !!");

        }
    }


    public void joueurADeployeUneUniteSpeciale(JoueurServer pJoueur, Territoire territoire, CarteTerritoire.UniteSpeciale uniteSpeciale){

        territoire.getUniteSpeciales().add(uniteSpeciale);
        envoieMessageATous(ClientCommandes.JOUEUR_A_DEPLOYE_UNE_UNITE_SPECIALE, pJoueur.getNom() + ";" + territoire.getNom().name()+";"+ uniteSpeciale.name());
        convertissezVosCartesTerritoires();


    }


    //PERSONNAGES...


    public void joueurVeutJouerUneCartePersonnage(JoueurServer pJoueur, CartePersonnage pCartePersonnage)
    {
        if (pJoueur.getArgent()>=pCartePersonnage.getCout()){
            if (estUtilisable(pCartePersonnage)){
                pJoueur.setArgent(pJoueur.getArgent()-pCartePersonnage.getCout());
                envoieMessageATous(JOUEUR_VA_UTILISER_UNE_CARTE_PERSONNAGE, pJoueur.getNom()+";"+pCartePersonnage.getName().name());
                utiliserCartePersonnage(pCartePersonnage);
            }
            else{
                //Assez d'argent, mais pas utilisable
                envoieMessage(pJoueur, CARTE_PERSONNAGE_NON_UTILISABLE, pCartePersonnage.getName().name()+";"+pCartePersonnage.getRaisonPourLaquelLaCarteNePeutPasEtreUtilisee());
            }
        }
        else{
            //Pas assez d'argent
            envoieMessage(pJoueur, JOUEUR_NA_PAS_ASSEZ_DARGENT_POUR_JOUER_SA_CARTE_PERSONNAGE, pCartePersonnage.getName().name());
        }
    }


    public boolean estUtilisable(CartePersonnage cartePersonnage) {
        if (this.etatprincipal != Etat.TOUR_DE_JEU) { // Les cartes ne sont utilisables que pendant le tour de jeu (pas pendant le placement initial par exemple).
            cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Les cartes ne sont utilisables que pendant le tour de jeu (pas pendant le placement initial par exemple)");
            return false;
        }
        if (cartePersonnage.isUtilisee()) { // Une carte personnage déjà utilisée ne peut pas l'être une seconde fois
            cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Une carte personnage déjà utilisée ne peut pas l'être une seconde fois");
            return false;
        }
        if (cartePersonnage.getFamille().getJoueur()==this.joueurCourant){ // On vérifie que la carte peut être jouée pendant le tour du joueur qui la joue
            if (!cartePersonnage.isPeutEtreJoueePendantSonTour()){
                cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Cette carte ne peut pas être jouée pendant votre tour !");
                return false;
            }
        }
        if (cartePersonnage.getFamille().getJoueur()!=this.joueurCourant){ // On vérifie que la carte peut être jouée pendant le tour d'un autre joueur si celui qui la joue n'est pas le joueur actif
            if (!cartePersonnage.isPeutEtreJoueePendantLeTourDesAutres()){
                cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Cette carte ne peut pas être jouée pendant le tour d'un autre joueur!");
                return false;
            }
        }
        if (!(this.sousEtatPrincipal==cartePersonnage.getSousEtatAuquelCartePeutEtreJouee())) // On vérifie que le sousEtat corresponde au sous état auquel la carte peut être jouée
        {
            cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne jouez pas cette carte au bon moment du tour. Il faut la joueur pendant la phase " + cartePersonnage.getSousEtatAuquelCartePeutEtreJouee().name());
            return false;
        }


        switch (cartePersonnage.getName()) { // On vérifie le reste des conditions
            case CATELYN_STARK:
               if (cartePersonnage.getFamille().getJoueur()==invasionEnCours.getJoueurDefenseur())
               {
                   if (invasionEnCours.getTerritoireCible().getNombreDeTroupes()==1)
                   {
                       if (invasionEnCours.getNbBataillesTermineesDansLinvasion()==0) {
                           return true;
                       }
                       else {
                           cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez jouer cette carte qu'au début d'une invasion !");
                           return false;
                       }
                   }
                   else {
                       cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Votre territoire doit contenir une seule troupe pour pouvoir jouer cette carte");
                       return false;
                   }
               }
               else{
                   cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous devez être défenseur pour jouer cette carte");
                   return false;
               }
            case JON_SNOW:
                return true;
            case NED_STARK://On doit être défenseur et avoir lancé les dés
            case TYWIN_LANNISTER:
                if (cartePersonnage.getFamille().getJoueur()==invasionEnCours.getJoueurDefenseur()) {
                    if (sousEtatEnvahissez == SousEtatEnvahissez.BATAILLE_TERMINEE) {
                        return true;
                    } else {
                        cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte qu'après avoir lancé tous les dés pour la bataille");
                        return false;
                    }
                }
                else {
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous devez être défenseur pour jouer cette carte");
                    return false;
                }
            case ROBB_STARK: //Lorsqu'on lance une invasion.
            case LORAS_TYRELL:
            case OBERYN_MARTELL:
                if (sousEtatEnvahissez==SousEtatEnvahissez.CHOIX_NB_TROUPES &&invasionEnCours.getNbBataillesTermineesDansLinvasion()==0) {
                    return true;
                }
                else{
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte que quand vous lancez une invasion");
                    return false;
                }
            case DEVOS_MERVAULT:
                if (sousEtatEnvahissez==SousEtatEnvahissez.CHOIX_NB_TROUPES &&invasionEnCours.getNbBataillesTermineesDansLinvasion()==0&&invasionEnCours.getTerritoireCible().isPort()) {
                        return true;
                }
                else {
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte lorsque vous lancez une invasion contre un territoire doté d'un port");
                    return false;
                }
            case MELISANDRE://Avant de lancer une invasion
            case SLADHOR_SAAN:
            case ELLARIA_SAND:
                if (sousEtatEnvahissez==SousEtatEnvahissez.CHOIX_TERRITOIRES) {
                    return true;
                }
                else{
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte qu'avant de lancer un invasion");
                    return false;
                }
            case STANIS_BARATHEON:
            case BRIENNE:
                if (cartePersonnage.getFamille().getJoueur()==invasionEnCours.getJoueurDefenseur()&&invasionEnCours.getNbBataillesTermineesDansLinvasion()==0) {
                    if (sousEtatEnvahissez == SousEtatEnvahissez.CHOIX_NB_TROUPES) {
                        return true;
                    } else {
                        cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte que quand un adversaire vient de lancer une invasion contre vous");
                        return false;
                    }
                }
                else {
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous devez subir une invasion pour jouer cette carte");
                    return false;
                }
            case CERSEI_LANNISTER:
                if (joueurCourant.nbTerritoiresGagnesPendantLeTour()>2){
                    return true;
                }
                else{
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous devez avoir gagné au moins 3 territoires pendant ce tour");
                    return false;
                }
            case JAIME_LANNISTER:
                if (sousEtatEnvahissez == SousEtatEnvahissez.BATAILLE_TERMINEE) {
                    return true;
                } else {
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte qu'après avoir lancé tous les dés pour la bataille");
                    return false;
                }
            case TYRION_LANNISTER:
                if (joueurCourant.getArgent()>=300){
                    return true;
                }
                else {
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous pourriez  joueur Tyrion, mais vous n'auriez pas assez d'argent pour acheter un mestre ... !");
                    return false;
                }
            case MARGAERY_TYRELL:
                if (invasionEnCours.getJoueurDefenseur()==joueurCourant||invasionEnCours.getJoueurAttaquant()==joueurCourant) {
                    if (sousEtatEnvahissez == SousEtatEnvahissez.BATAILLE_TERMINEE) {
                        return true;
                    } else {
                        cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte qu'après avoir lancé tous les dés pour la bataille");
                        return false;
                    }
                }
                else{
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez pas joueur cette carte sans être impliqué dans l'invasion ! Vous êtes spectateur !");
                    return false;
                }
            case RENLY_BARATHEON:
                if (sousEtatRenforcez == SousEtatRenforcez.DEPLOYEZ_DES_TROUPES) {
                return true;
                }
                else{
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez jouer cette carte qu'au moment de déployer des renforts");
                    return false;
                }
            case AREO_HOTAH:
                if (cartePersonnage.getFamille().getJoueur()==invasionEnCours.getJoueurDefenseur()&&invasionEnCours.getNbBataillesTermineesDansLinvasion()==0) {
                    if (sousEtatEnvahissez == SousEtatEnvahissez.CHOIX_NB_TROUPES) {
                        if (invasionEnCours.getTerritoireCible().isPort()||invasionEnCours.getTerritoireCible().isChateau()) {
                            return true;
                        }
                        else {
                            cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Le territoire attaqué doit avoir un port ou un chateau ! Or là il n'a ni port ni chateau...");
                            return false;
                        }
                    } else {
                        cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous ne pouvez joueur cette carte que quand un adversaire vient de lancer une invasion contre vous");
                        return false;
                    }
                }
                else {
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous devez subir une invasion pour jouer cette carte");
                    return false;
                }
            case DORAN_MARTELL:
                if (joueurCourant.nbTerritoiresGagnesPendantLeTour()==0){
                    return true;
                }
                else{
                    cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Vous devez n'avoir gagné aucun territoires pendant ce tour");
                    return false;
                }
            default:
                cartePersonnage.setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee("Désolé, cette carte n'est pas encore implémentée, vous venez de perdre " + cartePersonnage.getCout() + " ! C'est pas de bol :)");
                break;

        }
        return false;
    }


    private void utiliserCartePersonnage(CartePersonnage pCartePersonnage) {
        pCartePersonnage.setUtilisee(true);
        switch (pCartePersonnage.getName()) {
            case CATELYN_STARK:
                invasionEnCours.getJoueurDefenseur().setNbTroupeAPlacer(invasionEnCours.getJoueurDefenseur().getNbTroupeAPlacer()+1);
                joueurARenforceUnTerritoire((JoueurServer)invasionEnCours.getJoueurDefenseur(),invasionEnCours.getTerritoireCible());
                break;
            case JON_SNOW:
                this.joueurCourant.setNombreDeManoeuvreEnFinDeTour(joueurCourant.getNombreDeManoeuvreEnFinDeTour()+1);
                break;
            case NED_STARK:
                envoieMessageATous(JOUEUR_PEUT_METTRE_UN_DES_DES_A_SA_VALEUR_MAXIMALE, invasionEnCours.getJoueurDefenseur().getNom());
                break;
            case ROBB_STARK:
            case DEVOS_MERVAULT:
                invasionEnCours.ajouterUnBonusALattaquant(1);
                break;
            case MELISANDRE:
                //TODO quand les mestres seront implémentés.
                break;
            case SLADHOR_SAAN:
                this.volDargentEnCours=100;
                break;
            case STANIS_BARATHEON:
                invasionEnCours.ajouterUnBonusAuDefenseur(1);
                break;
            case DORAN_MARTELL:
            case CERSEI_LANNISTER:
                nbCartePiochables++;
                envoieMessageATous(ClientCommandes.JOUEUR_PEUT_PIOCHER_UNE_CARTE_TERRITOIRE, joueurCourant.getNom());
                break;
            case JAIME_LANNISTER:
                envoieMessageATous(JOUEUR_PEUT_METTRE_UN_DES_DES_A_SA_VALEUR_MAXIMALE, invasionEnCours.getJoueurAttaquant().getNom());
                break;
            case TYRION_LANNISTER:
                //TODO Quand carte mestres sont implémentées
                break;
            case TYWIN_LANNISTER:
                invasionEnCours.ajouterUnBonusAuDefenseur(1);
                String message = "";
                for (DeTypeValeur deTypeValeur: invasionEnCours.getResultatsDesDefenseur()) {
                    message = message + deTypeValeur.getTypeDe().name() + ","+ deTypeValeur.getValeur() + ";";
                }
                joueurALanceLesDesEnDefense((JoueurServer)invasionEnCours.getJoueurDefenseur(), message.substring(0, message.length() - 1));
                break;
            case BRIENNE:
                invasionEnCours.ajouterDesDesHuitFacesAuDefenseur(2);
                envoieMessageATous(JOUEUR_REMPACE_DES_DES_SIX_PAR_DES_DES_HUIT_PENDANT_LINVASION,invasionEnCours.getJoueurDefenseur().getNom()+";"+2);
                break;
            case LORAS_TYRELL:
                invasionEnCours.setAttaquantGagneLesEgalites(true);
                envoieMessageATous(ATTAQUANT_GAGNE_LES_EGALITES_PENDANT_LINVASION,"");
            case MARGAERY_TYRELL:
                //TODO envoieMessageATous(FAIRE_RELANCER_UN_JOUEUR_UN_DE_CES_DES, invasionEnCours.getJoueurAttaquant().getNom());
                break;
            case RENLY_BARATHEON:
                //TODO
                break;
            case AREO_HOTAH:
                //TODO
                break;
            case ELLARIA_SAND:
                invasionEnCours.setManoeuvrerSansContrainte(true);
                break;
            case OBERYN_MARTELL:
                invasionEnCours.ajouterDesDesHuitFacesAuAttaquant(1);
                envoieMessageATous(JOUEUR_REMPACE_DES_DES_SIX_PAR_DES_DES_HUIT_PENDANT_LINVASION,invasionEnCours.getJoueurAttaquant().getNom()+";"+1);
                break;
            default:
                break;
        }

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

    public void prochainJoueur(boolean pDemarrage) {
        if (pDemarrage) {
            joueurCourant=riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Stark).getJoueur();
        }
        else {
            Famille familleSuivante = riskGOTFamilles.getFamilleSuivante(joueurCourant.getFamille());
            joueurCourant = familleSuivante.getJoueur();
        }

        joueurCourant.demarreSonTour();

        envoieMessageATous(ClientCommandes.JOUEUR_ACTIF, joueurCourant.getNom());
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

    //**************************************
    //DEBUG AREA
    //**************************************
    public void initChoixTerritoire4Debug() {
        if (etatprincipal != Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE) {
            System.out.println("Le serveur n'est pas dans un état permettant l'initialisation");
        } else {
            prochainJoueur(true);
            while (riskGOTterritoires.isEncoreAuMoinsUnTerritoireLibre()) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Territoire ter = riskGOTterritoires.getTerritoiresNonAttribues().get((int) (this.riskGOTterritoires.getTerritoiresNonAttribues().size() * Math.random()));
                ter.setAppartientAJoueur(joueurCourant);
                ter.ajouteDesTroupesAPlacer(1);
                envoieMessageATous(ClientCommandes.JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE, joueurCourant.getNom() + ";" + ter.getNom().name());
                prochainJoueur(false);
            }
            envoieMessageATous(ClientCommandes.CHOIX_TERRITOIRES_DEMARRAGE_TERMINE, "");
            //demarrePlacementDesTroupesDemarrage();
        }

    }


    public void initPlacementDesTroupes4Debug() {
        if (etatprincipal != Etat.PLACER_LES_TROUPES_DEMARRAGE) {
            System.out.println("Le serveur n'est pas dans un état permettant l'initialisation");
        } else {
            prochainJoueur(true);
            boolean cpasfini = true;
            while (cpasfini) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cpasfini = false;
                if (joueurCourant.getNbTroupeAPlacer() > 0) {
                    cpasfini = true;
                    Territoire ter = joueurCourant.territoires.get((int) (joueurCourant.territoires.size() * Math.random()));
                    ter.ajouteDesTroupesAPlacer(1);
                    envoieMessageATous(ClientCommandes.JOUEUR_A_RENFORCE_UN_TERRITOIRE, joueurCourant.getNom() + ";" + ter.getNom().name());
                    prochainJoueur(false);
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
            if (famille==riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Martell)||famille==riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Tyrell)) {
                CarteTerritoire carteTerritoire = riskGOTCartesTerritoires.piocher(j);
                envoieMessageATous(ClientCommandes.JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE, j.getNom()+";"+carteTerritoire.getTerritoire().getNom().name());
            }
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            famille = riskGOTFamilles.getFamilleSuivante(famille);
        }
        joueurCourant = this.riskGOTFamilles.getFamilleParNom(Famille.FamilyNames.Stark).getJoueur();
        int nbTroupesRestantAPlacer = riskGOTFamilles.initCapitales(nbJoueurs);
        envoieMessageATous(ClientCommandes.CHOIX_FAMILLE_TERMINE,"");
        initChoixObjectifsDemarrage4Debug();
        setEtatprincipal(Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE);
        envoieMessageATous(ClientCommandes.CHOIX_CARTES_OBJECTIFS_DEMARRAGE_TERMINE,((Integer) nbTroupesRestantAPlacer).toString());
        initChoixTerritoire4Debug();
        setEtatprincipal(Etat.PLACER_LES_TROUPES_DEMARRAGE);
        initPlacementDesTroupes4Debug();
        demarrerUnTour(true);

    }

    private void initChoixObjectifsDemarrage4Debug()
    {
        for (Joueur j : joueurServers)
        {
            CarteObjectif carte1 = riskGOTCartesObjectifs.piocher(j);
            CarteObjectif carte2 = riskGOTCartesObjectifs.piocher(j);
            CarteObjectif carte3 = riskGOTCartesObjectifs.piocher(j);
            j.jetteUneCarteObjectif(carte3);
            envoieMessageATous(ClientCommandes.JOUEUR_A_CHOISI_SES_OBJECTIFS_DEMARRAGE,j.getNom()+";"+carte1.getIdAsStr()+",Y"+";"+carte2.getIdAsStr()+",Y"+";"+carte3.getIdAsStr()+",N");
        }
    }

    public void initPlacerDesUnitesSpecialesSurLesTerritoires()
    {
        for (Territoire ter : this.riskGOTterritoires.getTerritoires())
        {
            Integer val = (int) (1 + 3 * Math.random());
            for (int i=1;i<val;i++){
                Integer val2 = (int) (1 + 3 * Math.random());
                CarteTerritoire.UniteSpeciale uniteSpeciale = null;
                if (val2==1){
                    uniteSpeciale= CarteTerritoire.UniteSpeciale.CHEVALIER;
                }
                if (val2==2){
                    uniteSpeciale= CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE;
                }
                if (val2==3){
                    uniteSpeciale= CarteTerritoire.UniteSpeciale.FORTIFICATION;
                }
                ter.getUniteSpeciales().add(uniteSpeciale);
                envoieMessageATous(ClientCommandes.JOUEUR_A_DEPLOYE_UNE_UNITE_SPECIALE, ter.getAppartientAJoueur().getNom() + ";" + ter.getNom().name()+";"+ uniteSpeciale.name());

            }
        }

    }


    public void initDistribuer4CartesTerritoiresAuHasardAuxJoueurs()
    {
        for (JoueurServer j:joueurServers)
        {
            for (int i=0;i<4;i++)
            {
                CarteTerritoire carteTerritoire = riskGOTCartesTerritoires.piocher(j);
                envoieMessageATous(ClientCommandes.JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE, j.getNom() + ";" + carteTerritoire.getTerritoire().getNom().name());
            }
        }

    }



}

