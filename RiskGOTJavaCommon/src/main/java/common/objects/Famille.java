package common.objects;


import common.objects.cartes.CartePersonnage;
import common.util.SousEtat;
import common.util.SousEtatEnvahissez;
import common.objects.cartes.CartePersonnage.PersonnageNames;

import java.util.ArrayList;

public class Famille {

    public enum FamilyNames {
    Stark, Baratheon, Lannister, Tyrell, Martell
    }

    public ArrayList<CartePersonnage> getCartesPersonnages() {
        return cartesPersonnages;
    }

    private ArrayList<CartePersonnage> cartesPersonnages;

    public CartePersonnage getCartePersonnageParNom(PersonnageNames personnageNames)
    {
     for (CartePersonnage carte : this.cartesPersonnages){
         if (personnageNames==carte.getName())
         {
             return  carte;
         }
     }
     return null;
    }

    private FamilyNames familyName;

    public Famille(FamilyNames pfamName, int pOrdreDeJeu, int pArgentDeDepart, boolean pIsPlayed, String pWebColor){
    this.familyName=pfamName;
    this.ordreDeJeu=pOrdreDeJeu;
    this.aUnJoueurAssocie=false;
    this.argentDeDepart = pArgentDeDepart;
    this.isPlayed=pIsPlayed;
    this.webColor = pWebColor;
    this.creerLesCartesPersonnage();
    }

    private void creerLesCartesPersonnage()
    {
        this.cartesPersonnages=new ArrayList<>();
        switch (this.familyName){
            case Stark:
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.CATELYN_STARK,this,200,"capa", SousEtat.ENVAHISSEZ, false, true));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.JON_SNOW,this,100,"capa", SousEtat.MANOEUVREZ, true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.NED_STARK,this,200,"capa", SousEtat.ENVAHISSEZ, false, true ));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.ROBB_STARK,this,300,"capa", SousEtat.ENVAHISSEZ ,true, false));
                break;
            case Baratheon:
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.DEVOS_MERVAULT,this,200,"capa", SousEtat.ENVAHISSEZ,true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.MELISANDRE,this,200,"capa", SousEtat.ENVAHISSEZ,true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.SLADHOR_SAAN,this,100,"capa", SousEtat.ENVAHISSEZ,true, false ));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.STANIS_BARATHEON,this,300,"capa", SousEtat.ENVAHISSEZ,false, true));
                break;
            case Lannister:
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.CERSEI_LANNISTER,this,300,"capa", SousEtat.TIREZ_UNE_CARTE_TERRITOIRE, true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.JAIME_LANNISTER,this,200,"capa", SousEtat.ENVAHISSEZ, true,false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.TYRION_LANNISTER,this,100,"capa", SousEtat.ACHETEZ_DES_CARTES, true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.TYWIN_LANNISTER,this,200,"capa", SousEtat.ENVAHISSEZ, false, true));
                break;
            case Tyrell:
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.BRIENNE,this,300,"capa", SousEtat.ENVAHISSEZ, false, true));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.LORAS_TYRELL,this,200,"capa", SousEtat.ENVAHISSEZ, true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.MARGAERY_TYRELL,this,100,"capa",SousEtat.ENVAHISSEZ, true, true));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.RENLY_BARATHEON,this,200,"capa", SousEtat.RENFORCEZ, true, false));
                break;
            case Martell:
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.AREO_HOTAH,this,200,"capa", SousEtat.ENVAHISSEZ,false, true));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.DORAN_MARTELL,this,200,"capa", SousEtat.TIREZ_UNE_CARTE_TERRITOIRE, true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.ELLARIA_SAND,this,100,"capa", SousEtat.ENVAHISSEZ,true, false));
                this.cartesPersonnages.add(new CartePersonnage(PersonnageNames.OBERYN_MARTELL,this,200,"capa", SousEtat.ENVAHISSEZ,true, false ));
                break;
            default:
                System.err.println("Euh, c'est quoi cette famille inconnue ?! --> " + familyName.name());
                break;
        }

    }

    public String getWebColor() {
        return webColor;
    }

    public void setWebColor(String webColor) {
        this.webColor = webColor;
    }

    private String webColor;

    public int getArgentDeDepart() {
        return argentDeDepart;
    }

    private int argentDeDepart;

    public Territoire getCapitale() {
        return capitale;
    }

    public void setCapitale(Territoire capitale) {
        this.capitale = capitale;
    }

    Territoire capitale;

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    boolean isPlayed;

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    Joueur joueur;

    public FamilyNames getFamilyName() {
        return familyName;
    }

    public void setFamilyName(FamilyNames familyName) {
        this.familyName = familyName;
    }

    public int getOrdreDeJeu() {
        return ordreDeJeu;
    }

    public void setOrdreDeJeu(int ordreDeJeu) {
        this.ordreDeJeu = ordreDeJeu;
    }

    public boolean isaUnJoueurAssocie() {
        return aUnJoueurAssocie;
    }

    public void setaUnJoueurAssocie(boolean aUnJoueurAssocie) {
        this.aUnJoueurAssocie = aUnJoueurAssocie;
    }

    private int ordreDeJeu;
    private boolean aUnJoueurAssocie;


}
