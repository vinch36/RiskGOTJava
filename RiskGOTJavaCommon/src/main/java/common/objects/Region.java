package common.objects;

import java.util.ArrayList;

public class Region {
    public enum RegionNames
    {
        NORD,CONFLANS, LES_ILES_DE_FER, VALE_DARRYN, TERRES_DE_LA_COURONNE, TERRES_DE_LOUEST, TERRES_DE_LORAGE, BIEF, DORNE
    }

    private RegionNames nom;
    private ArrayList<Territoire> territoires;

    public RegionNames getNom() {
        return nom;
    }

    public void setNom(RegionNames nom) {
        this.nom = nom;
    }

    public ArrayList<Territoire> getTerritoires() {
        return territoires;
    }

    public void setTerritoires(ArrayList<Territoire> territoires) {
        this.territoires = territoires;
    }

    public int getBonusTroupes() {
        return bonusTroupes;
    }

    public void setBonusTroupes(int bonusTroupes) {
        this.bonusTroupes = bonusTroupes;
    }

    private int bonusTroupes;

    public Region(RegionNames pNom, int pBonusTroupes)
    {
        this.territoires=new ArrayList<>();
        this.nom = pNom;
        this.bonusTroupes=pBonusTroupes;
    }

    public int getNbTerritoire()
    {
        return this.territoires.size();
    }

    public void ajouterUnTerritoire(Territoire pTerritoire)
    {
        this.territoires.add(pTerritoire);
    }

    public boolean appartientComplementAUnJoueur(Joueur pJoueur)
    {
     for (Territoire t :territoires)
     {
       if (t.getAppartientAJoueur()!=pJoueur)
       {
           return false;
           //Un territoire au moins de la region n'appartient pas au joueur, donc il n'a pas la region complète
       }
     }
     return true;
    }


}
