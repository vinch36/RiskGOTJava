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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.ClientConnexion;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class LancerLesDeDemarrage extends VBox {





    private ClientConnexion clientConnexion;
    private  HashMap<Joueur, ArrayList<DeGui>> tableJoueurDe;
    private boolean demandeRelanceSurEgalite;
    VBox mainContainer;





    public LancerLesDeDemarrage(ClientConnexion clientConnexion, boolean pDemandeRelanceSurEgalite){
        super();
        initialize(clientConnexion,pDemandeRelanceSurEgalite);

    }

    private void initialize(ClientConnexion clientConnexion, boolean pDemandeRelanceSurEgalite)
    {

        this.clientConnexion=clientConnexion;
        this.demandeRelanceSurEgalite = pDemandeRelanceSurEgalite;
        mainContainer=new VBox();

        this.tableJoueurDe = new HashMap<>();

    }


    public  void createComponents()
    {


        int taille = getPlusGrandDe().getTaille();


        int maxNbDe = 0;
        for (Joueur j : tableJoueurDe.keySet()){
            HBox h = new HBox();
            Label label = new Label();
            label.setText(j.getNom());
            int nbDe=0;
            for (DeGui deGui:tableJoueurDe.get(j)){
                h.getChildren().add(deGui);
                nbDe++;
            }
            if (nbDe>maxNbDe)
            {
                maxNbDe=nbDe;
            }
            h.getChildren().add(label);
            this.mainContainer.getChildren().add(h);
        }
        Label lblTitre= new Label();
        lblTitre.setText("Lancez votre dé pour savoir\n dans quel ordre s'effectuera\n le choix des familles !");
        this.getChildren().add(mainContainer);
        this.setHeight((taille+20)*tableJoueurDe.size()+40);
        this.setWidth((taille+20)*maxNbDe+150);
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
