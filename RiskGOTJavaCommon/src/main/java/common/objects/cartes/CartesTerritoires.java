package common.objects.cartes;

import common.objects.Joueur;
import common.objects.Territoire;
import common.objects.Territoires;

import java.util.ArrayList;
import java.util.Collections;

public class CartesTerritoires {


    private ArrayList<CarteTerritoire> cartesTerritoires;

    public CartesTerritoires(Territoires pTerritoires)
    {
        this.cartesTerritoires = new ArrayList<>();
        this.initCartesTerritoires(pTerritoires);
        Collections.shuffle(cartesTerritoires);
        indexCourant=-1;
    }

    private void initCartesTerritoires(Territoires territoires)
    {
        //North (13)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.SKAGOS), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.DON), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.KARHOLD), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.FORT_TERREUR), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.ILE_AUX_OURS), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.TERTRES), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.ROCHES), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.NECK), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN), CarteTerritoire.UniteSpeciale.CHEVALIER));

        //RiverLands (5)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.LES_JUMEAUX), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.TRIDENT), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.VIVESAIGUES), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.HARRENHAL), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.PIERRE_MOUTIER), CarteTerritoire.UniteSpeciale.CHEVALIER));

        //Iron Islands (2)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.HARLOI), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.PYK), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));

        //Westerlands (5)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.FALAISE), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.LA_DENT_DOR), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.MONTARGENT), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.CRAKENHALL), CarteTerritoire.UniteSpeciale.FORTIFICATION));

        //Vale of Arryn (4)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.DOIGTS), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.LES_EYRIE), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE), CarteTerritoire.UniteSpeciale.CHEVALIER));

        //Crownlands (4)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.PRESQU_ILE_DE_CLAQUEPINCE), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.BOIS_DU_ROI), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));


        //StormLands (4)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.ILE_DE_TORTH), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.BOIS_LA_PLUIE), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.MARCHES_DE_DORNE), CarteTerritoire.UniteSpeciale.CHEVALIER));


        //Bief(4)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.ROUTE_DE_LOCEAN), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.NERA), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.MANDER), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.TROIS_TOURS), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.LA_TREILLE), CarteTerritoire.UniteSpeciale.FORTIFICATION));

        //Dorne (4)
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_ROUGES), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.LE_GRES), CarteTerritoire.UniteSpeciale.FORTIFICATION));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.SANG_VERT), CarteTerritoire.UniteSpeciale.CHEVALIER));
        cartesTerritoires.add(new CarteTerritoire(territoires.getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION), CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
    }


    private int indexCourant;
    public CarteTerritoire piocher(Joueur joueur)
    {
        indexCourant++;
        if (indexCourant<cartesTerritoires.size()){
            System.out.println("Carte Territoire piochée : " + cartesTerritoires.get(indexCourant).getTerritoire().getNom().name() + " - " +  cartesTerritoires.get(indexCourant).getUniteSpeciale().name());
            CarteTerritoire cartePiochee =cartesTerritoires.get(indexCourant);
            joueur.aPiocheUneCarteTerritoire(cartePiochee);
            return  cartePiochee;

        }
        else{
            System.out.println("Il n'y a plus de cartes territoires dans la pioche !!" );
            return null;
        }

    }


    public CarteTerritoire piocher(Joueur pJoueur, String pTerritoireName){
        for (CarteTerritoire carteTerritoire : cartesTerritoires){
            if (pTerritoireName.equals(carteTerritoire.getTerritoire().getNom().name())){
                pJoueur.aPiocheUneCarteTerritoire(carteTerritoire);
                return carteTerritoire;
            }
        }
        return null;

    }


    public int getBonusCombinaisonDeTroisCartesTerritoires(CarteTerritoire carteTerritoire1, CarteTerritoire carteTerritoire2, CarteTerritoire carteTerritoire3) {
        if (carteTerritoire1.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.CHEVALIER && carteTerritoire2.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.CHEVALIER && carteTerritoire3.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.CHEVALIER) {
            return 4;
        } else if (carteTerritoire1.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE && carteTerritoire2.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE && carteTerritoire3.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE) {
            return 5;
        } else if (carteTerritoire1.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.FORTIFICATION && carteTerritoire2.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.FORTIFICATION && carteTerritoire3.getUniteSpeciale() == CarteTerritoire.UniteSpeciale.FORTIFICATION) {
            return 6;
        } else if (carteTerritoire1.getUniteSpeciale() != carteTerritoire2.getUniteSpeciale() && carteTerritoire1.getUniteSpeciale() != carteTerritoire3.getUniteSpeciale() && carteTerritoire2.getUniteSpeciale() != carteTerritoire3.getUniteSpeciale()) {
            return 7;
        } else {
            return 0;
        }
    }

    public CarteTerritoire getCarteTerritoireParNom(Territoire.TerritoireNames pTerritoireName)
    {
        for (CarteTerritoire carteTerritoire : cartesTerritoires){
            if (pTerritoireName==carteTerritoire.getTerritoire().getNom()){
                return carteTerritoire;
            }
        }
        return null;

    }


}
