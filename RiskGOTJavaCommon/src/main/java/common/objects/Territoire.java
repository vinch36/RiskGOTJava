package common.objects;

import common.objects.cartes.CarteTerritoire;

import java.util.ArrayList;

public class Territoire {

    public enum TerritoireNames {
        //North (13)
        SKAGOS,DON,KARHOLD,FORT_TERREUR,WINTERFELL,ILE_AUX_OURS,BOIS_AUX_LOUPS,LA_VEUVE,BLANCPORT,TERTRES,ROCHES,NECK,CAP_KRAKEN,
        //RiverLands (5)
        LES_JUMEAUX,TRIDENT,VIVESAIGUES,HARRENHAL,PIERRE_MOUTIER,
        //Iron Islands (2)
        HARLOI,PYK,
        //Westerlands (5)
        FALAISE,LA_DENT_DOR,CASTRAL_ROC,MONTARGENT,CRAKENHALL,
        //Vale of Arryn (4)
        MONTAGNES_DE_LA_LUNE,DOIGTS,LES_EYRIE,GOEVILLE,
        //Crownlands (4)
        PRESQU_ILE_DE_CLAQUEPINCE,PORT_REAL,PEYREDRAGON,BOIS_DU_ROI,
        //StormLands (4)
        ACCALMIE,ILE_DE_TORTH,BOIS_LA_PLUIE,MARCHES_DE_DORNE,
        //Reach (7)
        ROUTE_DE_LOCEAN,NERA,MANDER,HAUTJARDIN,VILLEVIEILLE,TROIS_TOURS,LA_TREILLE,
        //Dorne (4)
        MONTAGNES_ROUGES,LE_GRES,SANG_VERT,LANCEHELION

    }


    private TerritoireNames nom;
    private Joueur appartientAJoueur;
    private boolean chateau;
    private boolean port;
    private Region region;
    private Famille capitaleDe;

    public ArrayList<CarteTerritoire.UniteSpeciale> getUniteSpeciales() {
        return uniteSpeciales;
    }

    private ArrayList<CarteTerritoire.UniteSpeciale> uniteSpeciales;

    public void supprimerLesUniteSpeciales(){
        uniteSpeciales=new ArrayList<>();
        this.chevaliersEngagesDansLaBataille=0;
        this.enginsDeSiegeEngagesDansLaBataille=0;
        this.fortificationsEngagesDansLaBataille=0;
    }

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
        this.visitePourRelieA = false;
        this.uniteSpeciales= new ArrayList<>();
        this.enginsDeSiegeEngagesDansLaBataille = 0;
        this.fortificationsEngagesDansLaBataille= 0;
        this.chevaliersEngagesDansLaBataille =0;

