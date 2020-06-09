package common.objects.cartes;

import common.objects.Joueur;

import java.util.ArrayList;
import java.util.Collections;

public class CartesObjectifs {


    private ArrayList<CarteObjectif> carteObjectifList;
    private int indexCourant;
    public CartesObjectifs()
    {
        this.carteObjectifList = new ArrayList<>();
        this.initCartesObjectifs();
        Collections.shuffle(carteObjectifList);
        indexCourant=-1;
    }


    private void initCartesObjectifs()
    {
        carteObjectifList.add(new CarteObjectif("TEST",1,1));

    }


    public CarteObjectif piocher(Joueur joueur)
    {
        indexCourant++;
        if (indexCourant<carteObjectifList.size()){
            CarteObjectif cartePiochee = carteObjectifList.get(indexCourant);
            joueur.aPiocheUneCarteObjectif(cartePiochee);
            return  cartePiochee;

        }
        else{
            System.out.println("Il n'y a plus de cartes objectifs dans la pioche !!" );
            return null;
        }

    }

}
