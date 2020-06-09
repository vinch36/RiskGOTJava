package common.objects;

public class Manoeuvre {

    public Territoire getTerritoireSource() {
        return territoireSource;
    }

    public void setTerritoireSource(Territoire territoireSource) {
        this.territoireSource = territoireSource;
    }

    Territoire territoireSource;

    public Territoire getTerritoireCible() {
        return territoireCible;
    }

    public void setTerritoireCible(Territoire territoireCible) {
        this.territoireCible = territoireCible;
    }

    Territoire territoireCible;


    public Manoeuvre(){
        this.nbTroupes=0;
    }


    public int getNbTroupes() {
        return nbTroupes;
    }

    public void setNbTroupes(int nbTroupes) {
        this.nbTroupes = nbTroupes;
    }

    int nbTroupes;



}
