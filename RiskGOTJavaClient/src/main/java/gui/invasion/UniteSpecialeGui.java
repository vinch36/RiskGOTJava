package gui.invasion;

import common.objects.cartes.CarteTerritoire;
import gui.ImgCliquable;
import gui.cartes.CarteGui;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.ClientConnexion;

public class UniteSpecialeGui extends ImgCliquable {


    public CarteTerritoire.UniteSpeciale getUniteSpeciale() {
        return uniteSpeciale;
    }

    private CarteTerritoire.UniteSpeciale uniteSpeciale;
    public UniteSpecialeGui(CarteTerritoire.UniteSpeciale pUniteSpeciale) {
        super("/img/unitesspeciales/"+pUniteSpeciale.name().toLowerCase()+".png", 40,40);
        this.uniteSpeciale=pUniteSpeciale;

    }
}
