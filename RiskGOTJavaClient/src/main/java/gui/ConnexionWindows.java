package gui;

import common.util.Utils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.ClientConnexion;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import static javax.swing.JOptionPane.showInputDialog;

//Fenêtre de connexion:
//Entrer l'adresse IP du server, le port, et le nom du joueur
public class ConnexionWindows extends Application {
    @Override
    public void start(Stage stage) {
        createComponents(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void createComponents(Stage primaryStage) {
        System.out.println("Je démarre, en tant que Thread" + Thread.currentThread().getName());
        primaryStage.setHeight(150);
        primaryStage.setWidth(400);
        primaryStage.setTitle("Risk Game of Thrones");
        primaryStage.setResizable(false);
        Scene scene = new Scene(new Group());
        VBox root = new VBox();
        HBox ipBox= new HBox();
        HBox nomBox= new HBox();
        Label labelNomServeur = new Label();
        labelNomServeur.setText("Adresse IP du serveur:");
        labelNomServeur.setPrefWidth(200);
        Label labelNomJoueur = new Label();
        labelNomJoueur.setPrefWidth(200);
        labelNomJoueur.setText("Nom du Joueur:");

        TextField ticNomServeur = new TextField();
        ticNomServeur.setPrefWidth(150);
        ticNomServeur.setText("127.0.0.1");
        TextField ticNomJoueur = new TextField();
        ticNomJoueur.setPrefWidth(150);
        ticNomJoueur.setText(Utils.givenUsingJava8_whenGeneratingRandomAlphabeticString_thenCorrect());


        Button btn3 = new Button();

        btn3.setText("Se connecter");
        btn3.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                ClientConnexion clientConnexion = null;
                try {
                    clientConnexion = new ClientConnexion(ticNomServeur.getText(), 7777);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Problème de connexion au serveur");
                    alert.setHeaderText(null);
                    alert.setContentText("Le serveur " + ticNomServeur.getText() + " ne répond pas sur le port 7777");
                    alert.showAndWait();
                    System.exit(0);
                }
                Thread t = new Thread(clientConnexion);
                t.start();
                clientConnexion.setNom(ticNomJoueur.getText().trim());
                MainView mv = new MainView(clientConnexion);
                clientConnexion.setMainView(mv);
                mv.start(primaryStage);


                /*TextInputDialog dialog = new TextInputDialog("127.0.0.1");
                dialog.setTitle("Adresse du serveur");
                dialog.setHeaderText("Veuillez entrer l'adresse IP du serveur");
                dialog.setContentText("Adresse IP:");
                Optional<String> nomMachine = dialog.showAndWait();

                ClientConnexion clientConnexion = null;
                try {
                    clientConnexion = new ClientConnexion(nomMachine.get(), 7777);
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

                clientConnexion.setNom(nomJoueur.get().trim());
                MainView mv = new MainView(clientConnexion);
                clientConnexion.setMainView(mv);
                mv.start(primaryStage);

            }*/
            }
        });
        ipBox.getChildren().addAll(labelNomServeur, ticNomServeur);
        nomBox.getChildren().addAll(labelNomJoueur, ticNomJoueur);
        root.getChildren().addAll(ipBox, nomBox, btn3);
        scene.setRoot(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        ticNomJoueur.selectAll();
        btn3.requestFocus();
    }



}
