package gui;

import applogic.objects.JoueurClient;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class JoueurChatGui extends VBox
{
    private Label nomJoueurLabel;
    private Image imageConnectionOff;
    private Image imageConnectionOn;

    public JoueurClient getJoueurClient() {
        return joueurClient;
    }

    private JoueurClient joueurClient;

    public JoueurChatGui(){
        super();
        nomJoueurLabel = new Label();
        imageConnectionOff = new Image(getClass().getResourceAsStream("/img/Connexion_red.png"));
        imageConnectionOn = new Image(getClass().getResourceAsStream("/img/Connexion_green.png"));
        nomJoueurLabel.setText("Attente joueur...");
        nomJoueurLabel.setGraphic(new ImageView(imageConnectionOff));

        this.getChildren().add(nomJoueurLabel);
    }

    public void setConnected()
    {
        this.nomJoueurLabel.setGraphic(new ImageView(imageConnectionOn));
    }

    public void setJoueurClient(JoueurClient joueurClient)
    {
        this.joueurClient=joueurClient;
        this.nomJoueurLabel.setTextFill(Color.WHITE);
        this.nomJoueurLabel.setStyle("-fx-background-color: black;");
        this.nomJoueurLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 10));
        if (joueurClient.isMe()) {
            this.nomJoueurLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
        }
        this.nomJoueurLabel.setText(joueurClient.getNom());

    }



}
