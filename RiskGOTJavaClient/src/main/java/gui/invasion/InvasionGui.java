package gui.invasion;

import common.ClientCommandes;
import common.objects.DeTypeValeur;
import common.objects.Invasion;
import common.objects.Territoire;
import gui.manoeuvre.ManoeuvreGui;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.ClientConnexion;

import static common.ClientCommandes.JOUEUR_ATTAQUANT_REALISE_SA_DEFAITE;
import static java.lang.Thread.sleep;

public class InvasionGui extends VBox {


    private Label lblTitre;
    private HBox hBoxInvasionJoueurGuis;
    private Button btnClose;
    private VBox mainContainer;
    private VBox zoneResultatInvasion;
    private HBox zoneBouttons;
    private Button btnArreterInvasion;
    private Button btnContinuerInvasion;
    private Invasion invasion;
    private ClientConnexion clientConnexion;
    private Stage primaryStage;
    private Label lblStatutInvasion;

    public InvasionJoueurGui getJoueurAttaquantGui() {
        return joueurAttaquantGui;
    }

    public InvasionJoueurGui getJoueurDefenseurGui() {
        return joueurDefenseurGui;
    }

    private InvasionJoueurGui joueurAttaquantGui;
    private InvasionJoueurGui joueurDefenseurGui;

    public InvasionGui(Stage primaryStage, Invasion pInvasion, ClientConnexion pClientConnexion) {
        super();
        //this.setOnCloseRequest(e->e.consume());
        this.invasion = pInvasion;
        this.clientConnexion = pClientConnexion;
        this.primaryStage = primaryStage;
        initialize();
    }


