package common;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Familles {

    public ArrayList<Famille> getFamilles() {
        return familles;
    }

    public Famille getFamilleParNom(String str) {
        for (Famille famille : familles) {

            if (str.equals(famille.getFamilyName().name())) {
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
        this.familles.add(new Famille(Famille.FamilyNames.Stark, 1));
        this.familles.add(new Famille(Famille.FamilyNames.Baratheon, 2));
        if (pNbJoueur > 2) {
            this.familles.add(new Famille(Famille.FamilyNames.Lannister, 3));
        }
        if (pNbJoueur > 3) {
            this.familles.add(new Famille(Famille.FamilyNames.Tyrell, 4));
        }
    }


}
