package gui.de;

import common.ClientCommandes;
import common.objects.Joueur;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.ClientConnexion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Thread.sleep;

public class LancerLesDeDemarrage extends Pane {

    private ClientConnexion clientConnexion;
    private  HashMap<Joueur, ArrayList<DeGui>> tableJoueurDe;
    private boolean demandeRelanceSurEgalite;
    private HBox zoneDesDes;
    private VBox mainContainer;

    public LancerLesDeDemarrage(ClientConnexion clientConnexion, boolean pDemandeRelanceSurEgalite){
        super();
        initialize(clientConnexion,pDemandeRelanceSurEgalite);

    }

    private void initialize(ClientConnexion clientConnexion, boolean pDemandeRelanceSurEgalite)
    {

        this.clientConnexion=clientConnexion;
        this.demandeRelanceSurEgalite = pDemandeRelanceSurEgalite;
        this.tableJoueurDe = new HashMap<>();
        zoneDesDes=new HBox();
        mainContainer=new VBox();
        zoneDesDes.setSpacing(5);


    }

    public void createComponents()
    {
        Label lblTitre= new Label();
        this.mainContainer.setSpacing(10);
        zoneDesDes.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        lblTitre.setText("Lancez votre dé (rouge) pour l'ordre du choix des familles !");
        lblTitre.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.mainContainer.getChildren().add(lblTitre);
        for (Joueur j : tableJoueurDe.keySet()){
            VBox v = new VBox();
            Label label = new Label();
            label.setText(j.getNom());
            v.getChildren().add(label);
            if (j==clientConnexion){
                label.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
            }
            for (DeGui deGui:tableJoueurDe.get(j)){
                v.getChildren().add(deGui);
            }
            zoneDesDes.getChildren().add(v);
        }
        this.mainContainer.getChildren().add(zoneDesDes);
        this.getChildren().add(mainContainer);
    }

    public void ajouterUnDe(DeGui de, Joueur j)
    {

        if (!tableJoueurDe.containsKey(j)){
            ArrayList<DeGui> deGuis = new ArrayList<>();
            deGuis.add(de);
            tableJoueurDe.put(j,deGuis);
            if (de.isDoitEtreLance()){
                de.mettreEnEvidence();
                de.setOnMouseClicked((MouseEvent event) -> {
                    this.lancerUnDe(de);
                });
            }
        }
        else
        {
            tableJoueurDe.get(j).add(de);
        }
    }


    public void afficherUnResultat(DeGui de, int nouvelleValeur)
    {
        de.changeValeur(nouvelleValeur);
        de.aJoue();
        checkAllDeOnJoue();
    }

    private void checkAllDeOnJoue()
    {
        boolean tousJoue = true;
        for (Joueur j : tableJoueurDe.keySet()) {
            for (DeGui deGui : tableJoueurDe.get(j)) {
                if (!deGui.isaEteLance()){
                    tousJoue=false;
                }
            }
        }

        if (tousJoue) {
            this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        }
    }



    public void animerUnDe(DeGui de)
    {
        de.animate();
    }


    private DeGui getPlusGrandDe()
    {
        DeGui plusGrandDe=null;
        for (Joueur j : tableJoueurDe.keySet())
        {
            for (DeGui deGui:tableJoueurDe.get(j))
            {
                if (plusGrandDe==null)
                {
                    plusGrandDe=deGui;
                }
                if (deGui.getTaille()>plusGrandDe.getTaille()){
                    plusGrandDe=deGui;
                }
            }
        }
        return plusGrandDe;

    }

    private void lancerUnDe(DeGui de)
    {
        if (de.isDoitEtreLance()) {
            de.animate();
            PauseTransition p = new PauseTransition(Duration.millis(de.getDureeAnimationMs()+500));
            p.setOnFinished(e -> finAnimation(de));
            p.play();
        }
    }

    private void finAnimation(DeGui de)
    {
        if (de == obtenirUnDe(clientConnexion,0)){
            de.mettreEnEvidence();
        }
        else{
            de.pasEnEvidence();
        }
        clientConnexion.sendCommand(ClientCommandes.LANCE_1DE_START, String.valueOf(de.getValeurCourante()));
    }

    public DeGui obtenirUnDe(Joueur j, int index)
    {
        return tableJoueurDe.get(j).get(index);
    }


}
