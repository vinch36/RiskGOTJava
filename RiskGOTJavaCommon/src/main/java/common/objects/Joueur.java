package common.objects;

import common.objects.cartes.CarteObjectif;
import common.objects.cartes.CarteTerritoire;

import java.util.ArrayList;

public class Joueur {


    public Famille getFamille() {
        return famille;
    }

    public int getNombreDeManoeuvreEnFinDeTour() {
        return nombreDeManoeuvreEnFinDeTour;
    }

    public void setNombreDeManoeuvreEnFinDeTour(int nombreDeManoeuvreEnFinDeTour) {
        this.nombreDeManoeuvreEnFinDeTour = nombreDeManoeuvreEnFinDeTour;
    }

    private int nombreDeManoeuvreEnFinDeTour = 1;

    public ArrayList<Territoire> territoires = new ArrayList<>();

    public int getNbTroupes() {

        int nbTroupes = 0;
        for (Territoire ter : territoires) {
            nbTroupes = nbTroupes + ter.getNombreDeTroupes();
        }
        return nbTroupes;

    }

    public int getPointsDeVictoire() {
        return pointsDeVictoire;
    }

    public void setPointsDeVictoire(int pointsDeVictoire) {
        this.pointsDeVictoire = pointsDeVictoire;
    }

    private int pointsDeVictoire = 0;

    public int getNbTroupeAPlacer() {
        return nbTroupeAPlacer;
    }

    public void setNbTroupeAPlacer(int nbTroupeAPlacer) {
        this.nbTroupeAPlacer = nbTroupeAPlacer;
    }

    int nbTroupeAPlacer = 0;

    public int getArgent() {
        return argent;
    }

    public void setArgent(int argent) {
        this.argent = argent;
    }

    private int argent = 0;

    public void setFamille(Famille famille) {
        this.famille = famille;
        famille.setaUnJoueurAssocie(true);
        famille.setJoueur(this);
        argent = famille.getArgentDeDepart();
    }

    protected Famille famille;

    public boolean controleSaCapitale() {
        return (famille.getCapitale().getAppartientAJoueur() == this);
    }


    public boolean aUnNom() {
        if (nom.equals("")) {
            return false;
        } else return true;
    }

    public String getNom() {
        String nomGet = "NONAME";
        if (aUnNom()) {
            nomGet = this.nom;
        }
        return nomGet;

    }

    public String getNomAtFamille(){
        if (this.famille!=null){
            return this.nom+"@"+this.famille.getFamilyName().name();
        }
        else return getNom();
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    protected String nom = "";

    public int getNombreDeTerritoires() {
        return territoires.size();
    }

    public int getNombreDeChateaux() {
        int counter = 0;
        for (Territoire ter : territoires) {
            if (ter.isChateau()) {
                counter++;
            }
        }
        return counter;
    }

    public int getNombreDePorts() {
        int counter = 0;
        for (Territoire ter : territoires) {
            if (ter.isPort()) {
                counter++;
            }
        }
        return counter;
    }

    public int calculerNombreDeRenfortsDeBase() {
        int result = 0;
        result = (getNombreDeTerritoires() + getNombreDeChateaux()) / 3;
        return result;
    }


    public int calculerBonusRegion()
    {
        int result = 0;
        ArrayList<Region> regionsDuJoueur = new ArrayList<>();
        for (Territoire ter : territoires) {
            if (!regionsDuJoueur.contains(ter.getRegion())){
                regionsDuJoueur.add(ter.getRegion());
            }
        }
        for (Region reg : regionsDuJoueur)
        {
            if (reg.appartientComplementAUnJoueur(this)){
                result =result+reg.getBonusTroupes();
            }

        }
        return result;

    }

    public String calculerBonusRegionAsString()
    {
        String result = "";
        ArrayList<Region> regionsDuJoueur = new ArrayList<>();
        for (Territoire ter : territoires) {
            if (!regionsDuJoueur.contains(ter.getRegion())){
                regionsDuJoueur.add(ter.getRegion());
            }
        }
        for (Region reg : regionsDuJoueur)
        {
            if (reg.appartientComplementAUnJoueur(this)){
                result = result + reg.getBonusTroupes()  + "(" + reg.getNom().name()+ ")+";
            }

        }
        if (!result.equals("")){
            result = result.substring(0,result.length()-1);
        }
        return result;

    }

    private ArrayList<Territoire> territoiresGagnesPendantLeTour = new ArrayList<>();


    public void ajouterUnTerritoireGagnePendantLeTour(Territoire pTerritoire){
        this.territoiresGagnesPendantLeTour.add(pTerritoire);
    }

    public void demarreSonTour(){
        this.territoiresGagnesPendantLeTour=new ArrayList<>();
        this.nombreDeManoeuvreEnFinDeTour=1;
    }

    public int nbTerritoiresGagnesPendantLeTour(){
        return this.territoiresGagnesPendantLeTour.size();
    }


    public ArrayList<CarteTerritoire> getCartesTerritoires() {
        return cartesTerritoires;
    }

    private ArrayList<CarteTerritoire> cartesTerritoires = new ArrayList<>();

    public void aPiocheUneCarteTerritoire(CarteTerritoire pCarteTerritoire){
        cartesTerritoires.add(pCarteTerritoire);
        pCarteTerritoire.setJoueur(this);
    }


    public void utiliseUneCarteTerritoire(CarteTerritoire pCarteTerritoire){
        cartesTerritoires.remove(pCarteTerritoire);
        //System.out.println("Joueur Utilise ine carte territoire :" + pCarteTerritoire.getTerritoire().getNom().name());
        pCarteTerritoire.setJoueur(null);
    }


    public ArrayList<CarteObjectif> getCartesObjectif() {
        return cartesObjectif;
    }

    private ArrayList<CarteObjectif> cartesObjectif = new ArrayList<>();

    public void aPiocheUneCarteObjectif(CarteObjectif pCarteObjectif){
        cartesObjectif.add(pCarteObjectif);
        pCarteObjectif.setJoueur(this);

    }

    public void jetteUneCarteObjectif(CarteObjectif pCarteObjectif){
        cartesObjectif.remove(pCarteObjectif);
        pCarteObjectif.setJoueur(null);
    }

    public void atteintUnObjectif(CarteObjectif pCarteObjectif){
        this.pointsDeVictoire=this.pointsDeVictoire+pCarteObjectif.getNbPointsDeVictoire();
        jetteUneCarteObjectif(pCarteObjectif);

    }

    public boolean estVictorieux(){
        return (this.pointsDeVictoire>9&&this.controleSaCapitale());
    }








}
