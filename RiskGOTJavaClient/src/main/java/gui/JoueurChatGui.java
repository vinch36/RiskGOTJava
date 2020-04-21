package gui;

import applogic.objects.JoueurClient;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


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
        this.nomJoueurLabel.setText(joueurClient.getName());
        if (joueurClient.isMe()) {
            this.nomJoueurLabel.setStyle("-fx-font-weight: bold");
        }

    }



}
