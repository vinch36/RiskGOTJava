package gui;
import applogic.CommandesConsoleClient;
import applogic.objects.JoueurClient;
import common.ClientCommandes;
import common.Famille;
import common.Familles;
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
import applogic.objects.ClientConnexion;

import java.awt.Toolkit;
import java.util.HashMap;


public class MainView {

    private WebView webView;
    private HBox playerZone;
    private VBox listeDesJoueursChatZone;
    private VBox leftActionsZone;
    private VBox actionZone;
    private VBox chatZone;
    private HBox familleZone;

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
        createComponents();

    }

    private void waitForAllConnections()
    {
        this.btnFireJS.setDisable(true);

    }

    public void zoom (ScrollEvent event) {
        if (event.isControlDown()) {
            System.out.println(event.getDeltaY());
            double currentZoomValue = ((WebView) event.getSource()).getZoom();
            double delta = event.getDeltaY() / 40 / 20;
            if (delta > 0 && currentZoomValue < 2 || delta < 0 && currentZoomValue > 0.25) {
                ((WebView) event.getSource()).setZoom(currentZoomValue + delta);
            }
        }
    }


    private void createComponents() {
        System.out.println("Je cree les composants de la MainView en tant que Thread " + Thread.currentThread().getName());
        primaryStage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().height-400);
        primaryStage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().width-10);
        primaryStage.setX(5);
        primaryStage.setY(5);
        primaryStage.setTitle("Risk Game of Thrones");
        Scene scene = new Scene(new Group());
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10,10, 10,10));
        root.setStyle("-fx-border-color: black");

        //Création de la zone joueur
        this.playerZone = new HBox();
        playerZone.setStyle("-fx-border-color: black");
        playerZone.setPrefHeight(80);
        root.setTop(playerZone);



        //zone des familles (invisible au début)
        this.familleZone = new HBox();
        familleZone.setStyle("-fx-border-color: black");
        playerZone.getChildren().add(familleZone);

        this.leftActionsZone = new VBox();
        leftActionsZone.setStyle("-fx-border-color: black");
        leftActionsZone.setPrefHeight(400);
        root.setLeft(leftActionsZone);


        //Création de la WebView pour la carte de GOT
        this.webView = new WebView();
        this.webView.setStyle("-fx-border-color: black");
        this.webView.prefHeightProperty().bind(primaryStage.heightProperty());
        this.webView.prefWidthProperty().bind(primaryStage.widthProperty());
        this.webView.getEngine().load(getClass().getResource("/Index.html").toString());
        this.webView.setOnScroll(event -> zoom(event));
        this.webView.setZoom(0.69);
        this.webView.getEngine().setJavaScriptEnabled(true);





        //Mise en place de la JavaScriptInterface pour pouvoir appeler du Java (dans la classe JavaScriptInterface depuis JavaScript
        JavaScriptInterface JSI = new JavaScriptInterface();
        webView.getEngine().getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {

                        if (newState == Worker.State.SUCCEEDED) {

                            JSObject jsobject = (JSObject) webView.getEngine().executeScript("window");
                            jsobject.setMember("myJavaMember", JSI);
                        }
                    }
                });
        root.setCenter(this.webView);




        //Création de la zone d'action, incluant la zone de chat

        //Creation de la zone pour la liste des joueurs avec statut dans le chat
        this.listeDesJoueursChatZone = new VBox();
        listeDesJoueursChatZone.setStyle("-fx-border-color: black");
        listeDesJoueursChatZone.setPrefWidth(200);
        leftActionsZone.getChildren().add(listeDesJoueursChatZone);


        chatZone = createComponentsChat();
        chatZone.setPrefHeight(600);
        chatZone.setFillWidth(true);
        chatZone.setStyle("-fx-border-color: black");
        leftActionsZone.getChildren().add(chatZone);
        actionZone = new VBox();
        actionZone.setStyle("-fx-border-color: black");
        actionZone.setFillWidth(true);
        leftActionsZone.getChildren().add(actionZone);





        this.btn1De = new Button("Lancer un dé");
        btn1De.setOnAction((EventHandler<ActionEvent>) new MainView.LaunchDes());
        btn1De.setDisable(true);
        actionZone.getChildren().add(btn1De);
        scene.setRoot(root);

        createTestButtons(actionZone);
        primaryStage.setScene(scene);
        primaryStage.show();
        new CommandesConsoleClient(clientConnexion); // lance le thread de gestion des commandes
        clientConnexion.sendCommand(ClientCommandes.SEND_NAME,clientConnexion.getName());
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
        for (Famille f : pFams.getFamilles()){
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
                    webView.getEngine().executeScript("test('SKAGOS', 'TEST1')");
                }
            }
        });
        menu.getChildren().add(btnFireJS);

        Button btn2 = new Button();
        btn2.setText("Fermer la connection au serveur");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clientConnexion.closeConnexion();
            }
        });
        menu.getChildren().add(btn2);

        Button btn3 = new Button();
        btn3.setText("Lister les joueurs");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToDisplay = "Je suis : " + clientConnexion.getName() + " - les adversaires sont : ";
                for (int i=0;i<clientConnexion.getAdversaires().size();i++)
                {
                    messageToDisplay=messageToDisplay+clientConnexion.getAdversaires().get(i).getName()+" - ";
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Liste des adversaires");
                alert.setHeaderText(null);
                alert.setContentText(messageToDisplay);
                alert.showAndWait();

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
        label.setText("CHAT - ["+this.clientConnexion.getName()+"]");

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
            String username = clientConnexion.getName().trim();
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

    //Action d'envoie d'un dé
    private class LaunchDes implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            clientConnexion.lanceUnDe();
            btn1De.setDisable(true);

        }
    }


    public void PretALancerUnDes()
    {
        btn1De.setDisable(false);
        btnFireJS.setDisable(false);
    }

    public void faireChoixFamille(String pFamille)
    {
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
            else{
                refreshFamilleJoueur(pFam, clientConnexion);
            }
        }
        this.clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_FAIT_CHOIX_FAMILLE,pFam.getFamilyName().name());
    }

    public void refreshFamilleJoueur(Famille pFam, JoueurClient pJoueurClient)
    {
        familleGuiHashMap.get(pFam.getFamilyName()).setJoueurClient(pJoueurClient);
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
