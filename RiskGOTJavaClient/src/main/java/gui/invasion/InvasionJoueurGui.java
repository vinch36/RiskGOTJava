package gui.invasion;

import common.objects.DeTypeValeur;
import common.objects.Invasion;
import common.objects.Joueur;
import common.objects.Territoire;
import common.objects.cartes.CarteTerritoire;
import gui.de.DeGui;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import network.ClientConnexion;
import java.util.ArrayList;



public class InvasionJoueurGui extends HBox {


    public enum RoleTerritoire {ATTAQUANT, DEFENSEUR}

    public enum EtatInvasionJoueurGui {CHOIX_ARMEES_EN_COURS, CHOIX_ARMEE_FAIT, DE_LANCES}

    public EtatInvasionJoueurGui getEtat() {
        return etat;
    }

    private EtatInvasionJoueurGui etat;

    public ArrayList<DeGui> getDeGuis() {
        return deGuis;
    }

    private ArrayList<DeGui> deGuis;
    private HBox entete;
    private Label lblAttaquantOuDefenseur;
    private VBox labelArea;
    private ImageView imgFamille;
    private Label lblTerritoire;
    private HBox buttonArea;
    private HBox zoneNbTroupes;
    private Button btnAjouter;
    private Button btnEnlever;
    private Button btnValiderNbTroupe;
    private Button btnLancerLesDes;
    private Button btnValiderLeResultat;
    private Label lblArmeesEngagees;
    private HBox deArea;
    private HBox unitesSpecialesArea;
    private Label lblResultatPertes;
    private VBox zoneGauche;
    private VBox zoneDe;


    public Territoire getTerritoire() {
        return territoire;
    }

    private Territoire territoire;
    private Joueur joueur;
    private ClientConnexion clientClonnexion;
    private Invasion invasion;
    private boolean isMe;
    private RoleTerritoire roleTerritoire;
    private int tailleDe;
    private InvasionGui parentGui;
    private ArrayList<UniteSpecialeGui> uniteSpecialeGuiList;


