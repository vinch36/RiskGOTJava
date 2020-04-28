package common.objects;

import java.util.ArrayList;

public class Joueur {


    public Famille getFamille() {
        return famille;
    }

    public ArrayList<Territoire> territoires = new ArrayList<>();

    public int getNbTroupes()
    {

      int nbTroupes = 0;
      for (Territoire ter :territoires)
      {
          nbTroupes=nbTroupes+ter.getNombreDeTroupes();
      }
      return nbTroupes;

    }
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

    private int argent=0;

    public void setFamille(Famille famille) {
        this.famille = famille;
        famille.setaUnJoueurAssocie(true);
        famille.setJoueur(this);
        argent=famille.getArgentDeDepart();
    }

    protected Famille famille;

    public boolean controleSaCapitale() {
        return (famille.getCapitale().getAppartientAJoueur()==this);
    }



    public boolean aUnNom(){
        if (nom.equals("")) {
            return false;
        }
        else return true;
    }

    public String getNom() {
        String nomGet="NONAME";
        if (aUnNom()) {
            nomGet=this.nom;
        }
        return nomGet;

    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    protected String nom="";

    public int getNombreDeTerritoires(){
        return territoires.size();
    }
    public int getNombreDeChateaux() {
    int counter=0;
    for (Territoire ter : territoires){
        if (ter.isChateau()){
            counter++;
        }
    }
    return counter;
    }
    public int getNombreDePorts(){
        int counter=0;
        for (Territoire ter : territoires){
            if (ter.isPort()){
                counter++;
            }
        }
        return counter;    }


}
