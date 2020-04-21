package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import common.ClientCommandes;
import applogic.objects.ClientConnexion;

import java.io.IOException;
import java.util.Optional;

import static javax.swing.JOptionPane.showInputDialog;

//Fenêtre de connexion:
//Entrer l'adresse IP du server, le port, et le nom du joueur
public class ConnexionWindows extends Application {
    @Override
    public void start(Stage stage ) {
        createComponents(stage);
    }

    public static void main( String[] args ) {
        launch();
    }

    private void createComponents(Stage primaryStage) {
        System.out.println("Je démarre, en tant que Thread" + Thread.currentThread().getName());
        primaryStage.setHeight(200);
        primaryStage.setWidth(400);
        primaryStage.setTitle("Risk Game of Thrones");
        primaryStage.setResizable(false);
        Scene scene = new Scene(new Group());
        VBox root = new VBox();
        //Bouton pour tester la connection au server
        Button btn3 = new Button();
        btn3.setText("Test connexion au serveur");
        btn3.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event){

                TextInputDialog dialog = new TextInputDialog("127.0.0.1");
                dialog.setTitle("Adresse du serveur");
                dialog.setHeaderText("Veuillez entrer l'adresse IP du serveur");
                dialog.setContentText("Adresse IP:");
                Optional<String> nomMachine = dialog.showAndWait();

                ClientConnexion clientConnexion = null;
                try {
                    clientConnexion = new ClientConnexion(nomMachine.get(),7777);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Problème de connexion au serveur");
                    alert.setHeaderText(null);
                    alert.setContentText("Le serveur " + nomMachine.get() + " ne répond pas sur le port 7777");
                    alert.showAndWait();
                    System.exit(0);
                }
                Thread t = new Thread(clientConnexion);
                t.start();

                TextInputDialog dialog2 = new TextInputDialog("Nom");
                dialog2.setTitle("Nom du joueur");
                dialog2.setHeaderText("Veuillez entrer votre nom");
                dialog2.setContentText("Nom:");
                Optional<String> nomJoueur = dialog2.showAndWait();

                clientConnexion.setName(nomJoueur.get().trim());
                MainView mv = new MainView(clientConnexion);
                clientConnexion.setMainView(mv);
                mv.start(primaryStage);

            }
        });
        root.getChildren().add(btn3);
        scene.setRoot(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
