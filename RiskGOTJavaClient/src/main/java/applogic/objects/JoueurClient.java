package applogic.objects;
import common.ClientCommandes;
import common.objects.*;
import gui.MainView;
import javafx.application.Platform;

import java.util.ArrayList;

public class JoueurClient extends Joueur {

    private ArrayList<JoueurClient> adversaires;
    private Familles riskGOTFamilles;
    private Regions riskGOTregions;

    public Territoires getRiskGOTterritoires() {
        return riskGOTterritoires;
    }

    private Territoires riskGOTterritoires;


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
        this.nbJoueurs=1;
        isMe=true;

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


    protected void updateChatText(String txt) {
        Platform.runLater(() -> {
            //an event with a button maybe
            this.mainView.updateTxt(txt);
        });
    }

    protected void commandeWelcome(String pCommande)
    {
        nbJoueursAttendus = Integer.parseInt(pCommande.split(";")[1]);
        riskGOTFamilles = new Familles(nbJoueursAttendus);
        riskGOTregions = new Regions();
        riskGOTterritoires= new Territoires(riskGOTregions, riskGOTFamilles);

        this.updateChatText("BIENVENUE [" + this.nom + "] SUR RISK GOT ONLINE !!");
        this.updateChatText("LA PARTIE VA SE JOUER A [" +nbJoueursAttendus + "] JOUEURS");
        this.updateChatText("EN ATTENTE DES AUTRES JOUEURS ...");
        Platform.runLater(() -> {
            mainView.createComponentsFamilles(riskGOTFamilles);
            mainView.createComponentsZoneListeJoueurs(nbJoueursAttendus);
        });

    }

    protected void commandeNouvelleConnection(String pCommande)
    {
        String joueurName;
        joueurName = pCommande.split(";")[1];
        if (!joueurName.equals(this.nom)) {
            JoueurClient joueurClient = new JoueurClient(joueurName);
            this.getAdversaires().add(joueurClient);
            nbJoueurs++;
            Platform.runLater(() -> mainView.nouveauJoueurConnecte(joueurClient));
        }
    }

    protected void commandeTousConnecte(String pCommande) {
        if (pCommande.split(";")[1].equals("DEBUG")){ // CAS DEBUG - ON SIMULE LENVOIE DU DE
            Integer de=(int)(1+6*Math.random());
            mainView.getClientConnexion().sendCommand(ClientCommandes.LANCE_1DE_START,de.toString());
        }
        else { // CAS NORMAL - LE JOUEUR DOIT CLIQUER POUR LANCER LE DE
            this.updateChatText("ACTION: Veuillez lancer un dés pour savoir qui pourra choisir en premier la première Famille.\nLe plus grand dé pourra choisir sa famille en premier.\nEn cas d'éaglité, l'heure de connection prime (le premier connecté aura la priorité !)");
            Platform.runLater(() -> mainView.PretALancerUnDes());
        }
    }

    protected void commandeFaireChoixFamille(String pCommande)
    {
        //le message a la forme "FAIRE_CHOIX_FAMILLE;Fam1;Fam2;Fam3)
        String listeFamillelDispo = pCommande.replaceAll(ClientCommandes.FAIRE_CHOIX_FAMILLE.name()+";", "");

        Platform.runLater(() -> mainView.faireChoixFamille(listeFamillelDispo));
    }

    protected void commandeMiseAJourFamilleJoueur(String pCommande)
    {
        //le message a la forme "JOUEUR_A_FAIT_CHOIX_FAMILLE;NOM_JOUEUR;NOM_FAMILLE)
        String nomJoueur = pCommande.split(";")[1];
        String nomFamille= pCommande.split(";")[2];

        JoueurClient joueur = this.getAversaireParNom(nomJoueur);
        if (joueur==null)
        {
            joueur = this;
        }

        Famille famille = riskGOTFamilles.getFamilleParNomString(nomFamille);
        joueur.setFamille(famille);
        JoueurClient finalJoueur = joueur;
        Platform.runLater(() -> this.mainView.refreshFamilleJoueur(famille, finalJoueur));

    }

    protected void commandeChoixFamilleTerminee(String pCommande)
    {
        //Commande reçue: NB_TROUPES_DEMARRAGE;28
        int nbTroupesAPlacer = riskGOTFamilles.initCapitales(nbJoueurs);
        for (Famille f : riskGOTFamilles.getFamilles())
        {
            Platform.runLater(() -> this.mainView.mettreAJourUnTerritoireSurLaCarte(f.getCapitale()));
        }
        Platform.runLater(() -> this.mainView.rafraichirLesZonesFamilles());




    }

    private JoueurClient getAversaireParNom(String pNom)
    {
     for (JoueurClient j : adversaires){
         if (pNom.equals(j.nom)){
             return j;
         }

     }
     return null;
    }


    protected void commandeChoisirUnTerritoireDemarrage(String pCommand)
    {
        System.out.println("Je dois choisir un territoire de départ");
        ArrayList<Territoire> territoiresLibres = new ArrayList<>();
        int nbTroupesAPlacer = Integer.valueOf(pCommand.split(";")[1]);
        if (nbTroupesAPlacer>0&&pCommand.split(";").length>1)
        {
            for (int i=2; i<pCommand.split(";").length; i++){
                Territoire ter = riskGOTterritoires.getTerritoireParNomStr(pCommand.split(";")[i]);
                territoiresLibres.add(ter);
            }

        }

        Platform.runLater(() -> mainView.choisirUnTerritoireDemarrage(territoiresLibres));

    }


    protected void commandeJoueurAChoisiUnTerritoireDemarrage(String pCommand){
        //Message type : JOUEUR_A_PLACE_UNE_TROUPE;NOM_JOUEUR;THE_DREADFORT
        JoueurClient joueur = this.getAversaireParNom(pCommand.split(";")[1]);
        if (joueur==null)
        {
            joueur = this;
        }
        Territoire territoire = this.getRiskGOTterritoires().getTerritoireParNomStr(pCommand.split(";")[2]);
        territoire.setAppartientAJoueur(joueur);
        territoire.ajouteDesTroupes(1);
        Platform.runLater(() -> mainView.mettreAJourUnTerritoireSurLaCarte(territoire));
    }


    protected void commandePlacerUneTroupeDemarrage(String pCommand){
        System.out.println("Je dois placer une troupe sur un de mes territoires");
        Platform.runLater(() -> mainView.placerUneTroupeSurUnTerritoire());

    }


    protected void commandeJoueurAChoisiUneTroupeDemarrage(String pCommand) {
        //Message type : JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE;NOM_JOUEUR;THE_DREADFORT
        JoueurClient joueur = this.getAversaireParNom(pCommand.split(";")[1]);
        if (joueur==null)
        {
            joueur = this;
        }
        Territoire territoire = this.getRiskGOTterritoires().getTerritoireParNomStr(pCommand.split(";")[2]);
        territoire.ajouteDesTroupes(1);
        Platform.runLater(() -> mainView.mettreAJourUnTerritoireSurLaCarte(territoire));
    }







}
