package gui;
import applogic.CommandesConsoleClient;
import applogic.objects.JoueurClient;
import common.ClientCommandes;
import common.objects.Famille;
import common.objects.Familles;
import common.objects.Territoire;
import common.util.Etat;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import network.ClientConnexion;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static javafx.concurrent.Worker.State.SUCCEEDED;


public class MainView {

    public Etat getEtatPrincipal() {
        return etatPrincipal;
    }

    public boolean isCarteCliquable() {
        return carteCliquable;
    }

    private boolean carteCliquable;
    private Etat etatPrincipal;
    private WebView webView;
    private VBox playerZone;
    private VBox listeDesJoueursChatZone;
    private VBox actionZone;
    private VBox chatZone;
    private VBox familleZone;

    public ClientConnexion getClientConnexion() {
        return clientConnexion;
    }

    private ClientConnexion clientConnexion;
    private TextField txtInput;
    private ScrollPane scrollPane;
    private TextArea txtAreaDisplay;
    private Button btn1De;
    private Stage primaryStage;
    private Button btnFireJS;
    private HashMap<Famille.FamilyNames,FamilleGui> familleGuiHashMap;

    public MainView(ClientConnexion pClientConnexion){
        this.clientConnexion = pClientConnexion;
    }

    public void start(Stage stage ) {
        this.primaryStage=stage;
        this.carteCliquable=false;
        createComponents();

    }

    private void waitForAllConnections()
    {
        etatPrincipal = Etat.ATTENTE_CONNECTION_JOUEURS;

    }

    public void zoom (ScrollEvent event) {
        if (event.isControlDown()) {
            double currentZoomValue = ((WebView) event.getSource()).getZoom();
            double delta = event.getDeltaY() / 40 / 20;
            if (delta > 0 && currentZoomValue < 2 || delta < 0 && currentZoomValue > 0.25) {
                ((WebView) event.getSource()).setZoom(currentZoomValue + delta);
            }
        }
    }


