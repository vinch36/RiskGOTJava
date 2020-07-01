package gui.de;

import common.objects.DeTypeValeur;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class DeGui extends VBox {

    private ImageView de;
    private Label lblValeur;
    public enum CouleurDe{ROUGE,NOIR}


    private HashMap<Integer, Image> faces;


    public int getDureeAnimationMs() {
        return dureeAnimationMs;
    }

    public void setDureeAnimationMs(int dureeAnimationMs) {
        this.dureeAnimationMs = dureeAnimationMs;
    }

    public int getNbChangementAnimation() {
        return nbChangementAnimation;
    }

    public void setNbChangementAnimation(int nbChangementAnimation) {
        this.nbChangementAnimation = nbChangementAnimation;
    }

    private int dureeAnimationMs;
    private int nbChangementAnimation;


    private CouleurDe couleur;
    private DeTypeValeur.TypeDe type;
    private int valeurCourante;

    public int getBonus() {
        return bonus;
    }

    private int bonus;


    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
        this.setHeight(taille);
        this.setWidth(taille);
        this.de.setFitHeight(taille);
        this.de.setFitWidth(taille);

    }

    private int taille;

    public CouleurDe getCouleur() {
        return couleur;
    }

    public void setCouleur(CouleurDe couleur) {
        this.couleur = couleur;
    }

    public DeTypeValeur.TypeDe getType() {
        return type;
    }

    public void setType(DeTypeValeur.TypeDe type) {
        this.type = type;
    }

    public int getValeurCourante() {
        return valeurCourante;
    }

    public void setValeurCourante(int valeur) {
        this.valeurCourante = valeur;
    }

    private void initFaces(int pValeurInitiale)
    {
        String pathInit = "/img/de/de_"+type.name()+"_"+pValeurInitiale+"_"+couleur.name()+".png";
        pathInit=pathInit.toLowerCase();
        Image image = new Image(getClass().getResourceAsStream(pathInit));
        this.de = new ImageView(image);
        this.de.setFitHeight(taille);
        this.de.setFitWidth(taille);
        this.getChildren().add(de);

        for (int val = 1;val<7;val++)
        {
            String path="/img/de/de_"+type.name()+"_"+val+"_"+couleur.name()+".png";
            path=path.toLowerCase();
            faces.put(val, new Image(getClass().getResourceAsStream(path)));
        }
        if (this.type== DeTypeValeur.TypeDe.HUIT){
            for (int val = 7;val<9;val++)
            {
                String path="/img/de/de_"+type.name()+"_"+val+"_"+couleur.name()+".png";
                path=path.toLowerCase();
                faces.put(val, new Image(getClass().getResourceAsStream(path)));
            }
        }
    }

    public int getMaxValue() {
        return maxValue;
    }

    private int maxValue = 6;

    public DeGui(CouleurDe couleur, DeTypeValeur.TypeDe type, int pValeurInitiale, int taille, int pDureeAnimation, int pNbChangementAnimation, boolean pDoitEtreLance) {
        super();
        faces=new HashMap<>();
        this.lblValeur = new Label();
        this.taille=taille;
        this.setHeight(taille);
        this.setWidth(taille);
        this.couleur = couleur;
        this.type = type;
        if (type== DeTypeValeur.TypeDe.SIX) {
            maxValue=6;
        }
        else if (type == DeTypeValeur.TypeDe.HUIT)
        {
            maxValue=8;
        }
        this.valeurCourante = pValeurInitiale;
        this.dureeAnimationMs = pDureeAnimation;
        this.nbChangementAnimation = pNbChangementAnimation;
        this.doitEtreLance = pDoitEtreLance;
        this.aEteLance=false;
        this.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.initFaces(pValeurInitiale);
        this.lblValeur.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        this.getChildren().add(lblValeur);
    }


    public boolean isaEteLance() {
        return aEteLance;
    }

    public void setaEteLance(boolean aEteLance) {
        this.aEteLance = aEteLance;
    }

    private boolean aEteLance;

    public boolean isDoitEtreLance() {
        return doitEtreLance;
    }

    public void setDoitEtreLance(boolean doitEtreLance) {
        this.doitEtreLance = doitEtreLance;
    }

    private boolean doitEtreLance;


    public void animate() {
        Integer durationMsDunAffichage = this.dureeAnimationMs / this.nbChangementAnimation;

        Timeline timeline = new Timeline();
        int a = 0;
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), e -> this.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))))));
        for (int i = 0; i < nbChangementAnimation - 1; i++) {
            Integer val = (int) (1 + maxValue * Math.random());

            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(a), e -> changeValeur(val)));
            a = a + durationMsDunAffichage;
        }
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(a+1), e -> this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))))));
        timeline.setOnFinished(e -> aJoue());
        timeline.play();
    }



    public void changeValeur(int pNouvellevaleur)
    {
        this.de.setImage(faces.get(pNouvellevaleur));
        //System.out.println ("Change Valeur:" +pNouvellevaleur);
        this.valeurCourante=pNouvellevaleur;
        this.lblValeur.setText(String.valueOf(pNouvellevaleur));
    }

    public void changeValeurAvecBonus(int pNouvellevaleur, int pBonus)
    {
        changeValeur(pNouvellevaleur);
        this.bonus=pBonus;
        int total=bonus+pNouvellevaleur;
        this.lblValeur.setText(String.valueOf(pNouvellevaleur+"+"+pBonus+"="+total));
    }

    public int getValeurAvecBonus(){
        return valeurCourante+bonus;
    }

    public void mettreEnEvidence()
    {
        this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
    }

    public void pasEnEvidence() {
        this.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
    }

    public void aJoue()
    {
        this.aEteLance=true;
        this.doitEtreLance=false;
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
    }


    public void deVainqueur()
    {
        this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));
    }

    public void dePerdant()
    {
        this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));
    }

    public void peutEtreMaxe()
    {
        this.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));
    }




}
