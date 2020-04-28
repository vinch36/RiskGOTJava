package common.objects;

import common.util.Utils;

import java.util.ArrayList;

public class Familles {

    public ArrayList<Famille> getFamilles() {
        return familles;
    }


    public ArrayList<Famille> getFamillesActives(){
        ArrayList<Famille> result = new ArrayList<>();
        for (Famille famille : familles) {
            if (famille.isPlayed()){
                result.add(famille);
            }

        }
        return result;
    }
    public Famille getFamilleParNomString(String str) {
        for (Famille famille : familles) {

            if (str.equals(famille.getFamilyName().name())) {
                return famille;
            }

        }
        return null;
    }

    public Famille getFamilleParNom(Famille.FamilyNames pFam) {
        for (Famille famille : familles) {

            if (pFam.equals(famille.getFamilyName())) {
                return famille;
            }
        }
        return null;
    }



    public void setFamilles(ArrayList<Famille> familles) {
        this.familles = familles;
    }

    private ArrayList<Famille> familles;

    public Familles(int pNbJoueur) {

        this.familles=new ArrayList<>();
        this.familles.add(new Famille(Famille.FamilyNames.Stark, 1, 0, true));
        this.familles.add(new Famille(Famille.FamilyNames.Baratheon, 2, 0, true));
        this.familles.add(new Famille(Famille.FamilyNames.Lannister, 3, 300,true));
        this.familles.add(new Famille(Famille.FamilyNames.Tyrell, 4, 500,pNbJoueur>3));
        this.familles.add(new Famille(Famille.FamilyNames.Martell, 5, 500, pNbJoueur>4));
    }



    public int initCapitales(int nbJoueurs)
    {

        //Récuperer le nombre de troupes à placer initialement pour chaque joueur
        int nbTroupesRestantAPlacer = Utils.getNombreDeTroupesAPlacer(nbJoueurs);
        for (Famille f : this.getFamillesActives()){
            f.getJoueur().setNbTroupeAPlacer(nbTroupesRestantAPlacer);
            f.getCapitale().setAppartientAJoueur(f.getJoueur());
            f.getCapitale().ajouteDesTroupes(3);
        }
        return nbTroupesRestantAPlacer-3;
    }

    public Famille getFamilleSuivante(Famille pFam)
    {
        int ordreDeJeu=pFam.getOrdreDeJeu();
        for (Famille famille : this.familles){
            if (famille.isPlayed()){
                if (famille.getOrdreDeJeu()==ordreDeJeu+1){
                    return famille;
                }
            }
        }
        //Si il n'y a pas d'autre famille >, on retourne aux Stark;
        return getFamilleParNom(Famille.FamilyNames.Stark);
    }



}
