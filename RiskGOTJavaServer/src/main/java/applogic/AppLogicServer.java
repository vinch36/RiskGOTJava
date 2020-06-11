package applogic;

import common.objects.cartes.CarteObjectif;
import common.objects.cartes.CarteTerritoire;
import common.objects.cartes.CartesObjectifs;
import common.objects.cartes.CartesTerritoires;
import common.util.Etat;
import common.ClientCommandes;
import common.objects.*;
import common.util.SousEtat;
import network.JoueurServer;
import network.ServerListener;

import java.util.*;


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
            envoieMessage((JoueurServer) joueurCourant, ClientCommandes.CONVERTISSEZ_VOS_CARTES_TERRITOIRES_EN_TROUPES_OU_UNITE_SPECIALES, "");
        }
        else {
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
            //Ici faire les cartes mestres et objectif !
            envahir();


        }

    }

    private void envahir(){

        this.setSousEtatPrincipal(SousEtat.ENVAHISSEZ);
        envoieMessageATous(ClientCommandes.TOUR_1_ENVAHISSEZ, joueurCourant.getNom());
        envoieMessage((JoueurServer)joueurCourant, ClientCommandes.LANCER_INVASION,"");

    }


    public void joueurALanceUneInvasion(JoueurServer pJoueurServer, Territoire pTerritoireSource, Territoire pTerritoireCible)
    {
        invasionEnCours = new Invasion();
        invasionEnCours.setTerritoireSource(pTerritoireSource);
        invasionEnCours.setTerritoireCible(pTerritoireCible);
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
            for (String s: message.split(";"))
            {
                invasionEnCours.getResultatsDesAttaquant().add(new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), Integer.parseInt(s.split(",")[2])));
            }
            invasionEnCours.setJoueurSourceALanceLesDes(true);
            envoieMessageATous(ClientCommandes.JOUEUR_A_LANCE_LES_DES_EN_ATTAQUE,pJoueur.getNom()+";"+ message);
            if (invasionEnCours.resoudreLaBatailleEnCours()) {
                envoieMessageATous(ClientCommandes.LA_BATAILLE_EST_TERMINEE, "");
                preparerLaProchaineBataille();
            }
        } else {
            System.err.println("Olala ya un bug (joueurALanceLesDesEnAttaque)");
        }

    }


    public void joueurALanceLesDesEnDefense(JoueurServer pJoueur, String message)
    {
        if (invasionEnCours.getTerritoireCible().getAppartientAJoueur() == pJoueur){
            for (String s: message.split(";"))
            {
                invasionEnCours.getResultatsDesDefenseur().add(new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), Integer.parseInt(s.split(",")[2])));
            }
            invasionEnCours.setJoueurCibleALanceLesDes(true);
            envoieMessageATous(ClientCommandes.JOUEUR_A_LANCE_LES_DES_EN_DEFENSE,pJoueur.getNom()+";"+ message);
            if (invasionEnCours.resoudreLaBatailleEnCours()) {

                envoieMessageATous(ClientCommandes.LA_BATAILLE_EST_TERMINEE, invasionEnCours.getNbTroupesPerduesEnAttaque()+";"+invasionEnCours.getNbTroupesPerduesEnDefense());
                preparerLaProchaineBataille();
            }
        } else
        {
            System.err.println("Olala ya un bug (joueurALanceLesDesEnDefense)");
        }
    }

    private void preparerLaProchaineBataille()
    {
        String message = invasionEnCours.getTerritoireSource().getNom().name()+";"+invasionEnCours.getTerritoireSource().getNombreDeTroupes()+";"+invasionEnCours.getTerritoireCible().getNom().name()+";"+invasionEnCours.getTerritoireCible().getNombreDeTroupes();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (invasionEnCours.getTerritoireCible().getNombreDeTroupes()==0){
            //Dans ce cas, le joueur défenseur a perdu le territoire - l'invasion se termine donc. Il faut gérer le changement de territoire, et la répartition nouvelle des troupes (manoeuvre).

            invasionEnCours.victoireAttaquant();
            envoieMessageATous(ClientCommandes.INVASION_TERMINEE_DEFAITE_DEFENSEUR,message);

        }
        else if (invasionEnCours.getTerritoireSource().getNombreDeTroupes()<2){
            //Dans ce cas, le joueur attaquant n'a plus assez de troupe pour attaquer. Il faut arrêter l'invasion là.
            invasionEnCours.victoireDefenseur();
            envoieMessageATous(ClientCommandes.INVASION_TERMINEE_DEFAITE_ATTAQUANT, message);
            //envoieMessage((JoueurServer)joueurCourant, ClientCommandes.LANCER_INVASION,"");
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
            envoieMessage((JoueurServer)joueurCourant, ClientCommandes.LANCER_INVASION,"");
        } else {
            System.err.println("Olala ya un bug (joueurAManoeuvrerEnFinDinvasion)");

        }
    }


    public void joueurContinueInvasion()
    {
        Joueur joueur = invasionEnCours.getJoueurAttaquant();
        invasionEnCours.setNbBataille(invasionEnCours.getNbBataille()+1);
        invasionEnCours.resetPourProchaineBataille();
        String message = joueur.getNom()+";"+invasionEnCours.getTerritoireSource().getNom().name()+";"+invasionEnCours.getTerritoireCible().getNom().name() + ";"+invasionEnCours.getNbBataille();
        envoieMessageATous(ClientCommandes.JOUEUR_CONTINUE_INVASION,message);


    }

    public void joueurArreteInvasion()
    {
        envoieMessageATous(ClientCommandes.JOUEUR_ARRETE_UNE_INVASION,joueurCourant.getNom());
        envoieMessage((JoueurServer)joueurCourant, ClientCommandes.LANCER_INVASION,"");
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
        piocherUneCarteEnFinDeTour();
        demarrerUnTour(false);

    }

    public void joueurPasseLaManoeuvreEnFinDeTour(JoueurServer pJoueur)
    {
        envoieMessageATous(ClientCommandes.JOUEUR_PASSE_LA_MANOEUVRE,pJoueur.getNom());
        piocherUneCarteEnFinDeTour();
        demarrerUnTour(false);
    }

    private void piocherUneCarteEnFinDeTour(){
        if (joueurCourant.nbTerritoiresGagnesPendantLeTour()>0)
        {
            CarteTerritoire carteTerritoire = riskGOTCartesTerritoires.piocher(joueurCourant);
            envoieMessageATous(ClientCommandes.JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE, joueurCourant.getNom()+";"+carteTerritoire.getTerritoire().getNom().name());
        }
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
            envoieMessageATous(ClientCommandes.JOUEUR_A_CONVERTI_UNE_CARTE_TERRITOIRE_EN_UNITE_SPECIALE, joueurCourant.getNom() + ";" + pCarteTerritoire.getTerritoire().getNom().name());
            //convertissezVosCartesTerritoires();
        } else {
            System.err.println("Olala ya un bug (joueurAConvertiUneCarteTerritoireEnUniteSpeciale) - le joueur n'est pas le joueur courant !!");

        }
    }

    public void joueurPasseLaConversionDeCartesTerritoires(JoueurServer pJoueur){
        if (joueurCourant==pJoueur) {//Normalement c'est bon !!
            envoieMessageATous(ClientCommandes.JOUEUR_PASSE_LA_CONVERSION_DE_CARTES_TERRITOIRES, joueurCourant.getNom());
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