    private void createComponents() {
        System.out.println("Je cree les composants de la MainView en tant que Thread " + Thread.currentThread().getName());
        primaryStage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().height-100);
        primaryStage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().width-10);
        primaryStage.setX(5);
        primaryStage.setY(5);
        primaryStage.setTitle("Risk Game of Thrones");
        Scene scene = new Scene(new Group());
        HBox root2 = new HBox();
        //Création de la zone joueur
        this.playerZone = new VBox();
        playerZone.setStyle("-fx-border-color: black");
        playerZone.setMinWidth(500);
        //playerZone.setFillWidth(true);
        root2.getChildren().add(playerZone);



        //Création de la WebView pour la carte de GOT
        this.webView = new WebView();
        this.webView.setStyle("-fx-border-color: black");
        this.webView.prefHeightProperty().bind(primaryStage.heightProperty());
        this.webView.setPrefWidth(primaryStage.getWidth()-playerZone.getWidth());
        this.webView.getEngine().load(getClass().getResource("/Index.html").toString());
        this.webView.setOnScroll(event -> zoom(event));
        this.webView.setZoom(0.69);
        this.webView.getEngine().setJavaScriptEnabled(true);





        //Mise en place de la JavaScriptInterface pour pouvoir appeler du Java (dans la classe JavaScriptInterface depuis JavaScript
        JavaScriptInterface JSI = new JavaScriptInterface(this);
        webView.getEngine().getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {

                        if (newState == SUCCEEDED) {

                            JSObject jsobject = (JSObject) webView.getEngine().executeScript("window");
                            jsobject.setMember("myJavaMember", JSI);
                        }
                    }
                });
        root2.getChildren().add(this.webView);

        //Creation de la zone pour la liste des joueurs avec statut dans le chat
        this.listeDesJoueursChatZone = new VBox();
        listeDesJoueursChatZone.setStyle("-fx-border-color: black");
        playerZone.getChildren().add(listeDesJoueursChatZone);

        //zone de Chat
        chatZone = createComponentsChat();
        chatZone.setPrefHeight(400);
        chatZone.setFillWidth(true);
        chatZone.setStyle("-fx-border-color: black");
        playerZone.getChildren().add(chatZone);


        //zone des familles (invisible au début)
        this.familleZone = new VBox();
        familleZone.setStyle("-fx-border-color: black");
        playerZone.getChildren().add(familleZone);


        actionZone = new VBox();
        actionZone.setStyle("-fx-border-color: black");
        actionZone.setFillWidth(true);
        playerZone.getChildren().add(actionZone);


        scene.setRoot(root2);

        createTestButtons(actionZone);
        primaryStage.setScene(scene);
        primaryStage.show();
        new CommandesConsoleClient(clientConnexion); // lance le thread de gestion des commandes
        clientConnexion.sendCommand(ClientCommandes.SEND_NAME,clientConnexion.getNom());
        this.waitForAllConnections();


    }

    public void  createComponentsZoneListeJoueurs(int nbJoueursAttendus)
    {

        Label label = new Label();
        label.setText("Joueurs Connectés");
        this.listeDesJoueursChatZone.getChildren().add(label);
        this.nouveauJoueurConnecte(this.clientConnexion);

    }

    public void createComponentsFamilles(Familles pFams)
    {
        this.familleGuiHashMap = new HashMap<>();
        for (Famille f : pFams.getFamillesActives()){
            FamilleGui famGui = new FamilleGui(f, this);
            this.familleZone.getChildren().add(famGui);
            this.familleGuiHashMap.put(f.getFamilyName(),famGui);


        }
    }




    private void createTestButtons(VBox menu) {
        //Bouton pour appler une fonction JavaScript
        btnFireJS = new Button();
        btnFireJS.setText("fire JS");
        btnFireJS.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (webView.getEngine() != null)
                {
                    sendToJVS("testInitAll()");
                    sendToJVS("RafraichirTousLesTerritoires()");
                }
            }
        });
        menu.getChildren().add(btnFireJS);

        Button btn2 = new Button();
        btn2.setText("Afficher les règles du jeu");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    File pdfFile = new File(getClass().getResource("/Regle_du_jeu_risk_game_of_thrones.pdf").getFile());
                    if (pdfFile.exists()) {

                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(pdfFile);
                        } else {
                            System.out.println("Awt Desktop is not supported!");
                        }

                    } else {
                        System.out.println("File is not exists!");
                    }

                    System.out.println("Done");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        menu.getChildren().add(btn2);

        Button btn3 = new Button();
        btn3.setText("Recharger La Carte");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                webView.getEngine().load(getClass().getResource("/Index.html").toString());
                webView.getEngine().setJavaScriptEnabled(true);
                rafraichirTousLesTerritoiresSurLaCarte();
            }
        });
        menu.getChildren().add(btn3);



    }


    public VBox createComponentsChat() {
        VBox vBox = new VBox();

        scrollPane = new javafx.scene.control.ScrollPane();   //pane to display text messages
        HBox hBox = new HBox(); //pane to hold input textfield and send button
        Label label = new Label();
        label.setStyle("-fx-font-weight: bold");
        label.setText("CHAT - ["+this.clientConnexion.getNom()+"]");

        txtAreaDisplay = new javafx.scene.control.TextArea();
        txtAreaDisplay.setEditable(false);
        scrollPane.setContent(txtAreaDisplay);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        //define textfield and button and add to hBox

        txtInput = new TextField();
        txtInput.setPromptText("Nouveau message");
        txtInput.setTooltip(new Tooltip("Ecrivez ici puis cliquez sur envoyer. "));
        Button btnSend = new Button("Envoyer");
        btnSend.setOnAction((EventHandler<ActionEvent>) new MainView.NewChatListener());
        txtInput.setOnAction((EventHandler<ActionEvent>) new MainView.NewChatListener());
        hBox.getChildren().addAll(txtInput, btnSend);
        hBox.setHgrow(txtInput, Priority.ALWAYS);  //set textfield to grow as window size grows

        //set center and bottom of the borderPane with scrollPane and hBox
        vBox.getChildren().addAll(label,scrollPane, hBox);
        vBox.setVgrow(scrollPane, Priority.ALWAYS);

        return vBox;

    }

    public void updateTxt(String txt) {
        txtAreaDisplay.appendText(txt + "\n");

    }

    /**
     * Action d 'envoie d'un message dans le chat
     */
    private class NewChatListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            //get username and message
            String username = clientConnexion.getNom().trim();
            String message = txtInput.getText().trim();

            //if username is empty set it to 'Unknown'
            if (username.length() == 0) {
                username = "?";
            }
            //if message is empty, just return : don't send the message
            if (message.length() == 0) {
                return;
            }

            //send message to server
            clientConnexion.sendCommand(ClientCommandes.CHAT, "[" + username + "]: " + message + "");
            //clear the textfield
            txtInput.clear();

        }
    }



    public void PretALancerUnDes()
    {


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lancer un dé de 6");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez lancer un dé de 6 pour savoir dans quel ordre vous allez pouvoir choisir votre Maison");
        alert.showAndWait();
        Integer de=(int)(1+6*Math.random());
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("Resultat de votre lancé");
        alert2.setHeaderText(null);
        alert2.setContentText("Bravo, vous avez fait un "+ de.toString() + " ! On va voir ce que les autres ont fait !");
        alert2.showAndWait();
        clientConnexion.lanceUnDe(de);
    }

    public void faireChoixFamille(String pFamille)
    {
        etatPrincipal = Etat.CHOIX_FAMILLE;
        //le message a la forme "Fam1;Fam2;Fam3" et correspond aux Familles encore disponibles.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Faire choix Maison");
        alert.setHeaderText(null);
        alert.setContentText("C'est le moment pour vous de choisir votre maison !\n Veuillez choisir en cliquant parmis les maison encore disponible");
        alert.showAndWait();

        for (String str : pFamille.split(";")){
            familleGuiHashMap.get(Famille.FamilyNames.valueOf(str)).highlight();
        }

    }


    public void aFaitChoixFamille(Famille pFam)
    {
        for (FamilleGui fGui : familleGuiHashMap.values()){
            if (fGui.getFamille()!=pFam){
                fGui.unHighLight();
            }

        }
        this.clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_FAIT_CHOIX_FAMILLE,pFam.getFamilyName().name());
    }

    public void refreshFamilleJoueur(Famille pFam, JoueurClient pJoueurClient)
    {
        familleGuiHashMap.get(pFam.getFamilyName()).setJoueurClient(pJoueurClient);
    }


    public void choisirUnTerritoireDemarrage(ArrayList<Territoire> plisteTerritoiresLibres)
    {

        etatPrincipal = Etat.CHOISIR_LES_TERRITOIRES_DEMARRAGE;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Choisissez un territoire");
        alert.setHeaderText(null);
        alert.setContentText("Choisissez un territoire en cliquant sur la carte!");
        carteCliquable=true;
        alert.showAndWait();
    }

    public void rafraichirTousLesTerritoiresSurLaCarte()
    {
          for (Territoire ter : clientConnexion.getRiskGOTterritoires().getTerritoires()){
              String message="";
              if (ter.getAppartientAJoueur()!=null) {
                   message = "territoireUpdateEtRafraichit('"+ter.getNom().name()+"', '"+ter.getAppartientAJoueur().getFamille().getFamilyName().name()+"', '"+((Integer)ter.getNombreDeTroupes()).toString()+"')";
              }
              else {
                  message = "territoireUpdateEtRafraichit('" + ter.getNom().name() + "', '', '')";
              }
              sendToJVS(message);
          }
          rafraichirLesZonesFamilles();
    }

    public void rafraichirLesZonesFamilles()
    {
        for (FamilleGui fGui : familleGuiHashMap.values()){
            fGui.updateLabels();
        }
    }

    public void mettreAJourUnTerritoireSurLaCarte(Territoire ter){
        String message="";
        if (ter.getAppartientAJoueur()!=null) {
            message = "territoireUpdateEtRafraichit('"+ter.getNom().name()+"', '"+ter.getAppartientAJoueur().getFamille().getFamilyName().name()+"', '"+((Integer)ter.getNombreDeTroupes()).toString()+"')";
        }
        else {
            message = "territoireUpdateEtRafraichit('" + ter.getNom().name() + "', '', '')";
        }
        sendToJVS(message);
        familleGuiHashMap.get(ter.getAppartientAJoueur().getFamille().getFamilyName()).updateLabels();
    }

    public void fromJSAChoisiUnTerritoireDemarrage(String s)
    {
        carteCliquable=false;
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        //On est dans l'état principal CHOISIR_LES_TERRITOIRES_DEMARRAGE
        if (ter.getAppartientAJoueur()==null){
            //OK
            System.out.println("le territoire est dispo");
            System.out.println("J'ai affecté le territoire " + ter.getNom().name() + " au joueur " + clientConnexion.getNom());
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE,clientConnexion.getNom()+";"+s);

        }
        else {
            Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Territoire déja occupé");
                        alert.setHeaderText(null);
                        alert.setContentText("Le territoire " + ter.getNom().name() + "est déjà occupé par " + ter.getAppartientAJoueur().getNom() + "\nVeuillez en choisir un innocupé !");
                        alert.showAndWait();
                    });
            carteCliquable=true;

        }
    }




    public void placerUneTroupeSurUnTerritoire()
    {
        etatPrincipal = Etat.PLACER_LES_TROUPES_DEMARRAGE;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajoutez une troupe en renfort");
        alert.setHeaderText(null);
        alert.setContentText("Choisissez un territoire sur lequel ajouter le renfort !");
        carteCliquable=true;
        alert.showAndWait();
    }


    public void fromJSAAjouteUneTroupeDemarrage(String s)
    {
        carteCliquable=false;
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        //On est dans l'état principal PLACER_LES_TROUPES_DEMARRAGE
        if (ter.getAppartientAJoueur()==clientConnexion){
            //OK, le territoire appartient bien à notre client !

            System.out.println("le territoire est dispo");
            System.out.println("J'ai ajouté une troupe au territoire " + ter.getNom().name() + " au joueur " + clientConnexion.getNom());
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_AJOUTE_UNE_TROUPE_DEMARRAGE,clientConnexion.getNom()+";"+s);

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Le territoire ne vous appartient pas !");
            alert.setHeaderText(null);
            alert.setContentText("Le territoire " + s + "appartient à " + clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s).getAppartientAJoueur().getNom() + "\nVeuillez en choisir un qui vous appartienne !");
            carteCliquable=true;
            alert.showAndWait();
        }
    }

    public void sendToJVS(String msg)
    {
        System.out.println("MESSAGE ENVOYE A JAVASCRIPT : " + msg);
        webView.getEngine().executeScript(msg);

    }



    public void nouveauJoueurConnecte(JoueurClient joueurClient)
    {
        JoueurChatGui joueurChatGui = new JoueurChatGui();
        joueurChatGui.setPrefHeight(80/clientConnexion.getNbJoueursAttendus());
        listeDesJoueursChatZone.getChildren().add(joueurChatGui);
        joueurChatGui.setJoueurClient(joueurClient);
        joueurChatGui.setConnected();
    }



}
