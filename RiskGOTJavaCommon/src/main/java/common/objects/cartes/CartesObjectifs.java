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
        carteObjectifList.add(new CarteObjectif("TEST",4,1));
        carteObjectifList.add(new CarteObjectif("TEST",4,2));
        carteObjectifList.add(new CarteObjectif("TEST",4,3));
        carteObjectifList.add(new CarteObjectif("TEST",4,4));
        carteObjectifList.add(new CarteObjectif("TEST",4,5));
        carteObjectifList.add(new CarteObjectif("TEST",4,6));
        carteObjectifList.add(new CarteObjectif("TEST",4,7));
        carteObjectifList.add(new CarteObjectif("TEST",4,8));
        carteObjectifList.add(new CarteObjectif("TEST",4,9));
        carteObjectifList.add(new CarteObjectif("TEST",3,10));
        carteObjectifList.add(new CarteObjectif("TEST",3,11));
        carteObjectifList.add(new CarteObjectif("TEST",3,12));
        carteObjectifList.add(new CarteObjectif("TEST",3,13));
        carteObjectifList.add(new CarteObjectif("TEST",3,14));
        carteObjectifList.add(new CarteObjectif("TEST",3,15));
        carteObjectifList.add(new CarteObjectif("TEST",3,16));
        carteObjectifList.add(new CarteObjectif("TEST",3,17));
        carteObjectifList.add(new CarteObjectif("TEST",3,18));
        carteObjectifList.add(new CarteObjectif("TEST",2,19));
        carteObjectifList.add(new CarteObjectif("TEST",2,20));
        carteObjectifList.add(new CarteObjectif("TEST",2,21));
        carteObjectifList.add(new CarteObjectif("TEST",2,22));
        carteObjectifList.add(new CarteObjectif("TEST",2,23));
        carteObjectifList.add(new CarteObjectif("TEST",2,24));
        carteObjectifList.add(new CarteObjectif("TEST",2,25));
        carteObjectifList.add(new CarteObjectif("TEST",2,26));
        carteObjectifList.add(new CarteObjectif("TEST",2,27));
        carteObjectifList.add(new CarteObjectif("TEST",1,28));
        carteObjectifList.add(new CarteObjectif("TEST",1,29));
        carteObjectifList.add(new CarteObjectif("TEST",1,30));
        carteObjectifList.add(new CarteObjectif("TEST",1,31));
        carteObjectifList.add(new CarteObjectif("TEST",1,32));
        carteObjectifList.add(new CarteObjectif("TEST",1,33));
        carteObjectifList.add(new CarteObjectif("TEST",1,34));
        carteObjectifList.add(new CarteObjectif("TEST",1,35));
        carteObjectifList.add(new CarteObjectif("TEST",1,36));
    }


    public CarteObjectif getCarteObjectifParIdStr(String pIdStr)
    {
        for (CarteObjectif carte : this.carteObjectifList) {
            if (carte.getIdAsStr().equals(pIdStr))
            {
                return carte;
            }

        }
        return null;
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
