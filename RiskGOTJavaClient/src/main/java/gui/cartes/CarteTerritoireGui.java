package gui.cartes;

import common.objects.cartes.CarteTerritoire;
import javafx.scene.control.Label;
import network.ClientConnexion;

public class CarteTerritoireGui extends CarteGui {

    public CarteTerritoire getCarteTerritoire() {
        return carteTerritoire;
    }

    private CarteTerritoire carteTerritoire;

    public CarteTerritoireGui(CarteTerritoire pCarteTerritoire)
    {
        super("/cartes/territoires/"+pCarteTerritoire.getTerritoire().getNom().name().toLowerCase()+".png");
        this.carteTerritoire=pCarteTerritoire;
    }


}
