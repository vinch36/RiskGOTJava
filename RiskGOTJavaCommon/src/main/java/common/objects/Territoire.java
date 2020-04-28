package common.objects;

import java.util.ArrayList;

public class Territoire {

    public enum TerritoireNames {
        //North (13)
        SKAGOS,THE_GIFT,KARHOLD,THE_DREADFORT,WINTERFELL,BEAR_ISLAND,WOLFSWOOD,WIDOWS_WATCH,WHITE_HARBOR,BARROWLANDS,STONLEY_SHORE,THE_NECK,CAPE_KRAKEN,
        //RiverLands (5)
        THE_TWINS,THE_TRIDENT,RIVERRUN,MARRENHAL,STONEY_KEPT,
        //Iron Islands (2)
        HARLAW,PYKE,
        //Westerlands (5)
        THE_CRAG,GOLDEN_TOOTH,CASTERLT_ROCK,SILVERKILL,CRAKENHALL,
        //Vale of Arryn (4)
        MOUNTAINS_OF_THE_MOON,THE_FINGERS,THE_EYRIE,GULLTOWN,
        //Crownlands (4)
        CRACKCLAW_POINT,KINGS_LANDING,DRAGONSTONE,KINGSWOOD,
        //StormLands (4)
        STORMS_END,TARTH,RAINWOOD,DORNISH_MARCHES,
        //Reach (7)
        SEAROAD_MARSHES,BLACKWATER_RUSH,THE_MANDER,HIGHGARDEN,OLDTOWN,THREE_TOWERS,THE_ARBOR,
        //Dorne (4)
        RED_MOUNTAINS,SANDSTONE,GREENBLOD,SUNSPEAR

    }


    private TerritoireNames nom;
    private Joueur appartientAJoueur;
    private boolean chateau;
    private boolean port;
    private Region region;
    private Famille capitaleDe;


    public TerritoireNames getNom() {
        return nom;
    }

    public int getNombreDeTroupes() {
        return nombreDeTroupes;
    }

    private int nombreDeTroupes = 0;


    public Territoire(TerritoireNames pNom, boolean pChateau, boolean pPort, Famille pCapitaleDe, Region pRegion) {
        this.nom = pNom;
        this.chateau = pChateau;
        this.port = pPort;
        this.capitaleDe = pCapitaleDe;
        this.region = pRegion;
        region.ajouterUnTerritoire(this);
        if (pCapitaleDe != null) capitaleDe.setCapitale(this);
        }

    public void setNom(TerritoireNames pNom) {
        this.nom = pNom;
    }

    public Joueur getAppartientAJoueur() {
        return appartientAJoueur;
    }

    public void setAppartientAJoueur(Joueur appartientAJoueur) {
        this.appartientAJoueur = appartientAJoueur;
        appartientAJoueur.territoires.add(this);
    }

    public boolean isChateau() {
        return chateau;
    }

    public void setChateau(boolean chateau) {
        this.chateau = chateau;
    }

    public boolean isPort() {
        return port;
    }

    public void setPort(boolean port) {
        this.port = port;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Famille getCapitaleDe() {
        return capitaleDe;
    }

    public void setCapitaleDe(Famille capitaleDe) {
        this.capitaleDe = capitaleDe;
    }


    public void ajouteDesTroupes(int nbTroupes)
    {
        this.nombreDeTroupes=nombreDeTroupes+nbTroupes;
        if(appartientAJoueur!=null) {
            appartientAJoueur.setNbTroupeAPlacer(appartientAJoueur.getNbTroupeAPlacer() - nbTroupes);
        }
    }

    private ArrayList<Territoire> territoiresConnexes = new ArrayList<>();

    public void ajouterTerritoireConnexe(Territoire pTerritoire) {
        if (!this.connexe(pTerritoire)) {
            territoiresConnexes.add(pTerritoire);
        }
        if (!pTerritoire.connexe(this)) {
            pTerritoire.ajouterTerritoireConnexe(this);
        }
    }

    public boolean connexe(Territoire ter)
    {
     return (territoiresConnexes.contains(ter));
    }

}
