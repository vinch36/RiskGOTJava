package gui;
import applogic.CommandesConsoleClient;
import applogic.objects.ChatMessage;
import applogic.objects.JoueurClient;
import common.ClientCommandes;
import common.objects.*;
import common.objects.cartes.CarteTerritoire;
import common.util.*;
import gui.cartes.CarteTerritoireGui;
import gui.de.DeGui;
import gui.de.LancerLesDeDemarrage;
import gui.invasion.InvasionGui;
import gui.manoeuvre.ManoeuvreGui;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import network.ClientConnexion;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static javafx.concurrent.Worker.State.SUCCEEDED;
import static javafx.scene.text.Font.font;


public class MainView {


    public InvasionGui getInvasionGuiCourante() {
        return invasionGuiCourante;
    }

    private ArrayList<Territoire> territoiresARafraichirSurLaCarte;

    private InvasionGui invasionGuiCourante;

    public Etat getEtatPrincipal() {
        return etatPrincipal;
    }

    public boolean isCarteCliquable() {
        return carteCliquable;
    }

    private boolean carteCliquable;

    public void setEtatPrincipal(Etat etatPrincipal) {
        this.etatPrincipal = etatPrincipal;
        System.out.println("#######################################################");
        System.out.println("ETAT PRINCIPAL = " + etatPrincipal.name());
        System.out.println("#######################################################");
    }

    private Etat etatPrincipal;

    public SousEtat getSousEtat() {
        return sousEtat;
    }

    public void setSousEtat(SousEtat pSousEtat) {
        this.sousEtat = pSousEtat;
        System.out.println("-------------------------------------------------------");
        System.out.println("SOUS - ETAT PRINCIPAL = " + etatPrincipal.name()+ "." + sousEtat.name());
        System.out.println("-------------------------------------------------------");
    }

    private SousEtat sousEtat;

    public SousEtatRenforcez getSousEtatRenforcez() {
        return sousEtatRenforcez;
    }

    public void setSousEtatRenforcez(SousEtatRenforcez sousEtatRenforcez) {
        this.sousEtatRenforcez = sousEtatRenforcez;
    }

    private SousEtatRenforcez sousEtatRenforcez;
    public SousEtatEnvahissez getSousEtatEnvahissez() {
        return sousEtatEnvahissez;
    }

    public void setSousEtatEnvahissez(SousEtatEnvahissez sousEtatEnvahissez) {
        this.sousEtatEnvahissez = sousEtatEnvahissez;
    }

    private SousEtatEnvahissez sousEtatEnvahissez;



    public SousEtatManoeuvrez getSousEtatManoeuvrez() {
        return sousEtatManoeuvrez;
    }

    public void setSousEtatManoeuvrez(SousEtatManoeuvrez sousEtatManoeuvrez) {
        this.sousEtatManoeuvrez = sousEtatManoeuvrez;
    }

    private SousEtatManoeuvrez sousEtatManoeuvrez;
    private Scene scene;
    private HBox mainContainer;
    private VBox invasionManoeuvresArea;
    private WebView webView;
    private StackPane webViewContainerStackPane;
    private VBox playerZone;
    private HBox zoneHauteActionsEtJoueursConnectes;
    private GridPane zoneBouttons;
    private HBox zoneCartesTerritoires;
    private VBox listeDesJoueursChatZone;
    private VBox chatZone;
    private VBox familleZone;
    private LancerLesDeDemarrage lancementDeDe;

    public ClientConnexion getClientConnexion() {
        return clientConnexion;
    }

    private ClientConnexion clientConnexion;
    private TextField txtInput;
    private ScrollPane scrollPane;
    private TextFlow txtAreaDisplay;
    private Stage primaryStage;
    private Button btnManoeuvrer;
    private Button btnConvertirDesTerritoires;

    private HashMap<Famille.FamilyNames, FamilleGui> familleGuiHashMap;

    private ArrayList<CarteTerritoireGui> cartesTerritoireGuiList;

    public MainView(ClientConnexion pClientConnexion) {
        this.clientConnexion = pClientConnexion;
        territoiresARafraichirSurLaCarte = new ArrayList<>();
        cartesTerritoireGuiList = new ArrayList<>();
    }

    public void start(Stage stage) {
        this.primaryStage = stage;
        createComponents();
        demarre();
    }


    public void zoom(ScrollEvent event) {
        if (event.isControlDown()) {
            double currentZoomValue = ((WebView) event.getSource()).getZoom();
            double delta = event.getDeltaY() / 40 / 20;
            if (delta > 0 && currentZoomValue < 2 || delta < 0 && currentZoomValue > 0.25) {
                ((WebView) event.getSource()).setZoom(currentZoomValue + delta);
            }
        }
    }

