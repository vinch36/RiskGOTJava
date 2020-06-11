package gui;

import common.ClientCommandes;
import common.objects.Territoire;
import common.objects.cartes.CarteObjectif;
import common.objects.cartes.CarteTerritoire;
import gui.cartes.CarteObjectifGui;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.ClientConnexion;

import java.util.ArrayList;
import java.util.Optional;

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
        this.demarrage = pDemarrage;
        this.cartesObjectifsList = pCartesObjectifsList;
        this.nbCartesAChoisir = pCartesObjectifsList.size()-1;
        this.initialize();
    }

    private void initialize()
    {
        Label lblTitre=new Label("Choisissez "+nbCartesAChoisir+" objectifs parmis les " + cartesObjectifsList.size() + " proposés:");
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
        if (demarrage){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmez votre choix d'objectifs");
            alert.setHeaderText("Veuillez confirmer votre selection");
            alert.setContentText("Merci de confirmer en cliquant sur OK\nVous pouvez modifier votre selection en cliquant sur Annuler");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                String message="";
                for (CarteObjectifGui carteGui : cartesObjectifsGuiList){
                    if (!carteGui.isSelectionnee()){
                        message = message+carteGui.getCarteObjectif().getIdAsStr()+","+"N;";
                    }
                    else{
                        message = message+carteGui.getCarteObjectif().getIdAsStr()+","+"Y;";
                    }
                }
                message = message.substring(0,message.length()-1);
                clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_CHOISI_SES_OBJECTIFS_DEMARRAGE, message);
                this.clientConnexion.getMainView().masquerLaFenetreChoixObjectifs(this,demarrage);
            }

        }
        else{ // On es dans le cas ou on achète un objectif ou on choisi un objectif en fin de tour.
            //TODO

        }
    }
}
