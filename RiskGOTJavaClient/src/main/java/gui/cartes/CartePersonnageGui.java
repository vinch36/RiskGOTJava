package gui.cartes;

import common.objects.cartes.CartePersonnage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CartePersonnageGui extends CarteGui {

    public CartePersonnage getCartePersonnage() {
        return cartePersonnage;
    }

    CartePersonnage cartePersonnage;

    public CartePersonnageGui(CartePersonnage pCartePersonnage) {
        super("/img/cartes/personnages/" + pCartePersonnage.getFamille().getFamilyName().name().toLowerCase() + "/" + pCartePersonnage.getName().name().toLowerCase() + ".png");
        this.cartePersonnage = pCartePersonnage;
    }

    public void afficherCommeUtilisee()
    {
        Image img  = new Image(getClass().getResourceAsStream("/img/cartes/personnages/" + cartePersonnage.getFamille().getFamilyName().name().toLowerCase() + "/carte.png"),100,150,false,false);
        this.imageView.setImage(img);
        this.setSelectionnee(false);
    }

    public void actualiser()
    {
        Image img  = new Image(getClass().getResourceAsStream("/img/cartes/personnages/" + cartePersonnage.getFamille().getFamilyName().name().toLowerCase() + "/" + cartePersonnage.getName().name().toLowerCase() + ".png"),100,150,false,false);
        this.imageView.setImage(img);
    }

}