        region.ajouterUnTerritoire(this);
        if (pCapitaleDe != null) capitaleDe.setCapitale(this);
        }

    public void setNom(TerritoireNames pNom) {
        this.nom = pNom;
    }

    public Joueur getAppartientAJoueur() {
        return appartientAJoueur;
    }

    public void setAppartientAJoueur(Joueur pAppartientAJoueur) {
        //Si le territoire a dejà un propriétaire et que ce n'est pas le même.
        if (appartientAJoueur!=null && pAppartientAJoueur!=appartientAJoueur){
            appartientAJoueur.territoires.remove(this);
        }
        pAppartientAJoueur.territoires.add(this);
        this.appartientAJoueur = pAppartientAJoueur;


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


    public void ajouteDesTroupesAPlacer(int nbTroupes)
    {
        this.nombreDeTroupes=nombreDeTroupes+nbTroupes;
        if(appartientAJoueur!=null) {
            appartientAJoueur.setNbTroupeAPlacer(appartientAJoueur.getNbTroupeAPlacer() - nbTroupes);
        }
    }

    public void ajouteDesTroupes(int nbTroupes)
    {
        this.nombreDeTroupes=nombreDeTroupes+nbTroupes;
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

    public boolean isVisitePourRelieA() {
        return visitePourRelieA;
    }

    public void setVisitePourRelieA(boolean visitePourRelieA) {
        this.visitePourRelieA = visitePourRelieA;
    }

    private boolean visitePourRelieA;


    public boolean relieA(Territoire ter)
    {
        this.visitePourRelieA=true;
        if (this==ter){
            return true;
        }
        for (Territoire terVoisin : territoiresConnexes){
            if (terVoisin.getAppartientAJoueur()==ter.getAppartientAJoueur()&&!terVoisin.isVisitePourRelieA()){
                if (terVoisin.relieA(ter)){
                    return true;
                }
            }

        }

        return false;
    }

    public int getArmeeEnReserve(){
        return this.nombreDeTroupes-getArmeeEngagees();
    }


    public int getArmeeEngagees() {
        return armeeEngagees;
    }

    public String getArmeesEngageesStringMsg()
    {
        return armeeEngagees+";"+chevaliersEngagesDansLaBataille+";"+enginsDeSiegeEngagesDansLaBataille+";"+fortificationsEngagesDansLaBataille;
    }

    public void setArmeeEngagees(int armeeEngagees) {
        this.armeeEngagees = armeeEngagees;
    }

    public int getMaxTroupesEngageableEnAttaque(){
        if (this.nombreDeTroupes<4)
        {
            return nombreDeTroupes-1;
        }
        else return 3;
    }

    private int chevaliersEngagesDansLaBataille;

    public void setChevaliersEngagesDansLaBataille(int chevaliersEngagesDansLaBataille) {
        this.chevaliersEngagesDansLaBataille = chevaliersEngagesDansLaBataille;
    }

    public void setEnginsDeSiegeEngagesDansLaBataille(int enginsDeSiegeEngagesDansLaBataille) {
        this.enginsDeSiegeEngagesDansLaBataille = enginsDeSiegeEngagesDansLaBataille;
    }

    public void setFortificationsEngagesDansLaBataille(int fortificationsEngagesDansLaBataille) {
        this.fortificationsEngagesDansLaBataille = fortificationsEngagesDansLaBataille;
    }

    private int enginsDeSiegeEngagesDansLaBataille;
    private int fortificationsEngagesDansLaBataille;


    public int getChevaliersEngagesDansLaBataille() {
        return chevaliersEngagesDansLaBataille;
    }

    public int getEnginsDeSiegeEngagesDansLaBataille() {
        return enginsDeSiegeEngagesDansLaBataille;
    }

    public int getFortificationsEngagesDansLaBataille() {
        return fortificationsEngagesDansLaBataille;
    }







    public int getMaxTroupesEngageableEnDefense(){
        if (this.nombreDeTroupes<3)
            return nombreDeTroupes;
        else return 2;
    }

    private int armeeEngagees;

    public boolean estEntoureDeTerritoiresAmis()
    {
     for (Territoire territoire:territoiresConnexes){
         if (!(territoire.getAppartientAJoueur()==this.getAppartientAJoueur())){
             return false;
         }
     }
     return true;
     //Utile pour savoir si on peut sélectionner ce territoire ou pas ?
    }


    public int getNbEnginDeSiege(){
        int result = 0;
        for (CarteTerritoire.UniteSpeciale uniteSpeciale:this.uniteSpeciales){
            if (uniteSpeciale== CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE) {
                result++;
            }
        }
        return result;
    }

    public int getNbFortification(){
        int result = 0;
        for (CarteTerritoire.UniteSpeciale uniteSpeciale:this.uniteSpeciales){
            if (uniteSpeciale== CarteTerritoire.UniteSpeciale.FORTIFICATION) {
                result++;
            }
        }
        return result;
    }

    public int getNbChevalier(){
        int result = 0;
        for (CarteTerritoire.UniteSpeciale uniteSpeciale:this.uniteSpeciales){
            if (uniteSpeciale== CarteTerritoire.UniteSpeciale.CHEVALIER) {
                result++;
            }
        }
        return result;
    }


}
