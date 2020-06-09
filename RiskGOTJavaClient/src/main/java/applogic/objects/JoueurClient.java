package applogic.objects;
import common.ClientCommandes;
import common.objects.*;
import common.objects.cartes.CarteTerritoire;
import common.objects.cartes.CartesTerritoires;
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
        });
        Platform.runLater(() -> this.mainView.refreshFamilleJoueur(famille, finalJoueur));

    }

    protected void commandeChoixFamilleTerminee(String pCommande) {
        //Commande reçue: NB_TROUPES_DEMARRAGE;28
        int nbTroupesAPlacer = riskGOTFamilles.initCapitales(nbJoueurs);
        for (Famille f : riskGOTFamilles.getFamillesActives()) {
            Platform.runLater(() -> this.mainView.mettreAJourUnTerritoireSurLaCarte(f.getCapitale(), true, false));
        }
        Platform.runLater(() -> this.mainView.rafraichirLesZonesFamilles());
        mainView.setEtatPrincipal(Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE);

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


    protected void commandeChoisirUnTerritoireDemarrage(String pCommand) {
        System.out.println("Je dois choisir un territoire de départ");
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
        String message = "Vous avez " + nbTroupesRestantAPlacer + " troupes à déployer, veuillez choisir un territoire qui vous appartient pour y affecter un renfort !";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.ACTION);
        mainView.setSousEtatRenforcez(SousEtatRenforcez.DEPLOYEZ_DES_TROUPES);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.setCarteCliquable(true);
        });

    }

    protected void commandeJoueurARenforceUnTerritoire(String pCommand) {
        //Message type : NOM_JOUEUR;FORT_TERREUR
        Territoire territoire = this.getRiskGOTterritoires().getTerritoireParNomStr(pCommand.split(";")[1]);
        territoire.ajouteDesTroupesAPlacer(1);
        ChatMessage chatMessage = new ChatMessage("a ajouté une troupe sur :" + territoire.getNom(), ChatMessage.ChatMessageType.INFOCHAT, territoire.getAppartientAJoueur());

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.mettreAJourUnTerritoireSurLaCarte(territoire, true, true);

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

    protected void commandeEnvahissez(String pCommande) {
        JoueurClient joueur = this.getJoueur(pCommande);
        String message = " A terminé de placer ses renforts, il peut maintenant envahir ! ";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.INFOCHAT, joueur);
        updateChatText(chatMessage);


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
            mainView.rafraichirTousLesTerritoiresEnEvidence();
            mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.afficheFenetreNouvelleInvasion();
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
        invasionEnCours.resoudreLaBatailleEnCours();
        String messagePourChat = invasionEnCours.getResultatDeLaBatailleEnCoursString();
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFO);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesZonesFamilles();
            mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.getInvasionGuiCourante().mettreEnEvidenceLesDesVainqueurs();
        });
    }


    protected void commandeJoueurContinueUneInvasion(String message) {
        Joueur joueur = getJoueur(message.split(";")[0]);
        int nbBataille = Integer.parseInt(message.split(";")[3]);
        invasionEnCours.resetPourProchaineBataille();
        invasionEnCours.setNbBataille(nbBataille);
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
        String messagePourChat = "L'invasion est terminée!\nVictoire de " + this.invasionEnCours.getTerritoireSource().getAppartientAJoueur().getNomAtFamille() + "\nqui gagne le territoire " + this.invasionEnCours.getTerritoireCible().getNom().name() + "\nqui appartenait à " + this.invasionEnCours.getTerritoireCible().getAppartientAJoueur().getNomAtFamille() + "!!";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.INFO_IMPORTANTE);
        invasionEnCours.victoireAttaquant();

        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
            mainView.rafraichirLesTerritoiresApresInvasion();
            mainView.getInvasionGuiCourante().invasionTermineeDefaiteDefenseur(messagePourChat);
        });

    }

    protected void commandeInvasionTermineeDefaiteAttaquant(String message) {
        String messagePourChat = this.invasionEnCours.getTerritoireSource().getAppartientAJoueur().getNomAtFamille() + " n'a plus de troupes disponible pour continuer son invasion !\nVictoire de " + this.invasionEnCours.getTerritoireCible().getAppartientAJoueur().getNomAtFamille() + "\nqui garde son territoire " + this.invasionEnCours.getTerritoireCible().getNom().name() + " !";
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


    protected void commandeJoueurPasseLaManoeuvre(String message) {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        String messageChat = " n'effectue aucune manoeuvre de fin de tour !";
        ChatMessage chatMessage = new ChatMessage(messageChat, ChatMessage.ChatMessageType.INFOCHAT, joueurClient);
        Platform.runLater(() -> {
            mainView.updateChatZone(chatMessage);
        });


    }


    protected void commandeJoueurAPiocheUneCarteTerritoire(String message) {
        JoueurClient joueurClient = getJoueur(message.split(";")[0]);
        CarteTerritoire carteTerritoire = this.riskGOTCartesTerritoires.piocher(joueurClient, message.split(";")[1]);
        String messageChat = " Le joueur " + joueurClient.nom + " a pioche une carte territoire";
        String messageChat2 = "Vous avez picohé le territoire " + carteTerritoire.getTerritoire().getNom() + " comportant l'unité spéciale " + carteTerritoire.getUniteSpeciale().name();
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

}