    private void initialize() {

        this.lblTitre = new Label();
        this.zoneResultatInvasion = new VBox();
        this.zoneResultatInvasion.setMinHeight(80);
        this.mainContainer = new VBox();
        this.hBoxInvasionJoueurGuis = new HBox();
        this.zoneBouttons = new HBox();
        this.zoneBouttons.setSpacing(5);
        this.lblStatutInvasion = new Label();
        this.lblStatutInvasion.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.lblStatutInvasion.setText("\n\nLes armées se préparent !!\n\n");
        this.lblTitre.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 22));
        //Scene scene = new Scene(mainContainer);
        //this.setScene(scene);
        //this.initModality(Modality.WINDOW_MODAL);
        //this.setResizable(false);
        this.getChildren().add(mainContainer);
        btnClose = new Button();
        btnClose.setText("Fermer la fenêtre");
        btnClose.setDisable(true);
        btnClose.setOnMouseClicked(e -> {
            this.close();
        });
        btnArreterInvasion = new Button();
        btnArreterInvasion.setText("Arrêter l'invasion");
        btnArreterInvasion.setDisable(true);
        btnArreterInvasion.setOnMouseClicked(e -> {
            this.arreteLinvasion();
        });

        btnContinuerInvasion = new Button();
        btnContinuerInvasion.setText("Continuer l'invasion");
        btnContinuerInvasion.setDisable(true);
        btnContinuerInvasion.setOnMouseClicked(e -> {
            this.continueLinvasion();
        });

        btnContinuerInvasion.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        btnArreterInvasion.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        btnClose.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));


        zoneBouttons.setSpacing(5);
        zoneBouttons.getChildren().addAll(btnClose, btnContinuerInvasion, btnArreterInvasion);

        if (this.clientConnexion == this.invasion.getTerritoireSource().getAppartientAJoueur()) {
            this.setTitle("INVASION - VUE ATTAQUANT");
            this.joueurAttaquantGui = new InvasionJoueurGui(this, invasion.getTerritoireSource(), clientConnexion, invasion, 90);
            this.joueurDefenseurGui = new InvasionJoueurGui(this, invasion.getTerritoireCible(), clientConnexion, invasion, 70);
        } else if (this.clientConnexion == this.invasion.getTerritoireCible().getAppartientAJoueur()) {
            this.setTitle("INVASION - VUE DEFENSEUR");
            this.joueurAttaquantGui = new InvasionJoueurGui(this, invasion.getTerritoireSource(), clientConnexion, invasion, 70);
            this.joueurDefenseurGui = new InvasionJoueurGui(this, invasion.getTerritoireCible(), clientConnexion, invasion, 90);
        } else {
            this.setTitle("INVASION - VUE SPECTATEUR");
            this.joueurAttaquantGui = new InvasionJoueurGui(this, invasion.getTerritoireSource(), clientConnexion, invasion, 70);
            this.joueurDefenseurGui = new InvasionJoueurGui(this, invasion.getTerritoireCible(), clientConnexion, invasion, 70);
        }

        hBoxInvasionJoueurGuis.getChildren().addAll(joueurAttaquantGui, joueurDefenseurGui);
        zoneResultatInvasion.getChildren().add(lblStatutInvasion);
        mainContainer.getChildren().addAll(lblTitre, hBoxInvasionJoueurGuis, zoneResultatInvasion, zoneBouttons);
    }


    private void setTitle(String s)
    {
        this.lblTitre.setText(s);

    }

    private void close(){
        this.clientConnexion.getMainView().masquerLaFenetreInvasion();
        if (this.defaiteAttaquant&&clientConnexion==invasion.getJoueurAttaquant())
        {
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_ATTAQUANT_REALISE_SA_DEFAITE,"");
        }
    }

    private void continueLinvasion(){
        clientConnexion.sendCommand(ClientCommandes.JOUEUR_CONTINUE_INVASION,"");
        this.close();
    }

    private void arreteLinvasion(){
        clientConnexion.sendCommand(ClientCommandes.JOUEUR_ARRETE_UNE_INVASION,"");
        this.close();
    }


    public void joueurAValiderSesArmees(Territoire ter) {

        if (ter == invasion.getTerritoireSource()) {
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_ATTAQUE, ter.getArmeesEngageesStringMsg());
        } else {
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_DEFENSE, ter.getArmeesEngageesStringMsg());
        }
    }


    public void joueurALanceSesDes(Territoire ter) {
        String message = "";
        if (ter == invasion.getTerritoireSource()) {
            for (DeTypeValeur deTypeValeur : invasion.getResultatsDesAttaquant()) {
                message = message + deTypeValeur.getTypeDe().name() + ","+ deTypeValeur.getValeur() + ","+deTypeValeur.getBonus() + ";";
            }
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_LANCE_LES_DES_EN_ATTAQUE, message.substring(0, message.length() - 1));

        } else {
            for (DeTypeValeur deTypeValeur: invasion.getResultatsDesDefenseur()) {
                message = message + deTypeValeur.getTypeDe().name() + ","+ deTypeValeur.getValeur() + ","+deTypeValeur.getBonus() + ";";
            }
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_LANCE_LES_DES_EN_DEFENSE, message.substring(0, message.length() - 1));
        }
    }


    public void pretALancerLesDes() {
        joueurDefenseurGui.activerLeBoutonDuLancementDeDe();
        joueurAttaquantGui.activerLeBoutonDuLancementDeDe();
        this.lblStatutInvasion.setText("\nLes armées sont prêtes ! \nVeuillez lancer les dès !");
    }

    public void mettreEnEvidenceLesDesVainqueurs() {
        this.lblStatutInvasion.setText("\nLa bataille est terminée\n");
        int maxDeAComparer = Math.min(joueurAttaquantGui.getTerritoire().getArmeeEngagees(), joueurDefenseurGui.getTerritoire().getArmeeEngagees());
        System.out.println("DEBUG --> MAX_DE_A_COMPARER = " + maxDeAComparer);
        for (int i = 0; i < maxDeAComparer; i++) {
            if (joueurAttaquantGui.getDeGuis().get(i).getValeurAvecBonus() > joueurDefenseurGui.getDeGuis().get(i).getValeurAvecBonus()) {
                joueurAttaquantGui.getDeGuis().get(i).deVainqueur();
                joueurDefenseurGui.getDeGuis().get(i).dePerdant();
            } else {
                joueurAttaquantGui.getDeGuis().get(i).dePerdant();
                joueurDefenseurGui.getDeGuis().get(i).deVainqueur();
            }
        }
        joueurAttaquantGui.afficherResultatBataille();
        joueurDefenseurGui.afficherResultatBataille();
    }


    public void invasionTermineeDefaiteDefenseur(String message) {
        this.lblStatutInvasion.setText(message);
        if (invasion.getJoueurAttaquant()==clientConnexion){
            //Je suis l'attaquant, et j'ai gagné
            this.btnClose.setText("Manoeuvrer");
            btnClose.setOnMouseClicked(e -> {
                this.fermerEtDemanderNombreDeTroupePourManoeuvre();
            });
        }
        else{
            //Je suis le défenseur ou un spectateur
            this.btnClose.setText("Fermer la fenêtre");
            btnClose.setOnMouseClicked(e -> {
                this.close();
            });
        }
        this.btnClose.setDisable(false);
    }

    private void fermerEtDemanderNombreDeTroupePourManoeuvre()
    {
        this.close();
        clientConnexion.getMainView().afficherUneFenetreManoeuvre(invasion.getTerritoireSource(), invasion.getTerritoireCible(), true);
    }

    public void invasionTermineeDefaiteAttaquant(String message) {
        this.lblStatutInvasion.setText(message);
        this.btnClose.setDisable(false);
        this.defaiteAttaquant=true;
        btnClose.setOnMouseClicked(e -> {
            this.close();
        });

    }

    private boolean defaiteAttaquant = false;

    public void invasionPeutContinuer(String message){
        this.lblStatutInvasion.setText(message);
        if (invasion.getJoueurAttaquant()==clientConnexion) {
            this.btnContinuerInvasion.setDisable(false);
            this.btnArreterInvasion.setDisable(false);
        }
        else {
            this.btnClose.setDisable(false);
        }
    }
}
