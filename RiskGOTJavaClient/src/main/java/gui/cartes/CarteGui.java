package gui.cartes;

import gui.ImgCliquable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import network.ClientConnexion;

public class CarteGui extends ImgCliquable {

    public CarteGui(String pathToImage)
    {
        super(pathToImage, 75, 112);
    }

}
