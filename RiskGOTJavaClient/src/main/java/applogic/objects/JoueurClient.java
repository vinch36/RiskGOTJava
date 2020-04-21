package applogic.objects;
import common.ClientCommandes;
import common.Famille;
import common.Familles;
import gui.MainView;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;

public class JoueurClient {

    private ArrayList<JoueurClient> adversaires;
    private Familles riskGOTFamilles;

    public Famille getMaFamille() {
        return maFamille;
    }

    public void setMaFamille(Famille maFamille) {
        this.maFamille = maFamille;
    }

    private Famille maFamille;
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


    public boolean isTousConnectes() {
        return tousConnectes;
    }

    public void setTousConnectes(boolean tousConnectes) {
        this.tousConnectes = tousConnectes;
    }

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
        this.name = "INCONNU";
        this.nbJoueurs=1;
        isMe=true;

    }

    public JoueurClient(String pNom) {
        this.name = pNom;
        isMe = false;
    }


    public ArrayList<JoueurClient> getAdversaires() {
        return adversaires;
    }

    public void setAdversaires(ArrayList<JoueurClient> adversaires) {
        this.adversaires = adversaires;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    protected void updateChatText(String txt) {
        Platform.runLater(() -> {
            //an event with a button maybe
            this.mainView.updateTxt(txt);
        });
    }

    protected void commandeWelcome(String pCommande)
    {
        nbJoueursAttendus = Integer.parseInt(pCommande.split(";")[1]);
        this.riskGOTFamilles = new Familles(nbJoueursAttendus);
        this.updateChatText("BIENVENUE [" + this.name + "] SUR RISK GOT ONLINE !!");
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
        if (!joueurName.equals(this.name)) {
            JoueurClient joueurClient = new JoueurClient(joueurName);
            this.getAdversaires().add(joueurClient);
            nbJoueurs++;
            Platform.runLater(() -> mainView.nouveauJoueurConnecte(joueurClient));
        }
    }

    protected void commandeTousConnecte(String pCommande) {

        tousConnectes=true;

        this.updateChatText("ACTION: Veuillez lancer un dés pour savoir qui pourra choisir en premier la première Famille.\nLe plus grand dé pourra choisir sa famille en premier.\nEn cas d'éaglité, l'heure de connection prime (le premier connecté aura la priorité !)");
        Platform.runLater(() -> mainView.PretALancerUnDes());
    }

    protected void commandeFaireChoixFamille(String pCommande)
    {
        //le message a la forme "FAIRE_CHOIX_FAMILLE;Fam1;Fam2;Fam3)
        String listeFamilelDispo = pCommande.replaceAll(ClientCommandes.FAIRE_CHOIX_FAMILLE.name()+";", "");

        Platform.runLater(() -> mainView.faireChoixFamille(listeFamilelDispo));
    }

    protected void commandeMiseAJourFamilleJoueur(String pCommande)
    {
        //le message a la forme "JOUEUR_A_FAIT_CHOIX_FAMILLE;NOM_JOUEUR;NOM_FAMILLE)
        String nomJoueur = pCommande.split(";")[1];
        String nomFamille= pCommande.split(";")[2];
        JoueurClient joueurClient = this.getAversaireParNom(nomJoueur);
        Famille famille = riskGOTFamilles.getFamilleParNom(nomFamille);
        joueurClient.setMaFamille(famille);
        Platform.runLater(() -> this.mainView.refreshFamilleJoueur(famille, joueurClient));

    }

    private JoueurClient getAversaireParNom(String pNom)
    {
     for (JoueurClient j : adversaires){
         if (pNom.equals(j.name)){
             return j;
         }

     }
     return null;
    }




}
