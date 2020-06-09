package common.objects.cartes;

import com.sun.istack.internal.Nullable;
import common.objects.Joueur;
import common.objects.Territoire;

public class CarteTerritoire {

    public enum UniteSpeciale {CHEVALIER, FORTIFICATION, ENGIN_DE_SIEGE}

    public Territoire getTerritoire() {
        return territoire;
    }

    public UniteSpeciale getUniteSpeciale() {
        return uniteSpeciale;
    }

    private Territoire territoire;
    private UniteSpeciale uniteSpeciale;


    public CarteTerritoire(Territoire pTerritoire, UniteSpeciale pUniteSpeciale){
        this.territoire=pTerritoire;
        this.uniteSpeciale=pUniteSpeciale;
    }


    private Territoire.TerritoireNames getTerritoireName()
    {
        return this.territoire.getNom();
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(@Nullable Joueur joueur) {
         this.joueur = joueur;
    }

    private Joueur joueur;






}
