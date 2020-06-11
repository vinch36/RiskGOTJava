package gui.cartes;

import common.objects.cartes.CarteObjectif;
import common.objects.cartes.CarteTerritoire;
import javafx.scene.control.Tooltip;

public class CarteObjectifGui extends CarteGui {

    public CarteObjectif getCarteObjectif() {
        return carteObjectif;
    }

    private CarteObjectif carteObjectif;


    public CarteObjectifGui(CarteObjectif pCarteObjectif)
    {
        super("/cartes/objectifs/"+pCarteObjectif.getIdAsStr() +".png");
        this.carteObjectif=pCarteObjectif;
    }

    public CarteObjectifGui(CarteObjectif pCarteObjectif, int requestedWidth, int requestedHeight)
    {
        super("/cartes/objectifs/"+pCarteObjectif.getIdAsStr() +".png", requestedWidth, requestedHeight);
        this.carteObjectif=pCarteObjectif;
    }





}
