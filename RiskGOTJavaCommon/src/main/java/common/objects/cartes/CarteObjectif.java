package common.objects.cartes;

import com.sun.istack.internal.Nullable;
import common.objects.Joueur;

public class CarteObjectif {

    public String getObjectifTxt() {
        return objectifTxt;
    }

    public void setObjectifTxt(String pObjectifTxt) {
        this.objectifTxt = pObjectifTxt;
    }

    public int getNbPointsDeVictoire() {
        return nbPointsDeVictoire;
    }

    public void setNbPointsDeVictoire(int pNbPointsDeVictoire) {
        this.nbPointsDeVictoire = pNbPointsDeVictoire;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int pId) {
        this.id = pId;
    }

    private String objectifTxt;
    private int nbPointsDeVictoire;
    private int id;

    public CarteObjectif(String pObjectifTxt, int pNbPointsDeVictoire, int pId) {
        this.objectifTxt = pObjectifTxt;
        this.nbPointsDeVictoire = pNbPointsDeVictoire;
        this.id = pId;
    }

    public String getIdAsStr(){
        if (this.id<10){
            return "0"+this.id;
        }
        else{
            return String.valueOf(this.id);
        }
    }

    public void setJoueur(@Nullable Joueur joueur) {
        this.joueur = joueur;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    private Joueur joueur;

}