    public InvasionJoueurGui(InvasionGui pParentGui, Territoire pTerritoire, ClientConnexion pClientConnexion, Invasion pInvasion, int pTailleDe) {
        this.setSpacing(20);
        this.tailleDe = pTailleDe;
        this.parentGui = pParentGui;
        this.zoneDe = new VBox();
        this.zoneGauche = new VBox();
        this.zoneGauche.setMinWidth(300);
        this.zoneDe.setSpacing(5);
        this.zoneDe.setFillWidth(true);
        this.entete = new HBox();
        this.unitesSpecialesArea = new HBox();
        this.joueur = pTerritoire.getAppartientAJoueur();
        this.lblAttaquantOuDefenseur = new Label();
        this.lblTerritoire = new Label();
        this.labelArea = new VBox();
        this.labelArea.getChildren().addAll(lblTerritoire);
        Image image = new Image(getClass().getResourceAsStream("/Maison_" + joueur.getFamille().getFamilyName().name() + "_64.png"));
        this.imgFamille = new ImageView(image);
        this.entete.getChildren().addAll(imgFamille, labelArea);
        this.lblArmeesEngagees = new Label();
        this.lblArmeesEngagees.setMinWidth(120);
        this.buttonArea = new HBox();
        this.buttonArea.setSpacing(5);
        this.btnAjouter = new Button("+");
        this.btnEnlever = new Button("-");
        this.btnValiderNbTroupe = new Button ("OK");
        this.btnAjouter.setMinWidth(50);
        this.btnEnlever.setMinWidth(50);
        this.btnValiderNbTroupe.setMinWidth(50);
        this.buttonArea.getChildren().addAll(btnEnlever, btnAjouter,btnValiderNbTroupe);
        this.deGuis = new ArrayList<>();
        this.deArea = new HBox();
        this.deArea.setSpacing(5);
        //this.deArea.setMinHeight(tailleDe + 2);
        this.btnLancerLesDes = new Button("JETER LES DES");
        this.btnLancerLesDes.setDisable(true);
        this.btnLancerLesDes.setMinWidth(230);
        this.btnValiderLeResultat = new Button("VALIDER RESULTAT");
        this.btnValiderLeResultat.setDisable(true);
        this.btnValiderLeResultat.setMinWidth(230);
        this.btnValiderNbTroupe.setTextAlignment(TextAlignment.LEFT);
        this.btnLancerLesDes.setTextAlignment(TextAlignment.LEFT);
        this.btnValiderLeResultat.setTextAlignment(TextAlignment.LEFT);
        this.clientClonnexion = pClientConnexion;
        this.isMe = (clientClonnexion == joueur);
        this.zoneNbTroupes = new HBox();
        this.zoneNbTroupes.setSpacing(5);
        this.zoneNbTroupes.getChildren().add(lblArmeesEngagees);
        if (this.isMe) {
            this.zoneNbTroupes.getChildren().add(buttonArea);
            this.btnEnlever.setDisable(false);
            this.btnEnlever.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");
            this.btnAjouter.setDisable(false);
            this.btnEnlever.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");
            this.btnValiderNbTroupe.setDisable(false);
            this.btnValiderNbTroupe.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");
        }

        this.territoire = pTerritoire;
        this.uniteSpecialeGuiList = new ArrayList<>();
        for (CarteTerritoire.UniteSpeciale uniteSpeciale:pTerritoire.getUniteSpeciales()){
            UniteSpecialeGui uniteSpecialeGui = new UniteSpecialeGui(uniteSpeciale);
            unitesSpecialesArea.getChildren().add(uniteSpecialeGui);
            uniteSpecialeGuiList.add(uniteSpecialeGui);
            uniteSpecialeGui.setCliquable(false);
        }
        this.invasion = pInvasion;
        if (invasion.getTerritoireSource() == pTerritoire) {
            roleTerritoire = RoleTerritoire.ATTAQUANT;
            lblAttaquantOuDefenseur.setText("ATTAQUANT - "+joueur.getFamille().getFamilyName().name());
        } else if (invasion.getTerritoireCible() == pTerritoire) {
            roleTerritoire = RoleTerritoire.DEFENSEUR;
            lblAttaquantOuDefenseur.setText("DEFENSEUR - " + joueur.getFamille().getFamilyName().name());
        } else {
            System.out.println("IMPOSSIBLE !!");
        }
        if (isMe){
            lblAttaquantOuDefenseur.setText(lblAttaquantOuDefenseur.getText()+"[MOI]");
        }
        this.lblResultatPertes=new Label();

        zoneGauche.getChildren().addAll(lblAttaquantOuDefenseur, entete, zoneNbTroupes, unitesSpecialesArea);
        if (roleTerritoire == roleTerritoire.ATTAQUANT) {
            if (this.isMe)
            {
                zoneDe.getChildren().addAll(btnLancerLesDes,btnValiderLeResultat);
            }
            zoneDe.getChildren().addAll(lblResultatPertes, deArea);
        }
        else{
            zoneDe.getChildren().addAll(lblResultatPertes, deArea);
            if (this.isMe)
            {
                zoneDe.getChildren().addAll(btnLancerLesDes,btnValiderLeResultat);
            }
        }
        this.getChildren().addAll(zoneGauche,zoneDe);
        etat = EtatInvasionJoueurGui.CHOIX_ARMEES_EN_COURS;
        this.initializeLabelEtBouttonsCouleurs();
    }


    private void initializeLabelEtBouttonsCouleurs() {
        this.entete.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.labelArea.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.lblArmeesEngagees.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.lblAttaquantOuDefenseur.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.lblTerritoire.setText("TROUPES:\n"+territoire.getNom().name() + " [" + territoire.getNombreDeTroupes() + "]");
        this.lblTerritoire.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        this.btnAjouter.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        this.btnEnlever.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        this.btnValiderNbTroupe.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        this.btnLancerLesDes.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        this.btnValiderLeResultat.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        this.zoneGauche.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        this.zoneDe.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        this.lblArmeesEngagees.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        //this.zoneNbTroupes.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.lblResultatPertes.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        this.lblArmeesEngagees.setMinHeight(this.btnAjouter.getHeight());

        if (roleTerritoire == RoleTerritoire.ATTAQUANT) {
            territoire.setArmeeEngagees(territoire.getMaxTroupesEngageableEnAttaque());
        } else {
            territoire.setArmeeEngagees(territoire.getMaxTroupesEngageableEnDefense());
        }
        if (this.isMe) {
            this.setUniteSpecialesCliquablesEtSelectionnees();
        }
        this.btnAjouter.setOnMouseClicked(e -> {
            this.ajouterUneArmee();
        });

        this.btnEnlever.setOnMouseClicked(e -> {
            this.enleverUneArmee();
        });
        this.btnValiderNbTroupe.setOnMouseClicked(e -> {
            this.validerNbTroupe();
        });
        this.btnLancerLesDes.setOnMouseClicked(e -> {
            this.lancerLesDe();
        });
        this.btnValiderLeResultat.setOnMouseClicked(e -> {
            this.validerLeResultat();
        });

        if (this.isMe) {
            this.refreshApresChangementNombreArmees();
        }

    }

