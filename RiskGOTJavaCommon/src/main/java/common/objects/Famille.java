package common.objects;

public class Famille {

    public enum FamilyNames {
    Stark, Baratheon, Lannister, Tyrell, Martell
    }

    private FamilyNames familyName;

    public Famille(FamilyNames pfamName, int pOrdreDeJeu, int pArgentDeDepart, boolean pIsPlayed){
    this.familyName=pfamName;
    this.ordreDeJeu=pOrdreDeJeu;
    this.aUnJoueurAssocie=false;
    this.argentDeDepart = pArgentDeDepart;
    this.isPlayed=pIsPlayed;
    }

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
