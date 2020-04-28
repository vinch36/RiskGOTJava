package common.objects;

import java.util.ArrayList;

public class Region {
    public enum RegionNames
    {
        THE_NORTH,THE_RIVERLANDS, THE_IRON_ISLANDS, THE_VALE_OF_ARRYN, THE_CROWNLANDS, THE_WESTERLANDS, THE_STORMLANDS, THE_REACH, DORNE
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


}
