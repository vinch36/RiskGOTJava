package gui;

import applogic.objects.JoueurClient;
import common.Famille;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class FamilleGui extends VBox {
    public Famille getFamille() {
        return famille;
    }

    private Famille famille;
    private ImageView imgView;
    private MainView mainView;
    private Label nomFamille = new Label();
    private Label nomJoueur = new Label();

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    private boolean isMe;

    public JoueurClient getJoueurClient() {
        return joueurClient;
    }

    public void setJoueurClient(JoueurClient joueurClient) {
        this.joueurClient = joueurClient;
        this.nomJoueur.setText(joueurClient.getName());
        if (!isMe){
            this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        }
    }

    private JoueurClient joueurClient;

    public FamilleGui(Famille pFam, MainView pMainView)
    {
        this.mainView=pMainView;
        this.famille = pFam;
        Image image = new Image(getClass().getResourceAsStream("/img/Maison_"+pFam.getFamilyName().name()+".png"));
        imgView = new ImageView(image);
        imgView.setDisable(true);
        isMe=false;
        nomJoueur.setText("?");
        nomJoueur.setStyle("-fx-font-weight: bold");
        nomFamille.setText(pFam.getFamilyName().name());
        nomFamille.setStyle("-fx-font-weight: bold");
        this.getChildren().addAll(nomJoueur,imgView,nomFamille);
        this.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        imgView.setOnMouseClicked(e->{
            clickOnImageView();
        });

    }

    public void highlight(){
        this.setBorder(new Border(new BorderStroke(Color.LIGHTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        this.imgView.setCursor(Cursor.HAND);
        this.imgView.setDisable(false);
    }

    public void unHighLight(){
        if (this.joueurClient==null)
        {
            this.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        }
        this.imgView.setCursor(Cursor.DEFAULT);
        this.imgView.setDisable(true);

    }

    private  void clickOnImageView() {
        if (!imgView.isDisable()) {
            imgView.setDisable(true);
            this.imgView.setCursor(Cursor.DEFAULT);
            this.isMe=true;
            mainView.aFaitChoixFamille(famille);
        }

    }



}
