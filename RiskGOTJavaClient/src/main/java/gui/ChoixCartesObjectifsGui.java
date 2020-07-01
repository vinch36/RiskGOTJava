package gui;

import common.ClientCommandes;
import common.objects.Territoire;
import common.objects.cartes.CarteObjectif;
import common.objects.cartes.CarteTerritoire;
import common.util.SousEtat;
import gui.cartes.CarteObjectifGui;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import network.ClientConnexion;

import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.text.Font.font;

public class ChoixCartesObjectifsGui extends VBox {


    private ArrayList<CarteObjectif> cartesObjectifsList;
    private ArrayList<CarteObjectifGui> cartesObjectifsGuiList;
    private boolean demarrage;
    private ClientConnexion clientConnexion;
    private int nbCartesAChoisir;
    private Button btnValider;

    public ChoixCartesObjectifsGui(ArrayList<CarteObjectif> pCartesObjectifsList, ClientConnexion pClientConnexion, boolean pDemarrage) {
        super();
        this.clientConnexion = pClientConnexion;
        this.demarrage= pDemarrage;
        this.cartesObjectifsList = pCartesObjectifsList;
        this.nbCartesAChoisir = pCartesObjectifsList.size()-1;
        this.initialize();
    }

    private void initialize()
    {
        Label lblTitre=new Label("Choisissez "+nbCartesAChoisir+" objectifs parmis les " + cartesObjectifsList.size() + " proposés:");
        lblTitre.setFont(font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));;
        HBox carteObjectifsGuiHBox = new HBox();
        this.btnValider = new Button("OK");
        btnValider.setOnMouseClicked(e -> {
            this.clickOnValider();
        });
        this.btnValider.setDisable(true);
        this.cartesObjectifsGuiList= new ArrayList<>();
        for (CarteObjectif carte : cartesObjectifsList){
            CarteObjectifGui carteGui = new CarteObjectifGui(carte, 150,225);
            carteObjectifsGuiHBox.getChildren().add(carteGui);
            this.cartesObjectifsGuiList.add(carteGui);
            carteGui.setCliquable(true);
            carteGui.setSelectionnee(false);
            carteGui.setOnMouseClicked(e->{
                this.refreshBouttonValider();
            });
        }
        this.getChildren().addAll(lblTitre,carteObjectifsGuiHBox,btnValider);

    }

    private void refreshBouttonValider()
    {
        this.btnValider.setDisable(true);
        int compteur=0;
        for(CarteObjectifGui carteGui:this.cartesObjectifsGuiList)
        {
            if (carteGui.isSelectionnee())
            {
                compteur++;
            }
        }
        if (compteur==nbCartesAChoisir){
            this.btnValider.setDisable(false);
        }

    }


    private void clickOnValider()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmez votre choix d'objectifs");
        alert.setHeaderText("Veuillez confirmer votre selection");
        alert.setContentText("Merci de confirmer en cliquant sur OK\nVous pouvez modifier votre selection en cliquant sur Annuler");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            String message = "";
            for (CarteObjectifGui carteGui : cartesObjectifsGuiList) {
                if (!carteGui.isSelectionnee()) {
                    message = message + carteGui.getCarteObjectif().getIdAsStr() + "," + "N;";
                } else {
                    message = message + carteGui.getCarteObjectif().getIdAsStr() + "," + "Y;";
                }
            }
            message = message.substring(0, message.length() - 1);
            if (demarrage){ // Cas du choix d'objectif au démarrage
                clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_CHOISI_SES_OBJECTIFS_DEMARRAGE, message);
            }

            else {//Cas du choix de l'objectif en fin de tour, ou lors de l'achat.
                clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_CHOISI_UN_OBJECTIF, message);
            }
            this.clientConnexion.getMainView().masquerLaFenetreChoixObjectifs(this,demarrage);
        }

    }
}
