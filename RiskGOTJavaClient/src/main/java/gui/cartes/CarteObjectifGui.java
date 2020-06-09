package gui.cartes;

import common.objects.cartes.CarteObjectif;
import common.objects.cartes.CarteTerritoire;

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


}