    private void initStageAndSceneAndMainContainer(){
        primaryStage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().height - 100);
        primaryStage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().width - 10);
        primaryStage.setX(5);
        primaryStage.setY(5);
        primaryStage.setTitle("Risk Game of Thrones");
        this.scene = new Scene(new Group());
        mainContainer = new HBox();
    }

    private void initLeftPart(){
        this.playerZone = new VBox();
        playerZone.setStyle("-fx-border-color: black");
        playerZone.setMinWidth(500);
        playerZone.setMaxWidth(500);

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
    }


    private void initWebView(){
        //Création de la WebView pour la carte de GOT
        this.webView = new WebView();
        this.webView.prefHeightProperty().bind(primaryStage.heightProperty());
        this.webView.setPrefWidth(primaryStage.getWidth() - playerZone.getWidth());
        this.webView.getEngine().load(getClass().getResource("/Index.html").toString());
        this.webView.setOnScroll(event -> zoom(event));
        this.webView.setZoom(0.33);
        this.webView.getEngine().setJavaScriptEnabled(true);
        this.webViewContainerStackPane = new StackPane();
        this.webViewContainerStackPane.getChildren().add(webView);
        this.setCarteCliquable(false);
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

    }


    private void initRightPart()
    {
        this.invasionManoeuvresArea = new VBox();
        this.invasionManoeuvresArea.setMinWidth(600);
        //Creation de la zone en haut à droite, pour y mettre la liste des joueurs connectés et le bouton d'aide et les boutons d'acions ?
        this.zoneHauteActionsEtJoueursConnectes = new HBox();
        this.zoneHauteActionsEtJoueursConnectes.setStyle("-fx-border-color: black");
        //Creation de la zone pour la liste des joueurs avec statut
        this.listeDesJoueursChatZone = new VBox();
        listeDesJoueursChatZone.setStyle("-fx-border-color: black");
        listeDesJoueursChatZone.setStyle("-fx-background-color: black;");
        this.zoneCartesTerritoires = new HBox();
        invasionManoeuvresArea.getChildren().add(zoneHauteActionsEtJoueursConnectes);
        initBouttonsRightPart();
        invasionManoeuvresArea.getChildren().add(zoneCartesTerritoires);
    }

    private void initBouttonsRightPart(){
        zoneBouttons = new GridPane();
        zoneBouttons.setPadding(new javafx.geometry.Insets(5));
        zoneBouttons.setHgap(5);
        zoneBouttons.setVgap(5);

        zoneHauteActionsEtJoueursConnectes.getChildren().add(listeDesJoueursChatZone);
        zoneHauteActionsEtJoueursConnectes.getChildren().add(zoneBouttons);

        Button aide = ajouterBoutonAide();
        zoneBouttons.setHalignment(aide, HPos.RIGHT);
        // Put on cell (0,1)
        zoneBouttons.add(aide, 0, 0);

        btnManoeuvrer = new Button();
        btnManoeuvrer.setText("MANOEUVRER");
        btnManoeuvrer.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        btnManoeuvrer.setDisable(true);
        btnManoeuvrer.setOnMouseClicked(e -> {
            this.clickSurManoeuvrer();
        });

        zoneBouttons.setHalignment(btnManoeuvrer, HPos.RIGHT);
        zoneBouttons.add(btnManoeuvrer,1,0);

        btnConvertirDesTerritoires = new Button();
        btnConvertirDesTerritoires.setText("CONVERTIR TERRITOIRES");
        btnConvertirDesTerritoires.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        btnConvertirDesTerritoires.setDisable(true);

        btnConvertirDesTerritoires.setOnMouseClicked(e -> {
            this.clickSurConvertirDesTerritoires();
        });

        zoneBouttons.setHalignment(btnConvertirDesTerritoires, HPos.RIGHT);
        zoneBouttons.add(btnConvertirDesTerritoires,1,1);


        Button btnTest = new Button();
        btnTest.setText("Test");
        btnTest.setOnMouseClicked(e -> {
            this.testFunction();
        });
        zoneBouttons.setHalignment(btnTest, HPos.RIGHT);
        zoneBouttons.add(btnTest,0,1);


    }
    private void createComponents() {
        //Initialisation de la scene principale et du contenur principal.
        initStageAndSceneAndMainContainer();
        //Création de la zone de gauche - chat et familles
        initLeftPart();
        //Creation de la WebView
        initWebView();
        //Creation de la zone de droite: info joueurs connectés, zone bouttons et zone cartes
        initRightPart();

        mainContainer.getChildren().addAll(this.playerZone,this.webViewContainerStackPane, this.invasionManoeuvresArea);
        scene.setRoot(mainContainer);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private void demarre()
    {

        new CommandesConsoleClient(clientConnexion); // lance le thread de gestion des commandes
        clientConnexion.sendCommand(ClientCommandes.SEND_NAME, clientConnexion.getNom());
        this.setEtatPrincipal(Etat.ATTENTE_CONNECTION_JOUEURS);
    }


    private void testFunction() {
        rafraichirTousLesTerritoiresEnEvidence();
        rafraichirLesTerritoiresApresInvasion();

    }


    public void createComponentsZoneListeJoueurs(int nbJoueursAttendus) {

        Label label = new Label();
        label.setText("Joueurs Connectés");
        label.setTextFill(Color.WHITE);
        this.listeDesJoueursChatZone.getChildren().add(label);
        this.nouveauJoueurConnecte(this.clientConnexion);

    }

    public void createComponentsFamilles(Familles pFams) {
        this.familleGuiHashMap = new HashMap<>();
        for (Famille f : pFams.getFamillesActives()) {
            FamilleGui famGui = new FamilleGui(f, this);
            this.familleZone.getChildren().add(famGui);
            this.familleGuiHashMap.put(f.getFamilyName(), famGui);


        }
    }

    public Button ajouterBoutonAide() {
        Button btn2 = new Button();
        btn2.setText("Les règles !");
        ImageView riskImg = new ImageView(new Image(getClass().getResourceAsStream("/img/Risk_Small.png")));
        btn2.setGraphic(riskImg);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    InputStream in = getClass().getResourceAsStream("/regle.pdf");
                    //URL url= getClass().getResource("/regle.pdf");
                    File pdfFile = new File("regles_du_jeu_risk_got.pdf");
                    if (!pdfFile.exists()) {
                        Files.copy(in, Paths.get("regles_du_jeu_risk_got.pdf"));
                    }
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
        return btn2;
    }


    public VBox createComponentsChat() {
        VBox vBox = new VBox();

        scrollPane = new javafx.scene.control.ScrollPane();   //pane to display text messages
        HBox hBox = new HBox(); //pane to hold input textfield and send button
        Label label = new Label();
        label.setStyle("-fx-font-weight: bold");
        label.setText("CHAT - [" + this.clientConnexion.getNom() + "]");

        txtAreaDisplay = new TextFlow();
        //txtAreaDisplay.setEditable(false);
        scrollPane.setContent(txtAreaDisplay);
        txtAreaDisplay.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
                    txtAreaDisplay.layout();
                    scrollPane.layout();
                    scrollPane.setVvalue(1.0f);
                }));
        txtAreaDisplay.setStyle("-fx-background-color: black;");

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
        vBox.getChildren().addAll(label, scrollPane, hBox);

        vBox.setVgrow(scrollPane, Priority.ALWAYS);
        return vBox;

    }


    public void updateChatZone(ChatMessage chatMessage) {
        Text txt = new Text();
        String textToDisplay;
        switch (chatMessage.getMessageType()) {
            case INFO:
                txt.setFill(Color.WHITE);
                txt.setFont(font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));
                textToDisplay = "[INFO]:" + chatMessage.getMessageTxt();
                break;
            case INFO_IMPORTANTE:
                txt.setFill(Color.WHITE);
                txt.setFont(font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
                textToDisplay = "###################\n" + chatMessage.getMessageTxt() + "\n###################";
                break;
            case CHAT:
                txt.setFont(font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
                txt.setFill(Color.web(chatMessage.getColor()));
                if (chatMessage.getJoueur().getFamille() == null) {
                    textToDisplay = "[" + chatMessage.getJoueur().getNom() + "]: " + chatMessage.getMessageTxt();
                } else {
                    textToDisplay = "[" + chatMessage.getJoueur().getNomAtFamille() + "]: " + chatMessage.getMessageTxt();
                }
                break;
            case INFOCHAT:
                txt.setFont(font("Verdana", FontWeight.NORMAL, FontPosture.ITALIC, 14));
                txt.setFill(Color.web(chatMessage.getColor()));
                textToDisplay = chatMessage.getJoueur().getNomAtFamille() + " " + chatMessage.getMessageTxt();
                break;
            case INFOCHAT_IMPORTANTE:
                txt.setFont(font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 16));
                txt.setFill(Color.web(chatMessage.getColor()));
                textToDisplay = "------------------\n" + chatMessage.getJoueur().getNomAtFamille() + " " + chatMessage.getMessageTxt() + "\n------------------";
                break;
            case ACTION:
                txt.setFont(font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
                txt.setFill(Color.GREENYELLOW);
                textToDisplay = "[ACTION]:" + chatMessage.getMessageTxt();
                break;
            case ERREUR:
                txt.setFont(font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 12));
                txt.setFill(Color.RED);
                textToDisplay = "[ERREUR]:" + chatMessage.getMessageTxt();
                break;
            case FROM_SERVEUR_DEBUG:
                txt.setFont(font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 10));
                txt.setFill(Color.LIGHTGRAY);
                textToDisplay = "[FROM_SERVEUR]:" + chatMessage.getMessageTxt();
                break;
            case TO_SERVEUR_DEBUG:
                txt.setFont(font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 10));
                txt.setFill(Color.LIGHTGRAY);
                textToDisplay = "[TO_SERVEUR]:" + chatMessage.getMessageTxt();
                break;
            default:
                txt.setFill(Color.WHITE);
                textToDisplay = "INCONNU: " + chatMessage.getMessageTxt();
                break;
        }
        txt.setText(textToDisplay + "\n");
        Text textNewLine = new Text();
        System.out.println("***CHAT MESSAGE *** \n" + textToDisplay + "\n***FIN CHAT MESSAGE ***");
        txtAreaDisplay.getChildren().add(txt);
        textNewLine.setText("\n");
        textNewLine.setFont(font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 6));
        txtAreaDisplay.getChildren().add(textNewLine);
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
            clientConnexion.sendCommand(ClientCommandes.CHAT, username + ";" + message);
            //clear the textfield
            txtInput.clear();

        }
    }


    public void nouveauJoueurConnecte(JoueurClient joueurClient) {
        JoueurChatGui joueurChatGui = new JoueurChatGui();
        joueurChatGui.setPrefHeight(45 / clientConnexion.getNbJoueursAttendus());
        listeDesJoueursChatZone.getChildren().add(joueurChatGui);
        joueurChatGui.setJoueurClient(joueurClient);
        joueurChatGui.setConnected();
        ChatMessage chatMessage = new ChatMessage("Le joueur " + joueurClient.getNom() + " est connecté !", ChatMessage.ChatMessageType.INFO);
        updateChatZone(chatMessage);
    }


    public void PretALancerUnDes() {

        lancementDeDe = new LancerLesDeDemarrage(clientConnexion, true);
        DeGui de = new DeGui(DeGui.CouleurDe.NOIR, DeTypeValeur.TypeDe.SIX, 1, 120, 2500, 18, true);
        de.mettreEnEvidence();
        lancementDeDe.ajouterUnDe(de, this.clientConnexion);
        for (Joueur j : clientConnexion.getAdversaires()) {
            lancementDeDe.ajouterUnDe(new DeGui(DeGui.CouleurDe.NOIR, DeTypeValeur.TypeDe.SIX, 1, 120, 2500, 18, false), j);
        }
        lancementDeDe.createComponents();
        invasionManoeuvresArea.getChildren().add(lancementDeDe);
    }


    public void mettreAJourUnResultatDeDeDemarrage(Joueur j, int pValeur) {
        lancementDeDe.afficherUnResultat(this.lancementDeDe.obtenirUnDe(j, 0), pValeur);
    }

    public void refreshJoueurActif(JoueurClient joueur) {
        //rafraichirTousLesTerritoiresEnEvidence();
        for (FamilleGui fGui : familleGuiHashMap.values()) {
            if (fGui.getFamille() != joueur.getFamille()) {
                fGui.setInactif();
            } else {
                fGui.setActif();
            }
        }
    }

    public void faireChoixFamille(String pFamille) {
        etatPrincipal = Etat.CHOIX_FAMILLE;
        //le message a la forme "Fam1;Fam2;Fam3" et correspond aux Familles encore disponibles.
        String message = "C'est le moment pour vous de choisir votre maison ! Veuillez choisir en cliquant sur l'image de la Maison parmis celles encore disponibles (entourées en vert)";
        for (String str : pFamille.split(";")) {
            familleGuiHashMap.get(Famille.FamilyNames.valueOf(str)).peutEtreChoisie();
            message = message + " - " + str;
        }
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.ACTION);
        this.updateChatZone(chatMessage);
    }


    public void aFaitChoixFamille(Famille pFam) {
        for (FamilleGui fGui : familleGuiHashMap.values()) {
            if (fGui.getFamille() != pFam) {
                fGui.unHighLight();
            }

        }
        this.clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_FAIT_CHOIX_FAMILLE, pFam.getFamilyName().name());
        invasionManoeuvresArea.getChildren().remove(lancementDeDe);
    }

    public void refreshFamilleJoueur(Famille pFam, JoueurClient pJoueurClient) {
        familleGuiHashMap.get(pFam.getFamilyName()).setJoueurClient(pJoueurClient);
    }


    public void choisirUnTerritoireDemarrage(ArrayList<Territoire> plisteTerritoiresLibres) {
        String message = "Veuillez choisir un territoire disponible";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.ACTION);
        updateChatZone(chatMessage);
        this.setCarteCliquable(true);
    }


    public void rafraichirLesZonesFamilles() {
        for (FamilleGui fGui : familleGuiHashMap.values()) {
            fGui.updateLabels();
        }
    }

    public void ajouterUneCarteTerritoire(CarteTerritoire pCarteTerritoire){
        CarteTerritoireGui carteTerritoireGui = new CarteTerritoireGui(pCarteTerritoire);
        this.zoneCartesTerritoires.getChildren().add(carteTerritoireGui);
        this.cartesTerritoireGuiList.add(carteTerritoireGui);
        carteTerritoireGui.setCliquable(false);
        this.rafraichirLesZonesFamilles();
    }

    public void enleverUneCarteTerritoireGui(CarteTerritoire pCarteTerritoire){
        CarteTerritoireGui carteGuiAEnlever=null;
        for (CarteTerritoireGui carteTerritoireGui:cartesTerritoireGuiList) {
            if (carteTerritoireGui.getCarteTerritoire()==pCarteTerritoire){
                carteGuiAEnlever = carteTerritoireGui;
            }

        }
        if (carteGuiAEnlever!=null) {
            cartesTerritoireGuiList.remove(carteGuiAEnlever);
            this.zoneCartesTerritoires.getChildren().remove(carteGuiAEnlever);
            this.rafraichirLesZonesFamilles();
        }


    }


    public void selectionnerDesCartesTerritoires()
    {
        this.setSousEtatRenforcez(SousEtatRenforcez.ECHANGEZ_DES_CARTES_TERRITOIRE);
        this.zoneCartesTerritoires.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        btnConvertirDesTerritoires.setDisable(false);
        for (CarteTerritoireGui carteTerritoireGui : cartesTerritoireGuiList){
            carteTerritoireGui.setCliquable(true);
        }
    }


    private void clickSurConvertirDesTerritoires() {
        ArrayList<CarteTerritoire> cartesSelectionnees = new ArrayList<>();
        // On récupère la liste des cartes que le joueur a selectionné
        for (CarteTerritoireGui carteTerritoireGui : cartesTerritoireGuiList) {
            if (carteTerritoireGui.isSelectionnee()) {
                cartesSelectionnees.add(carteTerritoireGui.getCarteTerritoire());
            }
        }

        //On en déduit le message à afficher dans une boite de dialogue grâce à la méthode associée
        String message;
        boolean combinaisonValable = false;

        if (cartesSelectionnees.size() == 0) {
            message = "Aucun territoire n'est selectionné, donc vous n'obtiendrez aucun bonus supplémentaire si vous cliquez OK";
        }

        else if (cartesSelectionnees.size() == 1) {
            CarteTerritoire carteTerritoire = cartesSelectionnees.get(0);
            message = "Vous receverez 1 " + carteTerritoire.getUniteSpeciale() + "en utilisant la carte " + carteTerritoire.getTerritoire().getNom().name();
            combinaisonValable = true;
        }
        else if (cartesSelectionnees.size() == 2) {
            message = "Vous avez sélectionné 2 territoires... veuillez cliquer Annuler si vous souhaitez sélectionner 1 (pour obtenir une unité spéciale) ou 3 (pour obtenir un bonus de troupes).\nSi vous cliquez OK vous n'obtiendrez pas debonus supplémentaire à ce tour";
        }
        else if (cartesSelectionnees.size() == 3) {
            int bonusTroupes = clientConnexion.getRiskGOTCarteTerritoires().getBonusCombinaisonDeTroisCartesTerritoires(cartesSelectionnees.get(0),cartesSelectionnees.get(1), cartesSelectionnees.get(2));
            message = "La combinaison de vos 3 cartes territoires :\n-"+
                    cartesSelectionnees.get(0).getTerritoire().getNom().name() + " - " + cartesSelectionnees.get(0).getUniteSpeciale().name()+"\n-" +
                    cartesSelectionnees.get(1).getTerritoire().getNom().name() + " - " + cartesSelectionnees.get(1).getUniteSpeciale().name()+"\n-" +
                    cartesSelectionnees.get(2).getTerritoire().getNom().name() + " - " + cartesSelectionnees.get(2).getUniteSpeciale().name()+"\n" +
                    "Vous apporte un bonus de "+ bonusTroupes +" troupes !";

            combinaisonValable = (bonusTroupes>0);
        }
        else// (cartesSelectionnees.size()>3)
        {
            message = "Plus de 3 territoire sont selectionnés, veuillez cliquer Annuler pour modifier votre sélection et n'en sélectionner qu'1 (pour obtenir une inité spéciale) ou 3 (pour obtenir un bonus de troupes).\nSi vous cliquez OK vous n'obtiendrez pas debonus supplémentaire à ce tour";
        }
        if (combinaisonValable) { //La combinaison de territoire est valable.
            message = message + "\n\nCliquez sur OK pour valider votre choix, ou annuler pour modifier votre sélection. Si vous validez et qu'il vous reste des cartes territoires, vous pourrez toujours continuer à en convertir d'autres";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conversion de cartes territoires");
            alert.setHeaderText("Résultat de votre sélection:");
            alert.setContentText(message);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) { // L'utilisateur valide son choix.
                this.btnConvertirDesTerritoires.setDisable(true);
                this.zoneCartesTerritoires.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                for (CarteTerritoireGui carteTerritoireGui : cartesTerritoireGuiList){
                    carteTerritoireGui.setCliquable(false);
                }
                if (cartesSelectionnees.size() == 1) {
                    enleverUneCarteTerritoireGui(cartesSelectionnees.get(0));
                    clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_CONVERTI_UNE_CARTE_TERRITOIRE_EN_UNITE_SPECIALE, cartesSelectionnees.get(0).getTerritoire().getNom().name());
                }
                if (cartesSelectionnees.size() == 3) {
                    enleverUneCarteTerritoireGui(cartesSelectionnees.get(0));
                    enleverUneCarteTerritoireGui(cartesSelectionnees.get(1));
                    enleverUneCarteTerritoireGui(cartesSelectionnees.get(2));
                    clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_CONVERTI_TROIS_CARTES_TERRITOIRE_EN_TROUPES_SUPPLEMENTAIRES, cartesSelectionnees.get(0).getTerritoire().getNom().name()+";"+cartesSelectionnees.get(1).getTerritoire().getNom().name()+";"+cartesSelectionnees.get(2).getTerritoire().getNom().name());
                }


            } else //Clique sur annuler, pour modifier la sélection.
            {
                //rien, l'utilisateur peut changer sa sélection et revalider.
            }
        } else //combinaison de territoire non valide
        {
            message = message + "\n\nVous avez choisi une combinaison de territoire non valable.\nCliquez annuler pour modifier votre sélection. Si vous validez celà reviendra à passer cette étape de convertion cartes territoires.";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conversion de cartes territoires");
            alert.setHeaderText("Résultat de votre sélection:");
            alert.setContentText(message);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) { // L'utilisateur valide son choix - passe son tour.
                this.btnConvertirDesTerritoires.setDisable(true);
                this.btnConvertirDesTerritoires.setDisable(true);
                this.zoneCartesTerritoires.setBorder(new Border(new BorderStroke(Color.DARKGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                for (CarteTerritoireGui carteTerritoireGui : cartesTerritoireGuiList){
                    carteTerritoireGui.setCliquable(false);
                }
                clientConnexion.sendCommand(ClientCommandes.JOUEUR_PASSE_LA_CONVERSION_DE_CARTES_TERRITOIRES, "");

            } else //Clique sur annuler, pour modifier sa sélection.
            {
                //rien, l'utilisateur peut changer sa sélection et revalider.
            }

        }

    }


    private CarteTerritoire.UniteSpeciale uniteSpecialeEnCoursDePlacement;
    public void placerUneUniteSpeciale(CarteTerritoire carteTerritoire)
    {
        String messageChat2 = "Veuillez maintenant placer votre unité spéciale "+ carteTerritoire.getUniteSpeciale().name() + " sur un territoire que vous controlez";
        ChatMessage chatMessage2 = new ChatMessage(messageChat2, ChatMessage.ChatMessageType.ACTION);
        this.updateChatZone(chatMessage2);
        this.setSousEtatRenforcez(SousEtatRenforcez.DEPLOYEZ_DES_UNITES_SPECIALES);
        this.uniteSpecialeEnCoursDePlacement = carteTerritoire.getUniteSpeciale();
        this.setCarteCliquable(true);

    }





    public void mettreAJourUnTerritoireSurLaCarte(Territoire ter, boolean mettreEnEvidence, boolean effacerLesTerritoiresDejaEnEvidence) {
        if (effacerLesTerritoiresDejaEnEvidence) {
            rafraichirTousLesTerritoiresEnEvidence();
        }
        String message = "";


        if (ter.getAppartientAJoueur() != null) {
            familleGuiHashMap.get(ter.getAppartientAJoueur().getFamille().getFamilyName()).updateLabels();
            message = "territoireUpdateEtRafraichit('" + ter.getNom().name() + "', '" + ter.getAppartientAJoueur().getFamille().getFamilyName().name() + "', '" + ((Integer) ter.getNombreDeTroupes()).toString() + "', '"+ ter.getNbChevalier()+"', '"+ ter.getNbEnginDeSiege()+"', '"+ ter.getNbFortification()+"')";

        } else {
            message = "territoireUpdateEtRafraichit('" + ter.getNom().name() + "', '', '','0','0','0')";
        }

        sendToJVS(message);

        if (mettreEnEvidence) {
            mettreEnEvidenceUnTerritoireSurLaCarte(ter);
        }
    }

    public void rafraichirTousLesTerritoiresEnEvidence() {
        for (Territoire terARafraichir : territoiresARafraichirSurLaCarte) {
            mettreAJourUnTerritoireSurLaCarte(terARafraichir, false, false);
        }

        territoiresARafraichirSurLaCarte.removeAll(territoiresARafraichirSurLaCarte);
    }

    public void mettreEnEvidenceUnTerritoireSurLaCarte(Territoire ter) {
        String message = "";
        if (ter.getAppartientAJoueur() != null) {
            familleGuiHashMap.get(ter.getAppartientAJoueur().getFamille().getFamilyName()).updateLabels();
            message = "territoireMettreEnEvidence('" + ter.getNom().name() + "')";

        }
        sendToJVS(message);
        this.territoiresARafraichirSurLaCarte.add(ter);

    }

    public void rafraichirLesTerritoiresApresInvasion() {
        mettreAJourUnTerritoireSurLaCarte(clientConnexion.getInvasionEnCours().getTerritoireSource(), true, false);
        mettreAJourUnTerritoireSurLaCarte(clientConnexion.getInvasionEnCours().getTerritoireCible(), true, false);
    }




    public void fromJSAChoisiUnTerritoireDemarrage(String s) {
        this.setCarteCliquable(false);
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        //On est dans l'état principal CHOISIR_LES_TERRITOIRES_DEMARRAGE
        if (ter.getAppartientAJoueur() == null) {
            //OK
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE, clientConnexion.getNom() + ";" + s);
        } else {
            ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] DEJA OCCUPE PAR [" + ter.getAppartientAJoueur().getNom() + "]\nMettez vos lunettes en choisissez en un innocupé !", ChatMessage.ChatMessageType.ERREUR);
            updateChatZone(chatMessage);
            this.setCarteCliquable(true);
        }
    }


    public void fromJSAAjouteUneTroupe(String s) {
        this.setCarteCliquable(false);
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        if (ter.getAppartientAJoueur() == clientConnexion) {
            //OK, le territoire appartient bien à notre client !
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_RENFORCE_UN_TERRITOIRE, clientConnexion.getNom() + ";" + s);
        } else {
            ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] APPARTIENT A [" + ter.getAppartientAJoueur().getNom() + "]\nMettez vos lunettes en choisissez en un qui vous appartient !", ChatMessage.ChatMessageType.ERREUR);
            updateChatZone(chatMessage);
            this.setCarteCliquable(true);
        }
    }


    public void fromJSAAjouteUneUniteSpeciale(String s) {
        this.setCarteCliquable(false);
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        if (ter.getAppartientAJoueur() == clientConnexion) {
            //OK, le territoire appartient bien à notre client !
            clientConnexion.sendCommand(ClientCommandes.JOUEUR_A_DEPLOYE_UNE_UNITE_SPECIALE, s+";"+uniteSpecialeEnCoursDePlacement);
        } else {
            ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] APPARTIENT A [" + ter.getAppartientAJoueur().getNom() + "]\nMettez vos lunettes en choisissez en un qui vous appartient !", ChatMessage.ChatMessageType.ERREUR);
            updateChatZone(chatMessage);
            this.setCarteCliquable(true);
        }
    }

    //********************************
    // INVASION - FROM JAVASCRIPT PART
    //********************************
    public void fromJSAChoisiUnTerritoireSourceInvasion(String s) {
        this.setCarteCliquable(false);

        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        //On est dans l'état principal PLACER_LES_TROUPES_DEMARRAGE
        if (ter.getAppartientAJoueur() == clientConnexion) {
            //OK, le territoire appartient bien à notre client !
            if (ter.getNombreDeTroupes() > 1) {
                //OK, le territoire comporte bien au moins 2 troupes
                String message = "Vous avez choisi d'attaquer depuis le territoire " + ter.getNom().name() + "\nVeuillez maintenant chosir un territoire ennemi, connecté au territoire " + s;
                ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.ACTION);
                updateChatZone(chatMessage);
                clientConnexion.getInvasionEnCours().setTerritoireSource(ter);
                mettreAJourUnTerritoireSurLaCarte(ter, true, true);
                this.setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_TERRITOIRE_CIBLE);
                this.setCarteCliquable(true);
            } else {
                ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] N'A QU'UNE SEULE ARMEE\nMettez vos lunettes en choisissez en un qui en a au moins deux !", ChatMessage.ChatMessageType.ERREUR);
                updateChatZone(chatMessage);
                this.setCarteCliquable(true);
            }
        } else {
            ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] APPARTIENT A [" + ter.getAppartientAJoueur().getNom() + "]\nMettez vos lunettes en choisissez en un qui vous appartient !", ChatMessage.ChatMessageType.ERREUR);
            updateChatZone(chatMessage);
            this.setCarteCliquable(true);
        }
    }

    public void fromJSAChoisiUnTerritoireCibleInvasion(String s) {
        this.setCarteCliquable(false);
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        Territoire terSource = clientConnexion.getInvasionEnCours().getTerritoireSource();
        if (ter == terSource) {
            //Cas ou le joueur reclique sur le même territoire, on va se remettre en mode selection du territoire source
            mettreAJourUnTerritoireSurLaCarte(terSource, false, false);
            demarrerUneInvasion();
        } else {

            if (ter.getAppartientAJoueur() != clientConnexion) {
                //OK, le territoire n'appartient pas à notre client !
                if (ter.connexe(terSource)) {
                    //OK, le territoire est bien connecté !
                    mettreAJourUnTerritoireSurLaCarte(ter, true, false);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Veuillez confirmer l'Invasion");
                    alert.setContentText("Veuillez confirmer que vous souhaitez bien lancer une invasion :\n- Depuis votre  territoire " + terSource.getNom().name() + " comportant " + terSource.getNombreDeTroupes() + " troupes \n- Sur le territoire " + s + " apartenant à " + ter.getAppartientAJoueur().getNom() + " défendu par " + ter.getNombreDeTroupes() + " troupes \nUne fois l'invasion lancée, il ne sera plus possible d'annuler !");
                    Optional<ButtonType> option = alert.showAndWait();
                    clientConnexion.getInvasionEnCours().setTerritoireCible(ter);
                    if (option.get() == ButtonType.OK) {
                        btnManoeuvrer.setDisable(true);
                        clientConnexion.sendCommand(ClientCommandes.JOUEUR_LANCE_UNE_INVASION, clientConnexion.getNom() + ";" + clientConnexion.getInvasionEnCours().getTerritoireSource().getNom().name() + ";" + clientConnexion.getInvasionEnCours().getTerritoireCible().getNom().name());
                    } else {
                        mettreAJourUnTerritoireSurLaCarte(terSource, false, true);
                        mettreAJourUnTerritoireSurLaCarte(ter, false, true);
                        demarrerUneInvasion();
                    }
                } else {
                    ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] N'EST PAS CONNECTE A " + terSource.getNom().name() + "\nMettez vos lunettes en choisissez en un qui soit connecté !", ChatMessage.ChatMessageType.ERREUR);
                    updateChatZone(chatMessage);
                    this.setCarteCliquable(true);
                    btnManoeuvrer.setDisable(false);
                }
            } else {
                ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "]VOUS APPARTIENT !\nVous voulez vraiment vous auto-attaquer ?!", ChatMessage.ChatMessageType.ERREUR);
                updateChatZone(chatMessage);
                this.setCarteCliquable(true);
                btnManoeuvrer.setDisable(false);
            }
        }
    }


    //Un joueur reçot des renforts et de l'argent - on met à jour son affichage
    //pJoueur: le joueur qui reçoit
    //pRenfort: nombre de troupes en renfort (avec (chateaux+territoires)/3)
    //int pbOnus: bonus regions
    // int pArgent: argent en renfort

    public void renfortRecu(JoueurClient pJoueur, int pRenfort, int pBonus, int pPorts, int pArgent) {
        setEtatPrincipal(Etat.TOUR_DE_JEU);
        setSousEtat(SousEtat.RENFORCEZ);
        btnManoeuvrer.setText("MANOEUVRER");
        btnManoeuvrer.setOnMouseClicked(e -> {
            this.clickSurManoeuvrer();
        });
        String message = " Démarre son tour avec:\n- "
                + pJoueur.getNombreDeTerritoires() + " TERRITOIRES\n- "
                + pJoueur.getNombreDeChateaux() + " CHATEAUX\n"
                + "Il reçoit donc (" + pJoueur.getNombreDeTerritoires() + "+" + pJoueur.getNombreDeChateaux() + ")/3=" + pRenfort + " troupes en renfort\n"
                + "- BONUS REGIONS: " + pJoueur.calculerBonusRegionAsString() + "=" + pBonus + " troupes supplémentaires \n"
                + "Il contrôle également " + pJoueur.getNombreDePorts() + " PORTS\n"
                + "Le joueur reçoit donc (" + pJoueur.getNbTroupeAPlacer() + "+" + pPorts + ")*100=" + pArgent + " pièces d'or supplémentaires";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, pJoueur);
        updateChatZone(chatMessage);
        familleGuiHashMap.get(pJoueur.getFamille().getFamilyName()).updateLabels();
    }

    public void demarrerUneInvasion() {
        //
        String message = "Vous pouvez lancer une invasion !\nChoisissez un territoire attaquant vous appartenant (comportant plus d'1 unité)\nPour manoeuvrer, cliquez sur MANOEUVREZ (en haut à gauche).";
        ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.ACTION);
        setSousEtat(SousEtat.ENVAHISSEZ);
        setSousEtatEnvahissez(SousEtatEnvahissez.CHOIX_TERRITOIRE_SOURCE);
        updateChatZone(chatMessage);
        btnManoeuvrer.setDisable(false);
        this.setCarteCliquable(true);
    }


    private void clickSurManoeuvrer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Veuillez confirmer que vous souhaitez ne pas faire d'invasion supplémentaire");
        alert.setContentText("Veuillez confirmer que vous souhaitez ne pas faire d'invasion supplémentaire");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            manoeuvrer();
        }

    }

    private void manoeuvrer() {

        String messagePourChat = "MANOEUVRE: Choisissez un territoire source vous appartenant (comportant plus d'1 unité)\nSi vous ne souhaiter faire de manoeuvre, vous pouvez passer cette étape en cliquant sur PASSER (en haut à gauche).";
        ChatMessage chatMessage = new ChatMessage(messagePourChat, ChatMessage.ChatMessageType.ACTION);
        updateChatZone(chatMessage);
        setSousEtat(SousEtat.MANOEUVREZ);
        setSousEtatManoeuvrez(SousEtatManoeuvrez.CHOIX_TERRITOIRE_SOURCE);
        updateChatZone(chatMessage);
        btnManoeuvrer.setText("PASSER LA MANOEUVRE");
        btnManoeuvrer.setOnMouseClicked(e -> {
            this.clickSurPasserLaManoeuvre();
        });
        this.setCarteCliquable(true);

    }

    public void fromJSAChoisiUnTerritoireSourceManoeuvre(String s) {
        this.setCarteCliquable(false);
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        if (ter.getAppartientAJoueur() == clientConnexion) {
            //OK, le territoire appartient bien à notre client !
            if (ter.getNombreDeTroupes() > 1) {
                //OK, le territoire comporte bien au moins 2 troupes
                String message = "Vous avez choisi de manoeuvrer depuis le territoire " + ter.getNom().name() + "\nVeuillez maintenant chosir un territoire relié, vous appartenant, sur lequel amener vous troupes";
                ChatMessage chatMessage = new ChatMessage(message, ChatMessage.ChatMessageType.ACTION);
                updateChatZone(chatMessage);
                mettreAJourUnTerritoireSurLaCarte(ter, true, true);
                clientConnexion.getManoeuvreEnCours().setTerritoireSource(ter);
                this.setSousEtatManoeuvrez(SousEtatManoeuvrez.CHOIX_TERRITOIRE_CIBLE);
                this.setCarteCliquable(true);
            } else {
                ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] N'A QU'UNE SEULE ARMEE\nMettez vos lunettes en choisissez en un qui en a au moins deux !", ChatMessage.ChatMessageType.ERREUR);
                updateChatZone(chatMessage);
                this.setCarteCliquable(true);
            }
        } else {
            ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] APPARTIENT A [" + ter.getAppartientAJoueur().getNom() + "]\nMettez vos lunettes en choisissez en un qui vous appartient !", ChatMessage.ChatMessageType.ERREUR);
            updateChatZone(chatMessage);
            this.setCarteCliquable(true);
        }
    }


    public void fromJSAChoisiUnTerritoireCibleManoeuvre(String s) {
        this.setCarteCliquable(false);
        Territoire ter = clientConnexion.getRiskGOTterritoires().getTerritoireParNomStr(s);
        Territoire terSource = clientConnexion.getManoeuvreEnCours().getTerritoireSource();
        if (ter == terSource) {
            //Cas ou le joueur reclique sur le même territoire (signifie qu'il veut changer son choix)
            mettreAJourUnTerritoireSurLaCarte(terSource, false, true);
            manoeuvrer();
        } else {
            if (ter.getAppartientAJoueur() == clientConnexion) {
                //OK, le territoire appartient  à notre client !
                if (clientConnexion.getRiskGOTterritoires().territoiresSontRelies(terSource, ter)) {
                    //OK, le territoire est bien connecté !
                    mettreAJourUnTerritoireSurLaCarte(ter, true, false);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Veuillez confirmer la manoeuvre");
                    alert.setContentText("Veuillez confirmer que vous souhaitez faire une manoeuvre :\n- Depuis votre  territoire " + terSource.getNom().name() + " comportant " + terSource.getNombreDeTroupes() + " troupes \n- Sur le territoire " + s + " comportant  " + ter.getNombreDeTroupes() + " troupes \nSi vous cliquez OK, il ne sera plus possible d'annuler !");
                    Optional<ButtonType> option = alert.showAndWait();
                    clientConnexion.getManoeuvreEnCours().setTerritoireCible(ter);
                    if (option.get() == ButtonType.OK) {
                        afficherUneFenetreManoeuvre(terSource, ter, false);
                        btnManoeuvrer.setDisable(true);
                    } else {
                        mettreAJourUnTerritoireSurLaCarte(terSource, false, true);
                        mettreAJourUnTerritoireSurLaCarte(ter, false, true);
                        manoeuvrer();
                    }
                } else {
                    ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] N'EST PAS RELIE A " + terSource.getNom().name() + "\nMettez vos lunettes en choisissez en un qui soit relié !", ChatMessage.ChatMessageType.ERREUR);
                    updateChatZone(chatMessage);
                    this.setCarteCliquable(true);
                }
            } else {
                ChatMessage chatMessage = new ChatMessage("TERRITOIRE [" + ter.getNom().name() + "] NE VOUS APPARTIENT PAS!\nVous voulez vraiment donner des troupes à vos ennemis ?!", ChatMessage.ChatMessageType.ERREUR);
                updateChatZone(chatMessage);
                this.setCarteCliquable(true);
            }
        }
    }

    public void afficherUneFenetreManoeuvre(Territoire terSource, Territoire terCible, boolean pModeInvasion)
    {
        if (pModeInvasion){this.btnManoeuvrer.setDisable(true);}
        ManoeuvreGui manoeuvreGui = new ManoeuvreGui(primaryStage, terSource, terCible, clientConnexion, pModeInvasion);
        manoeuvreGui.setMinWidth(720);
        zoomAvant=webView.getZoom();
        webView.setZoom(0.33);
        this.invasionManoeuvresArea.getChildren().add(manoeuvreGui);
    }

    public void masquerLaFenetreManoeuvre(ManoeuvreGui manoeuvreGui)
    {
        this.invasionManoeuvresArea.getChildren().remove(manoeuvreGui);
        webView.setZoom(zoomAvant);
    }


    private void clickSurPasserLaManoeuvre() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Veuillez confirmer que vous souhaitez ne pas faire votre manoeuvre");
        alert.setContentText("Veuillez confirmer que vous souhaitez ne pas faire votre manoeuvre");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            passerLaManoeuvre();
        }
    }

    private void passerLaManoeuvre() {
        btnManoeuvrer.setDisable(true);
        this.setCarteCliquable(false);
        clientConnexion.sendCommand(ClientCommandes.JOUEUR_PASSE_LA_MANOEUVRE, "");

    }

    public void afficheFenetreNouvelleInvasion() {


        String message = "LANCE UNE INVASION DEPUIS " + clientConnexion.getInvasionEnCours().getTerritoireSource().getNom().name() + " CONTRE " + clientConnexion.getInvasionEnCours().getTerritoireCible().getAppartientAJoueur().getNomAtFamille() + " SUR " + clientConnexion.getInvasionEnCours().getTerritoireCible().getNom().name();
        ChatMessage chatmessage = new ChatMessage(message, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, clientConnexion.getInvasionEnCours().getTerritoireSource().getAppartientAJoueur());
        updateChatZone(chatmessage);
        afficherLaFenetreInvasion();


    }

    private double zoomAvant;

    private void afficherLaFenetreInvasion() {



        zoomAvant = webView.getZoom();
        webView.setZoom(0.33);
        if (invasionManoeuvresArea.getChildren().contains(invasionGuiCourante)) {
            invasionManoeuvresArea.getChildren().remove(invasionGuiCourante);
        }
        invasionGuiCourante = new InvasionGui(primaryStage, clientConnexion.getInvasionEnCours(), clientConnexion);
        invasionGuiCourante.setVisible(true);
        invasionManoeuvresArea.getChildren().add(invasionGuiCourante);
        btnManoeuvrer.setDisable(true);



    }

    public void masquerLaFenetreInvasion() {
        invasionGuiCourante.setVisible(false);
        invasionManoeuvresArea.getChildren().remove(invasionGuiCourante);
        if (clientConnexion.getInvasionEnCours().getJoueurAttaquant()==clientConnexion){
            btnManoeuvrer.setDisable(false);
        }
        webView.setZoom(zoomAvant);

    }


    public void continuerUneInvasion() {
        String message = "CONTINUE L'INVASION  DEPUIS " + clientConnexion.getInvasionEnCours().getTerritoireSource().getNom().name() + " CONTRE " + clientConnexion.getInvasionEnCours().getTerritoireCible().getAppartientAJoueur().getNomAtFamille() + " SUR " + clientConnexion.getInvasionEnCours().getTerritoireCible().getNom().name();
        ChatMessage chatmessage = new ChatMessage(message, ChatMessage.ChatMessageType.INFOCHAT_IMPORTANTE, clientConnexion.getInvasionEnCours().getTerritoireSource().getAppartientAJoueur());
        updateChatZone(chatmessage);
        afficherLaFenetreInvasion();
    }


    public void setCarteCliquable(boolean pCarteCliquable) {
        this.carteCliquable = pCarteCliquable;
        if (carteCliquable) {
            this.webViewContainerStackPane.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

        } else {
            this.webViewContainerStackPane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        }
    }


    public void sendToJVS(String msg) {
        System.out.println("MESSAGE ENVOYE A JAVASCRIPT : " + msg);
        webView.getEngine().executeScript(msg);

    }


}