    private void setUniteSpecialesCliquablesEtSelectionnees(){
        if (roleTerritoire==RoleTerritoire.ATTAQUANT) {
            for (UniteSpecialeGui uniteSpecialeGui : uniteSpecialeGuiList) {
                if (uniteSpecialeGui.getUniteSpeciale() != CarteTerritoire.UniteSpeciale.FORTIFICATION) {
                    uniteSpecialeGui.setCliquable(true);
                    uniteSpecialeGui.setSelectionnee(true);//Par défaut on séléctionne les unités spéciales (sauf les fortifications qui ne peuvent pas attaquer)
                }
            }
        }
        else{//En defense pas le choix; toutes les unites spéciales du joueurs sont engagées dans la bataille !
            for (UniteSpecialeGui uniteSpecialeGui : uniteSpecialeGuiList) {
                uniteSpecialeGui.setCliquable(false);
                uniteSpecialeGui.setSelectionnee(true);
            }
        }
    }

    private void setUniteSpecialesNonCliquables() {
        for (UniteSpecialeGui uniteSpecialeGui : uniteSpecialeGuiList) {
            uniteSpecialeGui.setCliquable(false);
        }
    }

    private void validerNbTroupe() {
        this.btnEnlever.setDisable(true);
        this.btnEnlever.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px;");
        this.btnAjouter.setDisable(true);
        this.btnAjouter.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px;");
        this.btnValiderNbTroupe.setDisable(true);
        this.btnValiderNbTroupe.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px;");

        this.setUniteSpecialesNonCliquables();
        etat = EtatInvasionJoueurGui.CHOIX_ARMEE_FAIT;
        for (UniteSpecialeGui uniteSpecialeGui:uniteSpecialeGuiList){
            if (uniteSpecialeGui.isSelectionnee()){
                if (uniteSpecialeGui.getUniteSpeciale()== CarteTerritoire.UniteSpeciale.FORTIFICATION){
                    territoire.setFortificationsEngagesDansLaBataille(territoire.getFortificationsEngagesDansLaBataille()+1);
                }
                if (uniteSpecialeGui.getUniteSpeciale()== CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE){
                    territoire.setEnginsDeSiegeEngagesDansLaBataille(territoire.getEnginsDeSiegeEngagesDansLaBataille()+1);
                }
                if (uniteSpecialeGui.getUniteSpeciale()== CarteTerritoire.UniteSpeciale.CHEVALIER){
                    territoire.setChevaliersEngagesDansLaBataille(territoire.getChevaliersEngagesDansLaBataille()+1);
                }

            }
        }
        parentGui.joueurAValiderSesArmees(territoire);


    }

    public void confirmationValidationResultatDesDes() {
        for (DeGui deGui : deGuis) {
            deArea.getChildren().remove(deGui);
        }
        deGuis = new ArrayList<>();
        if (roleTerritoire == RoleTerritoire.ATTAQUANT) {
            for (DeTypeValeur deTypeValeur : invasion.getResultatsDesAttaquant()) {
                DeGui de = ajouterUnDe(deTypeValeur.getTypeDe());
                de.changeValeurAvecBonus(deTypeValeur.getValeur(), deTypeValeur.getBonus());
            }
        } else {
            for (DeTypeValeur deTypeValeur : invasion.getResultatsDesDefenseur()) {
                DeGui de = ajouterUnDe(deTypeValeur.getTypeDe());
                de.changeValeurAvecBonus(deTypeValeur.getValeur(), deTypeValeur.getBonus());
            }
        }
        trierLesDes();
        regorganiserLesDes();
    }






    public void confirmationValidationNombreDarmee()
    {
        this.lblArmeesEngagees.setText(territoire.getArmeeEngagees()+" TROUPE(S)");
        this.zoneNbTroupes.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setUniteSpecialesNonCliquables();
        int nbFortificationsASelectionner=this.territoire.getFortificationsEngagesDansLaBataille();
        int nbEnginsDeSiegeASelectionner=this.territoire.getEnginsDeSiegeEngagesDansLaBataille();
        int nbChevalierASelectionner=this.territoire.getChevaliersEngagesDansLaBataille();

        for (UniteSpecialeGui uniteSpecialeGui:uniteSpecialeGuiList){
            uniteSpecialeGui.setSelectionnee(false);
            if (uniteSpecialeGui.getUniteSpeciale()== CarteTerritoire.UniteSpeciale.CHEVALIER&&nbChevalierASelectionner>0){
                nbChevalierASelectionner--;
                uniteSpecialeGui.setSelectionnee(true);
            }
            if (uniteSpecialeGui.getUniteSpeciale()== CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE&&nbEnginsDeSiegeASelectionner>0){
                nbEnginsDeSiegeASelectionner--;
                uniteSpecialeGui.setSelectionnee(true);
            }
            if (uniteSpecialeGui.getUniteSpeciale()== CarteTerritoire.UniteSpeciale.FORTIFICATION&&nbFortificationsASelectionner>0){
                nbFortificationsASelectionner--;
                uniteSpecialeGui.setSelectionnee(true);
            }
        }

    }
    public void activerLeBoutonDuLancementDeDe(){
        if (this.isMe) {
            this.btnLancerLesDes.setDisable(false);
            btnLancerLesDes.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");

        }
    }

