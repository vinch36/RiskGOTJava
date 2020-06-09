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



public class InvasionJoueurGui extends VBox {


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
    private Label lblJoueurFamille;
    private VBox labelArea;
    private ImageView imgFamille;
    private Label lblTerritoire;
    private HBox buttonArea;
    private HBox zoneNbTroupes;
    private Button btnAjouter;
    private Button btnEnlever;
    private Button btnValiderNbTroupe;
    private Button btnLancerLesDes;
    private Label lblArmeesEngagees;
    private VBox deArea;
    private HBox unitesSpecialesArea;
    private Label lblResultatPertes;


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
        this.setMinWidth(300);
        this.setSpacing(5);
        this.tailleDe = pTailleDe;
        this.parentGui = pParentGui;
        this.entete = new HBox();
        this.unitesSpecialesArea = new HBox();
        this.joueur = pTerritoire.getAppartientAJoueur();
        this.lblAttaquantOuDefenseur = new Label();
        this.lblJoueurFamille = new Label();
        this.lblTerritoire = new Label();
        this.labelArea = new VBox();
        this.labelArea.getChildren().addAll(lblJoueurFamille, lblTerritoire);
        Image image = new Image(getClass().getResourceAsStream("/Maison_" + joueur.getFamille().getFamilyName().name() + "_80.png"));
        this.imgFamille = new ImageView(image);
        this.entete.getChildren().addAll(imgFamille, labelArea);
        this.lblArmeesEngagees = new Label();
        this.lblArmeesEngagees.setMinWidth(200);
        this.buttonArea = new HBox();
        this.buttonArea.setSpacing(5);
        this.btnAjouter = new Button("+");
        this.btnEnlever = new Button("-");
        this.btnAjouter.setMaxWidth(30);
        this.btnEnlever.setMaxWidth(30);
        this.deGuis = new ArrayList<>();
        this.deArea = new VBox();
        this.deArea.setSpacing(5);
        this.deArea.setMinHeight(3 * tailleDe + 30);
        this.btnValiderNbTroupe = new Button("1-Valider troupes");
        this.btnLancerLesDes = new Button("2-Jeter dés");
        this.btnLancerLesDes.setDisable(true);
        this.btnValiderNbTroupe.setMinWidth(230);
        this.btnLancerLesDes.setMinWidth(230);
        this.btnValiderNbTroupe.setTextAlignment(TextAlignment.LEFT);
        this.btnLancerLesDes.setTextAlignment(TextAlignment.LEFT);
        this.clientClonnexion = pClientConnexion;
        this.isMe = (clientClonnexion == joueur);
        this.zoneNbTroupes = new HBox();
        this.zoneNbTroupes.setSpacing(5);
        this.zoneNbTroupes.getChildren().addAll(lblArmeesEngagees, btnEnlever,btnAjouter);
        if (this.isMe) {
            this.btnEnlever.setDisable(false);
            this.btnAjouter.setDisable(false);
            this.btnValiderNbTroupe.setDisable(false);
        } else {
            this.btnEnlever.setDisable(true);
            this.btnAjouter.setDisable(true);
            this.btnValiderNbTroupe.setDisable(true);
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
            lblAttaquantOuDefenseur.setText("Attaquant");
        } else if (invasion.getTerritoireCible() == pTerritoire) {
            roleTerritoire = RoleTerritoire.DEFENSEUR;
            lblAttaquantOuDefenseur.setText("Défenseur");
        } else {
            System.out.println("IMPOSSIBLE !!");
        }
        this.lblResultatPertes=new Label();
        this.getChildren().addAll(lblAttaquantOuDefenseur, entete, zoneNbTroupes,unitesSpecialesArea, btnValiderNbTroupe, btnLancerLesDes, deArea, lblResultatPertes);
        etat = EtatInvasionJoueurGui.CHOIX_ARMEES_EN_COURS;
        this.initializeLabelEtBouttonsCouleurs();
    }


    private void initializeLabelEtBouttonsCouleurs() {
        this.entete.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.labelArea.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.lblArmeesEngagees.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.lblAttaquantOuDefenseur.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        this.lblJoueurFamille.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.lblTerritoire.setText(territoire.getNom().name() + " [" + territoire.getNombreDeTroupes() + "] troupes");
        this.lblTerritoire.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.btnAjouter.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.btnEnlever.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.btnValiderNbTroupe.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.btnLancerLesDes.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.entete.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        this.lblArmeesEngagees.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        this.lblArmeesEngagees.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        this.zoneNbTroupes.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.lblResultatPertes.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        this.lblArmeesEngagees.setMinHeight(this.btnAjouter.getHeight());

        if (roleTerritoire == RoleTerritoire.ATTAQUANT) {
            territoire.setArmeeEngagees(territoire.getMaxTroupesEngageableEnAttaque());
        } else {
            territoire.setArmeeEngagees(territoire.getMaxTroupesEngageableEnDefense());
        }
        if (this.isMe) {
            this.lblJoueurFamille.setText(joueur.getNomAtFamille() + " [MOI]");
            this.setUniteSpecialesCliquablesEtSelectionnees();

        } else {
            this.lblJoueurFamille.setText(joueur.getNomAtFamille());
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
        this.btnAjouter.setDisable(true);
        this.btnValiderNbTroupe.setDisable(true);
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
        if (this.isMe) {
            //RAF
        } else {
            if (roleTerritoire == RoleTerritoire.ATTAQUANT) {
                for (DeTypeValeur deTypeValeur : invasion.getResultatsDesAttaquant()) {
                    DeGui de = ajouterUnDe(deTypeValeur.getTypeDe());
                    de.changeValeurAvecBonus(deTypeValeur.getValeur(),deTypeValeur.getBonus());
                }
            } else {
                for (DeTypeValeur deTypeValeur: invasion.getResultatsDesDefenseur()) {
                    DeGui de = ajouterUnDe(deTypeValeur.getTypeDe());
                    de.changeValeurAvecBonus(deTypeValeur.getValeur(), deTypeValeur.getBonus());
                }
            }
            trierLesDes();
            //ajouterLesBonus();
            regorganiserLesDes();
        }

    }


    public void confirmationValidationNombreDarmee()
    {
        this.lblArmeesEngagees.setText(territoire.getArmeeEngagees()+" troupe(s) engagée(s)");
        this.lblArmeesEngagees.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
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
        }
    }

    public void lancerLesDe() {
        etat=EtatInvasionJoueurGui.DE_LANCES;
        this.btnLancerLesDes.setDisable(true);
        int maxDuration = 0;

        int nbDes8 = this.territoire.getEnginsDeSiegeEngagesDansLaBataille();
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
            } else {
                btnAjouter.setDisable(false);
            }

        } else //Défenseur
        {
            if (territoire.getArmeeEngagees() == territoire.getMaxTroupesEngageableEnDefense()) {
                btnAjouter.setDisable(true);
            } else {
                btnAjouter.setDisable(false);
            }
        }
        //Gestion du bouton de retrait de troupes dans la bataille
        if (territoire.getArmeeEngagees() < 2) {
            btnEnlever.setDisable(true);
        } else {
            btnEnlever.setDisable(false);
        }
    }

    private void refreshApresChangementNombreArmees() {

        this.refreshButtons();
        this.lblArmeesEngagees.setText(territoire.getArmeeEngagees() + " troupe(s)");

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
        ajouterLesBonus();
        regorganiserLesDes();

        for (DeGui de : deGuis) {
            if (roleTerritoire==RoleTerritoire.ATTAQUANT)
                invasion.getResultatsDesAttaquant().add(new DeTypeValeur(de.getType(), de.getValeurCourante(), de.getBonus()));
            else{
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


    public void afficherResultatBataille() {
        this.lblTerritoire.setText(this.lblTerritoire.getText()+"\n-->" +  " [" + territoire.getNombreDeTroupes() + "] après la bataille");
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
    }

}



