package gui;

import applogic.objects.JoueurClient;
import common.objects.Famille;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class FamilleGui extends HBox {
    public Famille getFamille() {
        return famille;
    }

    private Famille famille;
    private ImageView imgView;
    private MainView mainView;
    private Label nomFamille = new Label();
    private Label nomJoueur = new Label();
    private Label nombreDeTerritoires = new Label();
    private Label nombreDeChateaux = new Label();
    private Label nombreDePorts = new Label();
    private Label nombreArgent = new Label();
    private Label nombrePointDeVictoire = new Label();
    private Label nombreDeTroupes = new Label();
    private Label nombreDeTroupesAPlacer = new Label();
    private Label ordreDeJeu = new Label();
    private Label capitaleNom = new Label();
    private Label bonusRegion = new Label();
    private Label renfortsAuProchainTour = new Label();
    private Label nombreDeCartesTerritoires = new Label();
    private Label nombreDeCartesObjectifs = new Label();
    private VBox contentZoneImg;
    private VBox contentZone;
    private VBox contentZone2;



    private boolean isMe;

    public JoueurClient getJoueurClient() {
        return joueurClient;
    }

    public void setJoueurClient(JoueurClient joueurClient) {
        this.joueurClient = joueurClient;
        if (joueurClient==mainView.getClientConnexion()){
            isMe=true;
        }
        this.nomJoueur.setText("Joueur: " + joueurClient.getNom());
        if (!isMe){
            this.unHighLight();
        }
        else{
            this.contentZoneImg.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        }
    }

    public void updateLabels()
    {
        nombreDeTerritoires.setText("Nb de Territoires = " + famille.getJoueur().getNombreDeTerritoires());
        nombreDeChateaux.setText("Nb de Chateaux = "+ famille.getJoueur().getNombreDeChateaux());
        nombreDePorts.setText("Nb Ports  = " + famille.getJoueur().getNombreDePorts());
        nombreArgent.setText("Argent = "+ famille.getJoueur().getArgent());
        nombrePointDeVictoire.setText("Points de Victoires = 0 / 10");
        nombreDeTroupesAPlacer.setText("Nombre de troupes à placer = " + famille.getJoueur().getNbTroupeAPlacer());
        nombreDeCartesTerritoires.setText("Nombre de cartes ter. = " + famille.getJoueur().getCartesTerritoires().size());
        nombreDeTroupes.setText("Nombre de troupes = " + famille.getJoueur().getNbTroupes());
        nombreDeCartesObjectifs.setText("Nombre de cartes obj. = " + famille.getJoueur().getCartesObjectif().size());
        bonusRegion.setText("Bonus region = " + famille.getJoueur().calculerBonusRegion());
        renfortsAuProchainTour.setText("Renforts est.="+famille.getJoueur().calculerNombreDeRenfortsDeBase());
        if (famille.getJoueur().controleSaCapitale())
        {
            capitaleNom.setText("Capitale: " + this.famille.getCapitale().getNom().name());
            capitaleNom.setTextFill(Color.BLACK);
        }
        else
            {
            capitaleNom.setTextFill(Color.RED);
            capitaleNom.setText("Capitale: " + this.famille.getCapitale().getNom().name()+ " PERDUE!!");
        }
    }


    private JoueurClient joueurClient;

    public FamilleGui(Famille pFam, MainView pMainView)
    {
        this.contentZoneImg = new VBox();
        this.contentZone = new VBox();
        this.contentZone2 = new VBox();
        this.mainView=pMainView;
        this.famille = pFam;
        this.setSpacing(5);
        this.setStyle("-fx-background-color:"+ pFam.getWebColor());
        this.contentZoneImg.setStyle("-fx-background-color:"+ pFam.getWebColor());
        this.contentZone.setStyle("-fx-background-color:"+ pFam.getWebColor());
        this.contentZone2.setStyle("-fx-background-color:"+ pFam.getWebColor());
        Image image = new Image(getClass().getResourceAsStream("/Maison_"+pFam.getFamilyName().name()+"_96.png"));
        imgView = new ImageView(image);
        imgView.setDisable(true);
        isMe=false;
        ordreDeJeu.setText("Ordre de jeu = " + pFam.getOrdreDeJeu());
        nomJoueur.setText("Joueur: A déterminer");
        nomJoueur.setStyle("-fx-font-weight: bold");
        nomFamille.setText(pFam.getFamilyName().name());
        nomFamille.setStyle("-fx-font-weight: bold");
        nombreDeTerritoires.setText("Nb de Territoires = 0");
        nombreDeChateaux.setText("Nb de Chateaux = 0");
        nombreDePorts.setText("Nb Ports  = 0");
        nombreArgent.setText("Argent = "+ pFam.getArgentDeDepart());
        nombrePointDeVictoire.setText("Points de Victoires = 0 / 10");
        nombrePointDeVictoire.setStyle("-fx-font-weight: bold");
        nombreDeTroupesAPlacer.setText("Nombre de troupes à placer = 0");
        nombreDeTroupes.setText("Nombre de troupes = 0");
        nombreDeCartesTerritoires.setText("Nombres de cartes ter. = 0");
        nombreDeCartesObjectifs.setText("Nombre de cartes obj. = 0");
        capitaleNom.setText("Capitale: " + pFam.getCapitale().getNom().name());
        bonusRegion.setText("Renforts est.= 0");
        bonusRegion.setText("Bonus region = 0");
        capitaleNom.setTextFill(Color.BLACK);
        contentZoneImg.getChildren().addAll(nomFamille,imgView);
        contentZone.getChildren().addAll(nomJoueur, ordreDeJeu , nombreArgent, nombrePointDeVictoire, capitaleNom, nombreDeCartesTerritoires);
        contentZone2.getChildren().addAll(nombreDeTerritoires, nombreDeChateaux, nombreDePorts, nombreDeTroupes, nombreDeTroupesAPlacer, renfortsAuProchainTour, bonusRegion);
        this.getChildren().addAll(contentZoneImg, contentZone, contentZone2);
        this.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        imgView.setOnMouseClicked(e->{
            clickOnImageView();
        });

    }

    public void peutEtreChoisie(){
        this.contentZoneImg.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        this.imgView.setCursor(Cursor.HAND);
        this.imgView.setDisable(false);
    }

    public void unHighLight(){
        if (this.joueurClient==null)
        {
            this.contentZoneImg.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
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


    public void setActif()
    {
        this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
    }

    public void setInactif()
    {
        this.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

    }



}