    public void lancerLesDe() {
        etat=EtatInvasionJoueurGui.DE_LANCES;
        this.btnLancerLesDes.setDisable(true);
        this.btnLancerLesDes.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 2px;");

        int maxDuration = 0;
        int nbDes8=0;
        if (roleTerritoire==RoleTerritoire.DEFENSEUR){
            nbDes8=invasion.getNbDeHuitEnBonusAuDefenseur();
        }
        if (roleTerritoire==RoleTerritoire.ATTAQUANT){
            nbDes8=invasion.getNbDeHuitEnBonusAuAttaquant();
        }

        nbDes8 = nbDes8+ this.territoire.getEnginsDeSiegeEngagesDansLaBataille();
        for (int i = 0; i < territoire.getArmeeEngagees(); i++) {
            if (nbDes8>0) {
                this.ajouterUnDe(DeTypeValeur.TypeDe.HUIT);
                nbDes8--;
            }
            else {
                this.ajouterUnDe(DeTypeValeur.TypeDe.SIX);
            }
        }


        for (DeGui de : deGuis) {
            de.animate();
            if (de.getDureeAnimationMs()>maxDuration){
                maxDuration=de.getDureeAnimationMs();
            }
        }

        PauseTransition p = new PauseTransition(Duration.millis(maxDuration+500));
        p.setOnFinished(e -> desOntEteLances());
        p.play();
    }


    private void validerLeResultat(){
        parentGui.joueurAValideLeResultat(territoire);
        this.btnValiderLeResultat.setDisable(true);
        this.btnValiderLeResultat.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 2px;");

    }

    public void joueurConfirmeLeResultatDeLaBataille() {
        this.btnValiderLeResultat.setDisable(true);
        this.btnValiderLeResultat.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 2px;");
    }



    private void ajouterUneArmee() {
        territoire.setArmeeEngagees(territoire.getArmeeEngagees() + 1);
        this.refreshApresChangementNombreArmees();
    }

    private void enleverUneArmee() {
        territoire.setArmeeEngagees(territoire.getArmeeEngagees() - 1);
        this.refreshApresChangementNombreArmees();
    }

    private void refreshButtons() {

        //Gestion du bouton d'ajout de troupes
        if (roleTerritoire == RoleTerritoire.ATTAQUANT) {
            if (territoire.getArmeeEngagees() == territoire.getMaxTroupesEngageableEnAttaque()) {
                btnAjouter.setDisable(true);
                btnAjouter.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px;");
            } else {
                btnAjouter.setDisable(false);
                btnAjouter.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");
            }

        } else //Défenseur
        {
            if (territoire.getArmeeEngagees() == territoire.getMaxTroupesEngageableEnDefense()) {
                btnAjouter.setDisable(true);
                btnAjouter.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px;");
            } else {
                btnAjouter.setDisable(false);
                btnAjouter.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");
            }
        }
        //Gestion du bouton de retrait de troupes dans la bataille
        if (territoire.getArmeeEngagees() < 2) {
            btnEnlever.setDisable(true);
            btnEnlever.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px;");

        } else {
            btnEnlever.setDisable(false);
            btnEnlever.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");

        }
    }

    private void refreshApresChangementNombreArmees() {

        this.refreshButtons();
        this.lblArmeesEngagees.setText(territoire.getArmeeEngagees() + " TROUPE(S)");

    }



    public DeGui ajouterUnDe(DeTypeValeur.TypeDe typeDe) {
        int dureeAnim = (int) (1000 + 4000 * Math.random());
        int nbChangement = (dureeAnim / 500) + (int) ((dureeAnim / 500) * Math.random());
        DeGui.CouleurDe coul = DeGui.CouleurDe.ROUGE;
        if (roleTerritoire == RoleTerritoire.ATTAQUANT) {
            coul = DeGui.CouleurDe.NOIR;
        }

        DeGui de = new DeGui(coul, typeDe, 1, tailleDe, dureeAnim, nbChangement, this.isMe);
        this.deArea.getChildren().add(de);
        this.deGuis.add(de);
        return de;
    }

