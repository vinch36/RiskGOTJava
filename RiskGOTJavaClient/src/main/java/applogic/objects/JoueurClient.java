package applogic.objects;
import common.ClientCommandes;
import common.objects.*;
import common.objects.cartes.*;
import common.util.Etat;
import common.util.SousEtat;
import common.util.SousEtatRenforcez;
import gui.MainView;
import javafx.application.Platform;

import java.util.ArrayList;

public class JoueurClient extends Joueur {

    private ArrayList<JoueurClient> adversaires;

    public Invasion getInvasionEnCours() {
        return invasionEnCours;
    }

    private Invasion invasionEnCours;

    public Manoeuvre getManoeuvreEnCours() {
        return manoeuvreEnCours;
    }

    private Manoeuvre manoeuvreEnCours;

    public Familles getRiskGOTFamilles() {
        return riskGOTFamilles;
    }

    private Familles riskGOTFamilles;
    private Regions riskGOTregions;

    public Territoires getRiskGOTterritoires() {
        return riskGOTterritoires;
    }

    private Territoires riskGOTterritoires;

    public CartesTerritoires getRiskGOTCarteTerritoires() {
        return riskGOTCartesTerritoires;
    }

    private CartesTerritoires riskGOTCartesTerritoires;


    public CartesObjectifs getRiskGOTCartesObjectifs() {
        return riskGOTCartesObjectifs;
    }

    private CartesObjectifs riskGOTCartesObjectifs;
    protected int nbJoueurs;

    public boolean isMe() {
        return isMe;
    }

    private boolean isMe;

    public int getNbJoueursAttendus() {
        return nbJoueursAttendus;
    }

    public void setNbJoueursAttendus(int nbJoueursAttendus) {
        this.nbJoueursAttendus = nbJoueursAttendus;
    }

    protected int nbJoueursAttendus;


    protected boolean tousConnectes;

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public MainView getMainView() {
        return mainView;
    }

    private MainView mainView;

    public JoueurClient() {
        this.adversaires = new ArrayList<>();
        nom = "INCONNU";
        this.nbJoueurs = 1;
        isMe = true;

    }

    public JoueurClient(String pNom) {
        nom = pNom;
        isMe = false;
    }


    public ArrayList<JoueurClient> getAdversaires() {
        return adversaires;
    }

    public void setAdversaires(ArrayList<JoueurClient> adversaires) {
        this.adversaires = adversaires;
    }


    protected void commandeChat(String pCommande) {
        String nomJoueur = pCommande.split(";")[0];
        String message = pCommande.substring(nomJoueur.length() + 1);
        updateChatText(new ChatMessage(message, ChatMessage.ChatMessageType.CHAT, getJoueur(nomJoueur)));

    }

    protected void updateChatText(ChatMessage chatMessage) {
        Platform.runLater(() -> {
            this.mainView.updateChatZone(chatMessage);
        });
    }


