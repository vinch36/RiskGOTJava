package gui.manoeuvre;

import common.ClientCommandes;
import common.objects.Joueur;
import common.objects.Territoire;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import network.ClientConnexion;

public class ManoeuvreGui extends VBox {


    private Territoire territoireSource;
    private Territoire territoireCible;
    private int nbTroupesPourManoeuvre;
    private ClientConnexion clientConnexion;
    private boolean modeInvasion;
    private VBox zoneManoeuvre;
    private HBox zoneTerritoireSource;
    private HBox zoneTerritoireCible;
    private ImageView imgFamilleSource;
    private ImageView imgFamilleCible;
    private Label lblAction;
    private Label lblTerritoireSource;
    private Label lblTerritoireCible;
    private Button btnAjouter;
    private Button btnEnlever;
    private Button btnValider;
    private VBox mainContainer;
    private HBox zoneBouttons;
    private Joueur joueur;

    public ManoeuvreGui(Stage primaryStage, Territoire pTerritoireSource,Territoire pTerritoireCible, ClientConnexion pClientConnexion, boolean pModeInvasion) {
        super();
        this.clientConnexion = pClientConnexion;
        this.joueur=clientConnexion;
        this.territoireSource=pTerritoireSource;
        this.territoireCible = pTerritoireCible;
        this.modeInvasion = pModeInvasion;
        this.initialize();
    }


    private void initialize()
    {
        this.mainContainer=new VBox();
        this.zoneManoeuvre = new VBox();
        this.zoneBouttons = new HBox();
        this.zoneTerritoireSource = new HBox();
        this.zoneTerritoireCible = new HBox();
        this.lblAction = new Label("Veuillez choisir le nombre de troupes à déplacer");
        this.lblAction.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Image image = new Image(getClass().getResourceAsStream("/Maison_" + joueur.getFamille().getFamilyName().name() + "_64.png"));
        this.imgFamilleSource = new ImageView(image);
        this.imgFamilleCible= new ImageView(image);
        this.lblTerritoireSource = new Label();
        this.lblTerritoireCible = new Label();
        this.zoneTerritoireSource.getChildren().addAll(imgFamilleSource, lblTerritoireSource);
        this.zoneTerritoireCible.getChildren().addAll(imgFamilleCible, lblTerritoireCible);
        this.zoneTerritoireSource.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());
        this.zoneTerritoireCible.setStyle("-fx-background-color:" + joueur.getFamille().getWebColor());


        btnAjouter=new Button("+");

        btnAjouter.setOnMouseClicked(e -> {
            this.ajouterUneTroupe();
        });
                btnEnlever=new Button("-");
                btnEnlever.setOnMouseClicked(e -> {
                    this.enelverUneTroupe();
                });
        btnValider=new Button("Valider");
        btnValider.setOnMouseClicked(e -> {
            this.valider();
        });

        btnAjouter.setMinWidth(60);
        btnEnlever.setMinWidth(60);

        if (this.modeInvasion) {
            this.nbTroupesPourManoeuvre = 1;
        }
        else {
            this.nbTroupesPourManoeuvre = 0;
        }
        rafraichirLesBoutons();

        this.rafraichirLesLibelles();
        this.lblTerritoireSource.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.lblTerritoireCible.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.btnAjouter.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.btnEnlever.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.btnValider.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.zoneBouttons.getChildren().addAll(btnEnlever, btnAjouter);
        this.zoneManoeuvre.getChildren().addAll(zoneTerritoireSource, zoneBouttons, zoneTerritoireCible);
        this.mainContainer.getChildren().addAll(lblAction, zoneManoeuvre, btnValider);
        this.getChildren().add(mainContainer);
    }

    private void rafraichirLesLibelles()
    {
        this.lblTerritoireSource.setText(territoireSource.getNom().name()+"\n\n[" + String.valueOf(territoireSource.getNombreDeTroupes()-nbTroupesPourManoeuvre) +"] TROUPES");
        this.lblTerritoireCible.setText(territoireCible.getNom().name()+"\n\n[" + String.valueOf(territoireCible.getNombreDeTroupes()+nbTroupesPourManoeuvre) +"] TROUPES");
    }

    private void ajouterUneTroupe()
    {
        nbTroupesPourManoeuvre++;
        rafraichirLesBoutons();
        rafraichirLesLibelles();
    }

    private void enelverUneTroupe(){
        nbTroupesPourManoeuvre--;
        rafraichirLesBoutons();
        rafraichirLesLibelles();
    }

    private void valider(){
        if (this.modeInvasion) {
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_EFFECTUE_UNE_MANOEUVRE_EN_FIN_DINVASION, String.valueOf(nbTroupesPourManoeuvre));
        }
        else{
            this.clientConnexion.getManoeuvreEnCours().setNbTroupes(nbTroupesPourManoeuvre);
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_EFFECTUE_UNE_MANOEUVRE, clientConnexion.getNom() + ";" + clientConnexion.getManoeuvreEnCours().getTerritoireSource().getNom().name() + ";" + clientConnexion.getManoeuvreEnCours().getTerritoireCible().getNom().name() + ";" + clientConnexion.getManoeuvreEnCours().getNbTroupes());
        }
        this.close();
    }

    private void close()
    {
        clientConnexion.getMainView().masquerLaFenetreManoeuvre(this, modeInvasion);
    }


    private void rafraichirLesBoutons() {
        //Gestion du bouton d'ajout de troupes dans la manoeuvre

        if (nbTroupesPourManoeuvre < this.territoireSource.getNombreDeTroupes() - 1) {
            btnAjouter.setDisable(false);
        } else {
            btnAjouter.setDisable(true);
        }

        //Gestion du bouton de retrait de troupes dans la manoeuvre
        if (this.modeInvasion) {
            if (nbTroupesPourManoeuvre < 2) {
                btnEnlever.setDisable(true);
            } else {
                btnEnlever.setDisable(false);
            }
        }
        else{
            if (nbTroupesPourManoeuvre < 1) {
                btnEnlever.setDisable(true);
            } else {
                btnEnlever.setDisable(false);
            }
        }
    }

}