    private void desOntEteLances() {
        trierLesDes();
        //ajouterLesBonus();
        regorganiserLesDes();


        if (roleTerritoire==RoleTerritoire.ATTAQUANT) {
            invasion.resetResultatDesAttaquant();
            for (DeGui de : deGuis) {
                invasion.getResultatsDesAttaquant().add(new DeTypeValeur(de.getType(), de.getValeurCourante(), de.getBonus()));
            }
        }
        else {
            invasion.resetResultatDesDefenseur();
            for (DeGui de : deGuis) {
                invasion.getResultatsDesDefenseur().add(new DeTypeValeur(de.getType(), de.getValeurCourante(), de.getBonus()));
            }
        }
        parentGui.joueurALanceSesDes(territoire);
    }

    private void ajouterLesBonus()
    {
        DeGui deMax = deGuis.get(0);
        deMax.changeValeurAvecBonus(deMax.getValeurCourante(),territoire.getChevaliersEngagesDansLaBataille());//On ajoute le nombre de chevaliers au meilleur dé.
        for (DeGui de : deGuis) {
            de.changeValeurAvecBonus(de.getValeurCourante(), de.getBonus()+territoire.getFortificationsEngagesDansLaBataille()); // on ajoute en bonus encore les fortifications.
        }

    }

    private void trierLesDes() {
        for (int i = 0; i < deGuis.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < deGuis.size(); j++) {
                if (deGuis.get(j).getValeurCourante() >= deGuis.get(index).getValeurCourante()) {
                    index = j;
                }
            }
            DeGui max = deGuis.get(index);
            deGuis.set(index, deGuis.get(i));
            deGuis.set(i, max);
        }
    }


    private void regorganiserLesDes() {
        for (DeGui de : deGuis) {
            deArea.getChildren().remove(de);
        }

        for (DeGui de : deGuis) {
            deArea.getChildren().add(de);
        }
    }


    public void afficherResultatBataille(boolean resultatValide) {
        if (roleTerritoire == RoleTerritoire.ATTAQUANT) {
            if (invasion.getNbTroupesPerduesEnAttaque() > 0) {
                this.lblResultatPertes.setText(invasion.getNbTroupesPerduesEnAttaque() + " TROUPE(S) PERDUE(S)");
                this.lblResultatPertes.setTextFill(Color.RED);
            } else {
                this.lblResultatPertes.setText("AUCUNE PERTE !!");
                this.lblResultatPertes.setTextFill(Color.DARKGREEN);
            }
        } else {
            if (invasion.getNbTroupesPerduesEnDefense() > 0) {
                this.lblResultatPertes.setText(invasion.getNbTroupesPerduesEnDefense() + " TROUPE(S) PERDUE(S)");
                this.lblResultatPertes.setTextFill(Color.RED);
            } else {
                this.lblResultatPertes.setText("AUCUNE PERTE !!");
                this.lblResultatPertes.setTextFill(Color.DARKGREEN);
            }
        }
        if (resultatValide){
            this.lblTerritoire.setText(this.lblTerritoire.getText()+"\n-->" +  " [" + territoire.getNombreDeTroupes() + "] APRES BATAILLE");
        }
        if (!resultatValide&&this.isMe){ //on active le bouton de validation du résultat. Cela nous permet par exemple de rajouter un mestre ou jouer un personnage
            btnValiderLeResultat.setDisable(false);
            btnValiderLeResultat.setStyle("-fx-border-color: #00ff00; -fx-border-width: 2px;");
        }
    }


    public void rafraichirLeTerritoire()
    {
        if (this.isMe){
            refreshButtons();
        }
        this.lblTerritoire.setText("TROUPES:\n"+territoire.getNom().name() + " [" + territoire.getNombreDeTroupes() + "]");
        this.lblTerritoire.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
    }

    public void permettreDeMaxerUnDe()
    {
        btnValiderLeResultat.setDisable(true);
        btnValiderLeResultat.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 1px;");
        if (this.isMe) // si on est le joueur qui peut maxer son dé.
        {
            for (DeGui de : deGuis) {
                de.peutEtreMaxe();
                de.setOnMouseClicked(e -> {
                    this.maxerCeDe(de);
                });
            }
        }
    }

    private void maxerCeDe(DeGui deGui)
    {
        for (DeGui de : deGuis) {//On déscative la possiblité de cliquer sur les dés
            de.setOnMouseClicked(null);
            de.pasEnEvidence();
        }
        deGui.changeValeurAvecBonus(deGui.getMaxValue(), deGui.getBonus());
        desOntEteLances();
    }

}