    protected void commandeWelcome(String pCommande) {
        nbJoueursAttendus = Integer.parseInt(pCommande);
        riskGOTFamilles = new Familles(nbJoueursAttendus);
        riskGOTregions = new Regions();
        riskGOTterritoires = new Territoires(riskGOTregions, riskGOTFamilles);
        riskGOTCartesTerritoires = new CartesTerritoires(riskGOTterritoires);
        riskGOTCartesObjectifs = new CartesObjectifs();
        ChatMessage chatMessage1 = new ChatMessage("BIENVENUE [" + this.nom + "] SUR RISK GOT ONLINE !! LA PARTIE VA SE JOUER A [" + nbJoueursAttendus + "] JOUEURS", ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        ChatMessage chatMessage2 = new ChatMessage("EN ATTENTE DES AUTRES JOUEURS ...", ChatMessage.ChatMessageType.INFO);

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage1);
            mainView.updateChatZone(chatMessage2);
            mainView.createComponentsFamilles(riskGOTFamilles);
            mainView.createComponentsZoneListeJoueurs(nbJoueursAttendus);
        });

    }

    protected void commandeNouvelleConnection(String pCommande) {
        String joueurName;
        joueurName = pCommande;
        if (!joueurName.equals(this.nom)) {
            JoueurClient joueurClient = new JoueurClient(joueurName);
            this.getAdversaires().add(joueurClient);
            nbJoueurs++;
            Platform.runLater(() -> mainView.nouveauJoueurConnecte(joueurClient));
        }
    }

    protected void commandeTousConnecte(String pCommande) {
        if (pCommande.equals("DEBUG")) { // CAS DEBUG - ON SIMULE LENVOIE DU DE
            Integer de = (int) (1 + 6 * Math.random());
            mainView.getClientConnexion().sendCommand(ClientCommandes.LANCE_1DE_START, de.toString());
        } else { // CAS NORMAL - LE JOUEUR DOIT CLIQUER POUR LANCER LE DE
            ChatMessage message = new ChatMessage("Veuillez lancer un dés pour savoir qui pourra choisir en premier la première Famille.\nLe plus grand dé pourra choisir sa famille en premier.\nEn cas d'éaglité, l'heure de connection prime (le premier connecté aura la priorité !)", ChatMessage.ChatMessageType.ACTION);
            Platform.runLater(() -> {
                mainView.updateChatZone(message);
                mainView.PretALancerUnDes();
            });
        }
    }

    protected void commandeResultat1DE(String pCommande) {
        int resultat = Integer.valueOf(pCommande.split(";")[1]);
        String nomJoueur = pCommande.split(";")[0];
        String connectTime = pCommande.split(";")[2];
        ChatMessage message = new ChatMessage("Le joueur " + nomJoueur + " a fait " + resultat + " au lancé de dé\nSon heure de connection était " + connectTime, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.mettreAJourUnResultatDeDeDemarrage(getJoueur(nomJoueur), resultat);
            mainView.updateChatZone(message);
        });
    }

    protected void commandeJoueurActif(String message) {
        JoueurClient joueurClient = this.getJoueur(message);
        Platform.runLater(() -> {
            mainView.updateChatZone(new ChatMessage(" commence son tour !", ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, joueurClient));
            mainView.refreshJoueurActif(joueurClient);
        });
    }

    protected void commandeFaireChoixFamille(String pCommande) {
        //le message a la forme "Fam1;Fam2;Fam3)
        Platform.runLater(() -> mainView.faireChoixFamille(pCommande));
    }

    protected void commandeMiseAJourFamilleJoueur(String pCommande) {
        //le message a la forme "NOM_JOUEUR;NOM_FAMILLE)
        String nomJoueur = pCommande.split(";")[0];
        String nomFamille = pCommande.split(";")[1];

        JoueurClient joueur = this.getJoueur(nomJoueur);
        Famille famille = riskGOTFamilles.getFamilleParNomString(nomFamille);
        joueur.setFamille(famille);
        JoueurClient finalJoueur = joueur;
        ChatMessage message = new ChatMessage("Le joueur " + nomJoueur + " a choisi la famille " + nomFamille + "\nSa capitale est : " + famille.getCapitale().getNom().name(), ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(message);
            mainView.refreshFamilleJoueur(famille, finalJoueur);
            if (joueur==this){
                mainView.afficherLesPersonnages(famille);
            }
        });

    }



    protected void commandeChoixFamilleTerminee(String pCommande) {
        int nbTroupesAPlacer = riskGOTFamilles.initCapitales(nbJoueurs);
        Platform.runLater(() -> {
            for (Famille f : riskGOTFamilles.getFamillesActives()) {
                this.mainView.mettreAJourUnTerritoireSurLaCarte(f.getCapitale(), true, false);
            }
            mainView.rafraichirLesZonesFamilles();
            mainView.setEtatPrincipal(Etat.CHOISIR_LES_CARTES_OBJECTIFS_DEMARRAGE);
        });


    }

    private JoueurClient getAversaireParNom(String pNom) {
        for (JoueurClient j : adversaires) {
            if (pNom.equals(j.nom)) {
                return j;
            }

        }
        return null;
    }

    private JoueurClient getJoueur(String pNom) {
        JoueurClient joueur = getAversaireParNom(pNom);
        if (joueur == null) {
            joueur = this;
        }
        return joueur;
    }

    protected void commandeChoisirLesCartesObjectifDemarrage(String pCommand)
    {
        //message: carte1.getIdAsStr()+";"+carte2.getIdAsStr()+";"+carte3.getIdAsStr()
        CarteObjectif carteObjectif1 = riskGOTCartesObjectifs.getCarteObjectifParIdStr(pCommand.split(";")[0]);
        CarteObjectif carteObjectif2 = riskGOTCartesObjectifs.getCarteObjectifParIdStr(pCommand.split(";")[1]);
        CarteObjectif carteObjectif3 = riskGOTCartesObjectifs.getCarteObjectifParIdStr(pCommand.split(";")[2]);
        ChatMessage chatMessage = new ChatMessage("Vous devez maintenant choisir 2 objectifs parmis les 3 proposés", ChatMessage.ChatMessageType.ACTION);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.faireChoixObjectifDemarrage(carteObjectif1,carteObjectif2,carteObjectif3);
        });


    }

    protected void commandeJoueurAChoisiLesCartesObjectifsDemarrage(String pCommand)
    {
        //message: joueurname;carte1ID,Y/N;carte2ID,Y/N;carte3ID,Y/N

        Joueur joueur = getJoueur(pCommand.split(";")[0]);
        pCommand=pCommand.substring(joueur.getNom().length()+1);
        for (String str : pCommand.split(";")){
            if (str.split(",")[1].equals("Y")){
                joueur.aPiocheUneCarteObjectif(riskGOTCartesObjectifs.getCarteObjectifParIdStr(str.split(",")[0]));
            }
        }
        ChatMessage chatMessage = new ChatMessage(" a choisi ses objectifs de démarrage", ChatMessage.ChatMessageType.INFOCHAT, joueur);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueur == this) {
                for (CarteObjectif carteObjectif:joueur.getCartesObjectif())
                {
                    mainView.ajouterUneCarteObjectif(carteObjectif);
                }
                mainView.rafraichirLesZonesFamilles();
            }
        });
    }



    protected void commandeChoixCartesObjectifDemarrageTermine(String pCommand)
    {
        ChatMessage chatMessage = new ChatMessage("Tous les joueurs ont choisi leurs 2 cartes objectifs, on va pouvoir commencer le choix des territoires", ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        Platform.runLater(() -> {
            mainView.setEtatPrincipal(Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE);
            mainView.updateChatZone(chatMessage);
        });


    }

    protected void commandeChoisirUnTerritoireDemarrage(String pCommand) {
        ArrayList<Territoire> territoiresLibres = new ArrayList<>();
        int nbTroupesAPlacer = Integer.parseInt(pCommand.split(";")[0]);
        if (nbTroupesAPlacer > 0 && pCommand.split(";").length > 0) {
            for (int i = 1; i < pCommand.split(";").length; i++) {
                Territoire ter = riskGOTterritoires.getTerritoireParNomStr(pCommand.split(";")[i]);
                territoiresLibres.add(ter);
            }

        }

        Platform.runLater(() -> mainView.choisirUnTerritoireDemarrage(territoiresLibres));

    }


    protected void commandeJoueurAChoisiUnTerritoireDemarrage(String pCommand) {
        //Message type : NOM_JOUEUR;FORT_TERREUR
        JoueurClient joueur = this.getJoueur(pCommand.split(";")[0]);
        Territoire territoire = this.getRiskGOTterritoires().getTerritoireParNomStr(pCommand.split(";")[1]);
        territoire.setAppartientAJoueur(joueur);
        territoire.ajouteDesTroupesAPlacer(1);
        ChatMessage chatMessage = new ChatMessage("a choisi :" + territoire.getNom(), ChatMessage.ChatMessageType.INFOCHAT, joueur);

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.mettreAJourUnTerritoireSurLaCarte(territoire, true, true);

        });
    }


    protected void commandeChoixTerritoireTermine(String message) {
        ChatMessage chatMessage = new ChatMessage("LE PLACEMENT INITIAL DES TERRITOIRES EST TERMINE.\nNOUS ALLONS MAINTENANT RENFORCER NOS TERRITOIRES A TOUR DE ROLE !", ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        mainView.setEtatPrincipal(Etat.PLACER_LES_TROUPES_DEMARRAGE);
        Platform.runLater(() -> {
            mainView.rafraichirTousLesTerritoiresEnEvidence();
            mainView.updateChatZone(chatMessage);
        });
    }

    protected void commandeDeployez(String pCommand) {
        int nbTroupesRestantAPlacer = Integer.parseInt(pCommand);

        Platform.runLater(() -> {
            mainView.deployez(nbTroupesRestantAPlacer);

        });

    }

    protected void commandeJoueurARenforceUnTerritoire(String pCommand) {
        //Message type : NOM_JOUEUR;FORT_TERREUR
        Territoire territoire = this.getRiskGOTterritoires().getTerritoireParNomStr(pCommand.split(";")[1]);
        ChatMessage chatMessage = new ChatMessage("a ajouté une troupe sur :" + territoire.getNom(), ChatMessage.ChatMessageType.INFOCHAT, territoire.getAppartientAJoueur());

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (mainView.getSousEtat()!=SousEtat.ENVAHISSEZ) {
                territoire.ajouteDesTroupesAPlacer(1);
                mainView.mettreAJourUnTerritoireSurLaCarte(territoire, true, true);
            }
            else{ // on est dans le cas où on renforce pendant une invasion
                territoire.ajouteDesTroupes(1); // On ajoute une troupe mais pas une que l'on attendait en renfort
                mainView.mettreAJourUnTerritoirePendantUneInvasion(territoire);
            }

        });
    }


    protected void commandePlacementTermine(String pCommande) {
        ChatMessage chatMessage = new ChatMessage("Le placement des troupes est terminé ! Le premier tour de jeu peut commencer !", ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        mainView.setEtatPrincipal(Etat.TOUR_DE_JEU);
        mainView.setSousEtat(SousEtat.RENFORCEZ);
        mainView.setSousEtatRenforcez(SousEtatRenforcez.ECHANGEZ_DES_CARTES_TERRITOIRE);
        Platform.runLater(() -> {
            mainView.rafraichirTousLesTerritoiresEnEvidence();
            mainView.updateChatZone(chatMessage);
        });


    }


    protected void commandeRenforcez(String pCommande) {
        //Format: NOM_JOUEUR;RENFORTS;BONUS_RENFORTS;ARGENT  joueurCourant.getNom() +";"+renforts+";"+bonus+";"+nbPorts+";"+joueurCourant.getArgent()
        JoueurClient joueur = this.getJoueur(pCommande.split(";")[0]);
        int renfort = Integer.parseInt(pCommande.split(";")[1]);
        int bonus = Integer.parseInt(pCommande.split(";")[2]);
        int ports = Integer.parseInt(pCommande.split(";")[3]);
        int argent = Integer.parseInt(pCommande.split(";")[4]);
        this.controleCoherence(joueur, renfort, bonus, ports);
        joueur.setNbTroupeAPlacer(renfort + bonus);
        joueur.setArgent(argent);
        Platform.runLater(() -> mainView.renfortRecu(joueur, renfort, bonus, ports, argent));
    }


    private void controleCoherence(JoueurClient joueur, int pRenfort, int pBonus, int pPorts) {
        boolean toutOK = true;
        //1.RENFORCEZ
        //1.1 CALCULER LES RENFORTS
        int renforts = joueur.calculerNombreDeRenfortsDeBase();
        //1.2 CALCULER LES BONUS REGIONS
        int bonus = joueur.calculerBonusRegion();
        int nbPorts = joueur.getNombreDePorts();

        if (renforts != pRenfort) {
            System.out.println("Contôle de cohérence FAUX sur RENFORTS");
            toutOK = false;
        }
        if (bonus != pBonus) {
            System.out.println("Contôle de cohérence FAUX sur BONUS");
            toutOK = false;
        }
        if (nbPorts != pPorts) {
            System.out.println("Contôle de cohérence FAUX sur PORTS");
            toutOK = false;
        }

        if (toutOK) {
            System.out.println("Contôle de cohérence OK :)");
        }
    }


    protected void commandeAchetezDesCartes(String pCommande) {
        JoueurClient joueur = this.getJoueur(pCommande);
        String message = " A terminé de placer ses renforts, il peut acheter des cartes ! ";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.INFOCHAT, joueur);


        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueur == this){//Si on est le joueur en question, on va devoir pouvoir des cartes !
                mainView.demarrerAchatDeCartes();
            }

        });
    }

    protected void commandeJoueurDemandeAAcheterUnObjectif(String pCommande)
    {
        JoueurClient joueur = this.getJoueur(pCommande);
        String message = " A choisi d'acheter un objectif, il tire 2 cartes et va en choisir une ! ";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        joueur.setArgent(joueur.getArgent()-200);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesZonesFamilles();
        });

    }
    protected void commandeChoisirUneCarteObjectif(String pCommand)
    {
        CarteObjectif carteObjectif1 = riskGOTCartesObjectifs.getCarteObjectifParIdStr(pCommand.split(";")[0]);
        CarteObjectif carteObjectif2 = riskGOTCartesObjectifs.getCarteObjectifParIdStr(pCommand.split(";")[1]);
        ChatMessage chatMessage = new ChatMessage("Vous devez maintenant choisir 1 objectifs parmis les 2 proposés", ChatMessage.ChatMessageType.ACTION);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.faireChoixObjectif(carteObjectif1,carteObjectif2);
        });
    }

    protected void commandeJoueurAChoisiUnObjectif(String pCommand){
        //message: joueurname;carte1ID,Y/N;carte2ID,Y/N
        Joueur joueur = getJoueur(pCommand.split(";")[0]);
        pCommand=pCommand.substring(joueur.getNom().length()+1);
        CarteObjectif carteSelectionnee = null;
        for (String str : pCommand.split(";")){
            if (str.split(",")[1].equals("Y")){
                carteSelectionnee =riskGOTCartesObjectifs.getCarteObjectifParIdStr(str.split(",")[0]);
                joueur.aPiocheUneCarteObjectif(carteSelectionnee);
            }
        }
        ChatMessage chatMessage = new ChatMessage(" a choisi sa carte objectif", ChatMessage.ChatMessageType.INFOCHAT, joueur);
        CarteObjectif finalCarteSelectionnee = carteSelectionnee;
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueur == this) {
                mainView.ajouterUneCarteObjectif(finalCarteSelectionnee);
            }
            mainView.rafraichirLesZonesFamilles();

        });
    }


    protected void commandeJoueurPasseAchatDeCartes(String pCommand)
    {
        JoueurClient joueurClient = getJoueur(pCommand);
        String messageChat = " n'achète pas de cartes objectifs ou mestres supplémentaires.";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
        });
    }



    protected void commandeEnvahissez(String pCommande) {
        JoueurClient joueur = this.getJoueur(pCommande);
        String message = " A terminé d'acheter des cartes, il peut maintenant envahir ! ";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        for (CartePersonnage cartePersonnage : joueur.getFamille().getCartesPersonnages()){
            cartePersonnage.setUtilisee(false);
        }
        Platform.runLater(() -> {
            if (joueur==this){
                mainView.actualiserLesPersonnages();
            }
            mainView.updateChatZone(chatMessage);
        });
    }

    protected void commandeLancerInvasion(String pCommande) {

        invasionEnCours = new Invasion();
        manoeuvreEnCours = new Manoeuvre();

        Platform.runLater(() -> {
            mainView.demarrerUneInvasion();
        });

    }


    protected void commandeJoueurLanceUneInvasion(String message) {
        //Format string --> récupérer les territoires... créer l'objet invasion courante etc...
        //pJoueurServer.getNom()+";"+pTerritoireSource.getNom()+";"+pTerritoireCible.getNom();
        Joueur joueur = getJoueur(message.split(";")[0]);
        Territoire territoireSource = riskGOTterritoires.getTerritoireParNomStr(message.split(";")[1]);
        Territoire territoireCible = riskGOTterritoires.getTerritoireParNomStr(message.split(";")[2]);
        invasionEnCours = new Invasion();
        invasionEnCours.setTerritoireSource(territoireSource);
        invasionEnCours.setTerritoireCible(territoireCible);
        Platform.runLater(() -> {
            mainView.setSousEtat(SousEtat.ENVAHISSEZ);
            mainView.rafraichirTousLesTerritoiresEnEvidence();
            mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.afficheFenetreNouvelleInvasion();
        });

    }

    protected void commandeJoueurRemplaceDesDesSixParDesDesHuit(String message){
        Joueur joueur = getJoueur(message.split(";")[0]);
        int nbDes = Integer.parseInt(message.split(";")[1]);
        if (joueur==invasionEnCours.getJoueurDefenseur()) {
            invasionEnCours.ajouterDesDesHuitFacesAuDefenseur(nbDes);
        }
        else{
            invasionEnCours.ajouterDesDesHuitFacesAuAttaquant(nbDes);
        }
        String messagePourChat = "transforme " + nbDes + " de SIX en des de HUIT pendant l'invasion !";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
        });

    }

    protected void commandeAttaquantGagneLesEgalitesPendantLinvasion(){
        invasionEnCours.setAttaquantGagneLesEgalites(true);
        String messagePourChat = "gagne les égalités durant cette invasion !";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, invasionEnCours.getJoueurAttaquant());
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
        });
    }


    protected void commandeJoueurAValideSesTroupesEnAttaque(String message) {
        Joueur joueur = getJoueur(message.split(";")[0]);
        int nbTroupes = Integer.parseInt(message.split(";")[1]);
        int nbChevaliers = Integer.parseInt(message.split(";")[2]);
        int nbEnginsDeSiege = Integer.parseInt(message.split(";")[3]);
        invasionEnCours.getTerritoireSource().setArmeeEngagees(nbTroupes);

        invasionEnCours.getTerritoireSource().setChevaliersEngagesDansLaBataille(nbChevaliers);
        invasionEnCours.getTerritoireSource().setEnginsDeSiegeEngagesDansLaBataille(nbEnginsDeSiege);
        invasionEnCours.getTerritoireSource().setFortificationsEngagesDansLaBataille(0); // Comme on est en attaque
        String messagePourChat = "a engagé " + nbTroupes + " troupe(s) dans l'attaque";
        if (nbChevaliers>0)
        {
            messagePourChat=messagePourChat+"\nIl ajoute également " + nbChevaliers + " chevalier(s) !";

        }
        if (nbEnginsDeSiege>0)
        {
            messagePourChat=messagePourChat+"\nIl ajoute également " + nbEnginsDeSiege + " engin(s) de siège !";

        }
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.getInvasionGuiCourante().getJoueurAttaquantGui().confirmationValidationNombreDarmee();
        });
    }

    protected void commandeJoueurAValideSesTroupesEnDefense(String message) {
        Joueur joueur = getJoueur(message.split(";")[0]);
        int nbTroupes = Integer.parseInt(message.split(";")[1]);
        int nbChevaliers = Integer.parseInt(message.split(";")[2]);
        int nbEnginsDeSiege = Integer.parseInt(message.split(";")[3]);
        int nbFortifications = Integer.parseInt(message.split(";")[4]);
        invasionEnCours.getTerritoireCible().setArmeeEngagees(nbTroupes);
        String messagePourChat = "a engagé " + nbTroupes + " troupe(s) pour défendre son territoire";

        invasionEnCours.getTerritoireCible().setChevaliersEngagesDansLaBataille(nbChevaliers);
        invasionEnCours.getTerritoireCible().setEnginsDeSiegeEngagesDansLaBataille(nbEnginsDeSiege);
        invasionEnCours.getTerritoireCible().setFortificationsEngagesDansLaBataille(nbFortifications);
        if (nbChevaliers>0)
        {
            messagePourChat=messagePourChat+"\nIl possède également " + nbChevaliers + " chevalier(s) !";

        }
        if (nbEnginsDeSiege>0)
        {
            messagePourChat=messagePourChat+"\nIl possède également " + nbEnginsDeSiege + " engin(s) de siège !";

        }

        if (nbFortifications>0)
        {
            messagePourChat=messagePourChat+"\nIl possède également " + nbFortifications + " fortification(s) !";
        }
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.getInvasionGuiCourante().getJoueurDefenseurGui().confirmationValidationNombreDarmee();
        });
    }

    protected void commandeLancezVosDesPourLaBataille(String message) {
        ChatMessage chatMessage;
        if (invasionEnCours.getTerritoireCible().getAppartientAJoueur() == this || invasionEnCours.getTerritoireSource().getAppartientAJoueur() == this) {
            chatMessage = new ChatMessage("Veuillez lancer vos dès pour la bataille !", ChatMessage.ChatMessageType.ACTION);
        } else {
            chatMessage = new ChatMessage("Les joueurs vont maintenant lancer leurs dés pour la bataille !", ChatMessage.ChatMessageType.INFO);
        }
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.getInvasionGuiCourante().pretALancerLesDes();

        });
    }


    protected void commandeJoueurALanceLesDesEnAttaque(String message) {
        Joueur joueur = getJoueur(message.split(";")[0]);
        String messageSansJoueur = message.substring(joueur.getNom().length() + 1);
        invasionEnCours.resetResultatDesAttaquant();
        for (String s : messageSansJoueur.split(";")) {
            invasionEnCours.getResultatsDesAttaquant().add(new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), Integer.parseInt(s.split(",")[2])));
        }
        invasionEnCours.setJoueurSourceALanceLesDes(true);
        String messagePourChat = "a réalisé un lancé de dé en attaque : " + messageSansJoueur;
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.getInvasionGuiCourante().getJoueurAttaquantGui().confirmationValidationResultatDesDes();
        });
    }


    protected void commandeJoueurALanceLesDesEnDefense(String message) {
        Joueur joueur = getJoueur(message.split(";")[0]);
        String messageSansJoueur = message.substring(joueur.getNom().length() + 1);
        invasionEnCours.resetResultatDesDefenseur();
        for (String s : messageSansJoueur.split(";")) {
            invasionEnCours.getResultatsDesDefenseur().add(new DeTypeValeur(DeTypeValeur.TypeDe.valueOf(s.split(",")[0]), Integer.parseInt(s.split(",")[1]), Integer.parseInt(s.split(",")[2])));
        }
        invasionEnCours.setJoueurCibleALanceLesDes(true);
        String messagePourChat = "a réalisé un lancé de dé en défense : " + messageSansJoueur;
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.getInvasionGuiCourante().getJoueurDefenseurGui().confirmationValidationResultatDesDes();
        });

    }

    protected void commandeLaBatailleEstTerminee(String message) {
        invasionEnCours.resoudreLaBatailleEnCoursApresLeLancerDeDes();
        String messagePourChat = invasionEnCours.getResultatDeLaBatailleEnCoursString();
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            //mainView.rafraichirLesZonesFamilles();
            //mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.getInvasionGuiCourante().mettreEnEvidenceLesDesVainqueurs(false);
        });
    }


    protected void commandeJoueurPeutMettreUnDesDesASaValeurMaximale(String message){
        Joueur joueur = getJoueur(message);
        String messagePourChat = "va choisir un de ses dés de défense et le passer à sa valeur maximale";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, joueur);

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueur==invasionEnCours.getJoueurDefenseur()) {
                mainView.getInvasionGuiCourante().getJoueurDefenseurGui().permettreDeMaxerUnDe();
            }
            if (joueur==invasionEnCours.getJoueurAttaquant()) {
                mainView.getInvasionGuiCourante().getJoueurAttaquantGui().permettreDeMaxerUnDe();
            }
        });
    }




    protected void commandeJoueurAValideLeResultatDeLaBataille(String message) {
        Joueur joueur = getJoueur(message);
        String messagePourChat = "a valide le resultat de la bataille (il ne joue donc pas de mestre ou personnage supplémentaire)";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        if (joueur == invasionEnCours.getJoueurAttaquant()) {
            invasionEnCours.setJoueurSourceAValideLeResultatDeLaBataille(true);
        }
        if (joueur == invasionEnCours.getJoueurDefenseur()) {
            invasionEnCours.setJoueurCibleAValideLeResultatDeLaBataille(true);
        }

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueur == invasionEnCours.getJoueurAttaquant()) {
                mainView.getInvasionGuiCourante().getJoueurAttaquantGui().joueurConfirmeLeResultatDeLaBataille();
            }
            if (joueur == invasionEnCours.getJoueurDefenseur()) {
                mainView.getInvasionGuiCourante().getJoueurDefenseurGui().joueurConfirmeLeResultatDeLaBataille();
            }

        });
    }


    protected void commandeLaBatailleEstTermineeEtLeResultatEstValide(String message)
    {
        invasionEnCours.resoudreLaBatailleEnCoursApresToutesLesValidations();
        String messagePourChat = invasionEnCours.getResultatDeLaBatailleEnCoursString();
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesZonesFamilles();
            mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.getInvasionGuiCourante().mettreEnEvidenceLesDesVainqueurs(true);
        });
    }



    protected void commandeJoueurContinueUneInvasion(String message) {
        Joueur joueur = getJoueur(message.split(";")[0]);
        int nbBataille = Integer.parseInt(message.split(";")[3]);
        invasionEnCours.resetPourProchaineBataille();
        invasionEnCours.setNbBataillesTermineesDansLinvasion(nbBataille);
        Platform.runLater(() -> {
            mainView.continuerUneInvasion();
        });
    }

    protected void commandeJoueurArreteUneInvasion(String message) {
        Joueur joueur = getJoueur(message.split(";")[0]);
        String messagePourChat = " arrête l'invasion en cours !";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, joueur);
        Platform.runLater(() -> {
            mainView.rafraichirTousLesTerritoiresEnEvidence();
            mainView.updateChatZone(chatMessage);
        });
    }

    protected void commandeInvasionTermineeDefaiteDefenseur(String message) {
        invasionEnCours.setManoeuvrerSansContrainte(Boolean.parseBoolean(message.split(";")[4]));
        String messagePourChat = "L'invasion est terminée!\nVictoire de " + this.invasionEnCours.getTerritoireSource().getAppartientAJoueur().getNomAtFamille() + " qui gagne le territoire " + this.invasionEnCours.getTerritoireCible().getNom().name() + "\nqui appartenait à " + this.invasionEnCours.getTerritoireCible().getAppartientAJoueur().getNomAtFamille() + "!!";
        messagePourChat=messagePourChat+"\n" + this.invasionEnCours.getJoueurAttaquant().getNomAtFamille() + " peut maintenant manoeuvrer";
        if (invasionEnCours.isManoeuvrerSansContrainte()){
            messagePourChat=messagePourChat + " sans contraintes !";
        }
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        invasionEnCours.victoireAttaquant();

        String finalMessagePourChat = messagePourChat;
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.getInvasionGuiCourante().invasionTermineeDefaiteDefenseur(finalMessagePourChat);
        });

    }

    protected void commandeInvasionTermineeDefaiteAttaquant(String message) {
        String messagePourChat = this.invasionEnCours.getTerritoireSource().getAppartientAJoueur().getNomAtFamille() + " n'a plus de troupes disponible pour continuer son invasion !\nVictoire de " + this.invasionEnCours.getTerritoireCible().getAppartientAJoueur().getNomAtFamille() + "qui garde son territoire " + this.invasionEnCours.getTerritoireCible().getNom().name() + " !";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        invasionEnCours.victoireDefenseur();


        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.getInvasionGuiCourante().invasionTermineeDefaiteAttaquant(messagePourChat);
        });
    }


    protected void commandeInvasionPeutContinuer(String message) {
        String messagePourChat = "La bataille est terminée, mais l'invasion peut continuer\nsi " + this.invasionEnCours.getTerritoireSource().getAppartientAJoueur().getNomAtFamille() + " le désire !";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.getInvasionGuiCourante().invasionPeutContinuer(messagePourChat);
        });
    }



    protected void commandeJoueurEffectueUneManoeuvre(String message) {
        //pJoueur.getNom()+";"+invasionEnCours.getTerritoireSource().getNom().name()+";"+invasionEnCours.getTerritoireCible().getNom().name()+";"+nbTroupesEnManoeuvre
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        Territoire terSource = riskGOTterritoires.getTerritoireParNomStr(message.split(";")[1]);
        Territoire terCible = riskGOTterritoires.getTerritoireParNomStr(message.split(";")[2]);
        int nbTroupesEnManoeuvre = Integer.parseInt(message.split(";")[3]);
        terSource.ajouteDesTroupes(-nbTroupesEnManoeuvre);
        terCible.ajouteDesTroupes(nbTroupesEnManoeuvre);
        String messageChat = " A effectue une manoeuvre de " + nbTroupesEnManoeuvre + " depuis " + terSource.getNom().name() + " vers " + terCible.getNom().name();
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.mettreAJourUnTerritoireSurLaCarte(terSource, true, true);
            mainView.mettreAJourUnTerritoireSurLaCarte(terCible, true, false);
            mainView.rafraichirLesZonesFamilles();
        });
    }

    protected void commandeManoeuvrezEnFinDeTour(String message)
    {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " arrête les invasions et peut maintenant manoeuvrer en fin de tour";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueurClient==this)
                mainView.manoeuvrer();
        });

    }

    protected void commandeJoueurPasseLaManoeuvre(String message) {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " n'effectue aucune manoeuvre de fin de tour !";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
        });


    }

    protected void commandeAtteignezUnObjectifEnFinDeTour(String message)
    {
        mainView.setSousEtat(SousEtat.ATTEIGNEZ_UN_OBJECTIF);
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " peut maintenant atteindre un objectif !";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueurClient==this) {
                mainView.atteignezUnObjectifEnFinDeTour();
            }
        });
    }

    protected void commandeJoueurAAtteintUnObjectifEnFinDeTour(String message)
    {
        //envoieMessageATous(ClientCommandes.JOUEUR_A_ATTEINT_UN_OBJECTIF_EN_FIN_DE_TOUR, pJoueur.getNom()+";"+ pCarteObjectif.getIdAsStr());
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        CarteObjectif carteObjectif = this.riskGOTCartesObjectifs.getCarteObjectifParIdStr(message.split(";")[1]);
        joueurClient.atteintUnObjectif(carteObjectif);
        String messageChat = " a atteint un objectif d'une valeur de " + carteObjectif.getNbPointsDeVictoire() + " points de victoire.";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.joueurAAtteintUnObjectif(joueurClient, carteObjectif);
        });
    }

    protected void commandeJoueurEstVictorieux(String message)
    {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " EST VICTORIEUX !!!\nIL A ATTEINT LES 10 PTS DE VICTOIRES ET CONTROLE SA CAPITALE !!!!";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.joueurEstVictorieux(joueurClient);
        });
    }


    protected void commandeJoueurNatteintPasDobjectif(String message){
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " n'atteint pas d'objectif ce tour là !";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
        });
    }


    protected void commandeJoueurPeutPiocheUneCarteTerritoire(String message){
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " Le joueur " + joueurClient.nom + " peut maintenant piocher une carte territoire";
        String messageChat2 = "Veuillez maintenant piocher une carte territoire (en cliquant sur PIOCHER)";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        ChatMessage chatMessage2 = new ChatMessage(messageChat2, ChatMessage.ChatMessageType.ACTION);
        Platform.runLater(() -> {

            if (this==joueurClient) {
                mainView.updateChatZone(chatMessage2);
                mainView.peutPiocherUneCarteTerritoire();
            }
            else{
                mainView.updateChatZone(chatMessage);
            }
        });
    }

    protected void commandeJoueurNePeutPasPiocherDeCarteTerritoireEnFinDeTour(String message){
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = "ne peut pas piocher de carte territoire en fin de tour, car il n'a conquis aucun territoire ! ";
        String messageChat2 = "Vous ne pouvez pas piocher de carte territoire à ce tour (vous n'avez conquis aucun territoire.\nVeuillez cliquer sur TERMINER (ou jouer une carte personnage si vous le pouvez))";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        ChatMessage chatMessage2 = new ChatMessage(messageChat2, ChatMessage.ChatMessageType.ACTION);
        Platform.runLater(() -> {

            if (this==joueurClient) {
                mainView.updateChatZone(chatMessage2);
                mainView.finDeTour();
            }
            else{
                mainView.updateChatZone(chatMessage);
            }
        });
    }


    protected void commandeJoueurAPiocheUneCarteTerritoire(String message) {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        CarteTerritoire carteTerritoire = this.riskGOTCartesTerritoires.piocher(joueurClient, message.split(";")[1]);
        String messageChat = " Le joueur " + joueurClient.nom + " a pioche une carte territoire";
        String messageChat2 = "Vous avez pioché le territoire " + carteTerritoire.getTerritoire().getNom() + " comportant l'unité spéciale " + carteTerritoire.getUniteSpeciale().name();
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        ChatMessage chatMessage2 = new ChatMessage(messageChat2, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueurClient == this) {
                mainView.updateChatZone(chatMessage2);
                mainView.ajouterUneCarteTerritoire(carteTerritoire);
            }
        });
    }

    protected void commandeJoueurAPiocheUneCarteTerritoireEnFinDeTour(String message){
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        CarteTerritoire carteTerritoire = this.riskGOTCartesTerritoires.piocher(joueurClient, message.split(";")[1]);
        String messageChat = " Le joueur " + joueurClient.nom + " a pioche une carte territoire en fin de tour";
        String messageChat2 = "Vous avez pioché le territoire " + carteTerritoire.getTerritoire().getNom() + " comportant l'unité spéciale " + carteTerritoire.getUniteSpeciale().name() + " en fin de tour";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        ChatMessage chatMessage2 = new ChatMessage(messageChat2, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            if (joueurClient == this) {
                mainView.updateChatZone(chatMessage2);
                mainView.ajouterUneCarteTerritoire(carteTerritoire);
                mainView.finDeTour();
            }
        });
    }

    protected void commandeJoueurTermineSonTour(String message)
    {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " a terminé son tour !";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
        });
    }


    protected void commandeConvertissezVosCartesTerritoires() {
        String messageChat = "Vous pouvez convertir des cartes territoires : Sélectionnez des cartes territoires. Si vous en sélectionnez 1 ou 2, elles seront converties en unités spéciales. Si vous en sélectionnez 3 et que celà correspond à un bonus, vous aurez des troupes supplémentaires.";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.ACTION);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.selectionnerDesCartesTerritoires();
        });
    }

    protected void commandeJoueurAConvertiUneCarteTerritoireEnUniteSpeciale(String message) {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        CarteTerritoire carteTerritoire = this.riskGOTCartesTerritoires.getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[1]));
        joueurClient.utiliseUneCarteTerritoire(carteTerritoire);
        String messageChat = " a converti la  carte territoire " + carteTerritoire.getTerritoire().getNom().name() + " en unitée spéciale " + carteTerritoire.getUniteSpeciale().name();
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesZonesFamilles();
            if (joueurClient == this) {

                mainView.placerUneUniteSpeciale(carteTerritoire);
            }
        });
    }

    protected void commandeJoueurADeployeUneUniteSpeciale(String message)
    {
        //envoieMessageATous(ClientCommandes.JOUEUR_A_DEPLOYE_UNE_UNITE_SPECIALE, pJoueur.getNom() + ";" + territoire.getNom().name()+";"+ uniteSpeciale.name());
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        Territoire ter = this.riskGOTterritoires.getTerritoireParNomStr(message.split(";")[1]);
        CarteTerritoire.UniteSpeciale uniteSpeciale = CarteTerritoire.UniteSpeciale.valueOf(message.split(";")[2]);
        ter.getUniteSpeciales().add(uniteSpeciale);
        String messageChat = "a posé une unité spéciale " + uniteSpeciale.name() + " sur le territoire " + ter.getNom().name();
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.mettreAJourUnTerritoireSurLaCarte(ter, false, true);
        });

    }

    protected void commandeJoueurAConvertiTroisCartesTerritoiresEnTroupesSupplementaires(String message){
        //envoieMessageATous(ClientCommandes.JOUEUR_A_CONVERTI_TROIS_CARTES_TERRITOIRE_EN_TROUPES_SUPPLEMENTAIRES, joueurCourant.getNom()+";"+pCarteTerritoire1.getTerritoire().getNom().name()+";"+pCarteTerritoire2.getTerritoire().getNom().name()+";"+pCarteTerritoire3.getTerritoire().getNom().name()+";"+ bonusTroupes+";"+bonusTroupeSupplementairesCarJoueurPossedeTerritoire);
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        CarteTerritoire carteTerritoire1 = this.riskGOTCartesTerritoires.getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[1]));
        CarteTerritoire carteTerritoire2 = this.riskGOTCartesTerritoires.getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[2]));
        CarteTerritoire carteTerritoire3 = this.riskGOTCartesTerritoires.getCarteTerritoireParNom(Territoire.TerritoireNames.valueOf(message.split(";")[3]));
        int bonusTroupes = Integer.parseInt(message.split(";")[4]);
        int bonusTroupeSupplementairesCarJoueurPossedeTerritoire = Integer.parseInt(message.split(";")[5]);

        String messageChat = " a converti les 3 cartes territoire suivantes :\n-"
                + carteTerritoire1.getTerritoire().getNom().name() +" - " +carteTerritoire1.getUniteSpeciale().name() + "\n-"
                + carteTerritoire2.getTerritoire().getNom().name() +" - " +carteTerritoire2.getUniteSpeciale().name() + "\n-"
                + carteTerritoire3.getTerritoire().getNom().name() +" - " +carteTerritoire3.getUniteSpeciale().name() + "\n-"
                + "et reçoit donc " + bonusTroupes + " supplémentaires à placer !";

        if (bonusTroupeSupplementairesCarJoueurPossedeTerritoire>0){
            messageChat=messageChat+"\nDe plus il reçoit "+bonusTroupeSupplementairesCarJoueurPossedeTerritoire+ " car il possède au moins un des territoires présents sur les cartes converties.\n Le joueur doit placer ces 2 troupes supplémentaires sur le ou les territoires en questions";
        }

        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);

        joueurClient.utiliseUneCarteTerritoire(carteTerritoire1);
        joueurClient.utiliseUneCarteTerritoire(carteTerritoire2);
        joueurClient.utiliseUneCarteTerritoire(carteTerritoire3);
        joueurClient.setNbTroupeAPlacer(joueurClient.getNbTroupeAPlacer()+bonusTroupes+bonusTroupeSupplementairesCarJoueurPossedeTerritoire);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesZonesFamilles();
        });

    }



    //PERSONNAGES

    public void commandeJoueurVaUtiliserUneCartePersonnage(String message){
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        CartePersonnage cartePersonnage = joueurClient.getFamille().getCartePersonnageParNom(CartePersonnage.PersonnageNames.valueOf(message.split(";")[1]));
        joueurClient.setArgent(joueurClient.getArgent()-cartePersonnage.getCout());
        cartePersonnage.setUtilisee(true);
        Platform.runLater(() -> {
            mainView.rafraichirLesZonesFamilles();
            mainView.joueurJoueUneCartePersonnage(joueurClient, cartePersonnage);

        });

    }

    public void commandeCartePersonnageNonUtilisable(String message)
    {
        CartePersonnage cartePersonnage = this.getFamille().getCartePersonnageParNom(CartePersonnage.PersonnageNames.valueOf(message.split(";")[0]));
        String messageChat = "Désolé, vous ne pouvez pas utiliser votre carte personnage " + cartePersonnage.getName().name() +"\n"+ (message.split(";")[1]);
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.cartePersonnagePasJouable(cartePersonnage, messageChat);
        });

    }

    public void commandePasAssezDargentPourUtiliserCartePersonnage(String message){
        CartePersonnage cartePersonnage = this.getFamille().getCartePersonnageParNom(CartePersonnage.PersonnageNames.valueOf(message));
        String messageChat = "Désolé, vous n'avez pas suffisament d'argent pour utiliser votre carte personnage " + cartePersonnage.getName().name();
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.cartePersonnagePasJouable(cartePersonnage, messageChat);
        });
    }


    public void commandeJoueurVolDeLargent(String message)
    {
        JoueurClient joueurVoleur = getJoueur(message.split(";")[0]);
        JoueurClient joueurVole = getJoueur(message.split(";")[1]);
        int argentVole = Integer.parseInt(message.split(";")[2]);
        joueurVole.setArgent(joueurVole.getArgent()-argentVole);
        joueurVoleur.setArgent(joueurVoleur.getArgent()+argentVole);
        String messageChat = " vol " +argentVole + " pièces d'or à " + joueurVole.getNomAtFamille();
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT,joueurVoleur);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesZonesFamilles();
        });




    }




}








