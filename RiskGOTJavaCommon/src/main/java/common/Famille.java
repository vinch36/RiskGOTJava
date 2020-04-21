package common;

public class Famille {

    public enum FamilyNames {
    Stark, Baratheon, Lannister, Tyrell
    }

    private FamilyNames familyName;

    public Famille(FamilyNames pfamName, int pOrdreDeJeu){
    this.familyName=pfamName;
    this.ordreDeJeu=pOrdreDeJeu;
    this.aUnJoueurAssocie=false;
    }

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
