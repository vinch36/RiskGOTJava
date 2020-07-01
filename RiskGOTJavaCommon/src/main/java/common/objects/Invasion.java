package common.objects;

import common.objects.cartes.CarteTerritoire;

import java.util.ArrayList;

public class Invasion {

    public void ajouterDesDesHuitFacesAuDefenseur(int nbDeHuit){
        this.nbDeHuitEnBonusAuDefenseur =nbDeHuit+ nbDeHuitEnBonusAuDefenseur;
    }

    public int getNbDeHuitEnBonusAuDefenseur() {
        return nbDeHuitEnBonusAuDefenseur;
    }

    private int nbDeHuitEnBonusAuDefenseur =0;


    public void ajouterDesDesHuitFacesAuAttaquant(int nbDeHuit){
        this.nbDeHuitEnBonusAuAttaquant =nbDeHuit+ nbDeHuitEnBonusAuAttaquant;
    }

    public int getNbDeHuitEnBonusAuAttaquant() {
        return nbDeHuitEnBonusAuAttaquant;
    }

    private int nbDeHuitEnBonusAuAttaquant =0;


    public boolean isAttaquantGagneLesEgalites() {
        return attaquantGagneLesEgalites;
    }

    public void setAttaquantGagneLesEgalites(boolean attaquantGagneLesEgalites) {
        this.attaquantGagneLesEgalites = attaquantGagneLesEgalites;
    }

    private boolean attaquantGagneLesEgalites;

    public boolean isManoeuvrerSansContrainte() {
        return manoeuvrerSansContrainte;
    }

    public void setManoeuvrerSansContrainte(boolean manoeuvrerSansContrainte) {
        this.manoeuvrerSansContrainte = manoeuvrerSansContrainte;
    }

    private boolean manoeuvrerSansContrainte;

    public int getNbBataillesTermineesDansLinvasion() {
        return nbBataillesTermineesDansLinvasion;
    }

    public void setNbBataillesTermineesDansLinvasion(int nbBataillesTermineesDansLinvasion) {
        this.nbBataillesTermineesDansLinvasion = nbBataillesTermineesDansLinvasion;
    }

    private int nbBataillesTermineesDansLinvasion;
    private Territoire territoireSource;

    public Territoire getTerritoireSource() {
        return territoireSource;
    }

    public void setTerritoireSource(Territoire territoireSource) {
        this.territoireSource = territoireSource;
        this.joueurAttaquant = territoireSource.getAppartientAJoueur();
    }

    public Territoire getTerritoireCible() {
        return territoireCible;
    }

    public void setTerritoireCible(Territoire territoireCible) {
        this.territoireCible = territoireCible;
        this.joueurDefenseur = territoireCible.getAppartientAJoueur();
    }

    private Territoire territoireCible;


    public Joueur getJoueurAttaquant() {
        return joueurAttaquant;
    }

    public void setJoueurAttaquant(Joueur joueurAttaquant) {
        this.joueurAttaquant = joueurAttaquant;
    }

    public Joueur getJoueurDefenseur() {
        return joueurDefenseur;
    }

    public void setJoueurDefenseur(Joueur joueurDefenseur) {
        this.joueurDefenseur = joueurDefenseur;
    }

    private Joueur joueurAttaquant;
    private Joueur joueurDefenseur;

    public Invasion()
    {
        this.joueurSourceAValideSesTroupes=false;
        this.joueurCibleAValideSesTroupes=false;
        this.resultatsDesAttaquant=new ArrayList<>();
        this.resultatsDesDefenseur= new ArrayList<>();
        this.joueurSourceALanceLesDes=false;
        this.joueurCibleALanceLesDes=false;
        this.joueurSourceAValideLeResultatDeLaBataille=false;
        this.joueurCibleAValideLeResultatDeLaBataille=false;
        this.nbBataillesTermineesDansLinvasion=0;
        this.nbDeHuitEnBonusAuDefenseur=0;
        this.nbDeHuitEnBonusAuAttaquant=0;
        this.attaquantGagneLesEgalites=false;
        this.manoeuvrerSansContrainte=false;
    }

    public void resetPourProchaineBataille(){
        this.joueurSourceAValideSesTroupes=false;
        this.joueurCibleAValideSesTroupes=false;
        this.resultatsDesAttaquant=new ArrayList<>();
        this.resultatsDesDefenseur= new ArrayList<>();
        this.joueurSourceALanceLesDes=false;
        this.joueurCibleALanceLesDes=false;
        this.joueurSourceAValideLeResultatDeLaBataille=false;
        this.joueurCibleAValideLeResultatDeLaBataille=false;

        this.territoireSource.setArmeeEngagees(0);
        this.territoireSource.setFortificationsEngagesDansLaBataille(0);
        this.territoireSource.setEnginsDeSiegeEngagesDansLaBataille(0);
        this.territoireSource.setChevaliersEngagesDansLaBataille(0);

        this.territoireCible.setArmeeEngagees(0);
        this.territoireCible.setFortificationsEngagesDansLaBataille(0);
        this.territoireCible.setEnginsDeSiegeEngagesDansLaBataille(0);
        this.territoireCible.setChevaliersEngagesDansLaBataille(0);

    }




    public boolean isJoueurSourceAValideSesTroupes() {
        return joueurSourceAValideSesTroupes;
    }

    public void setJoueurSourceAValideSesTroupes(boolean joueurSourceAValideSesTroupes) {
        this.joueurSourceAValideSesTroupes = joueurSourceAValideSesTroupes;
    }

    public boolean isJoueurCibleAValideSesTroupes() {
        return joueurCibleAValideSesTroupes;
    }

    public void setJoueurCibleAValideSesTroupes(boolean joueurCibleAValideSesTroupes) {
        this.joueurCibleAValideSesTroupes = joueurCibleAValideSesTroupes;
    }

    private boolean joueurSourceAValideSesTroupes;
    private boolean joueurCibleAValideSesTroupes;

    public boolean toutLeMondeAValideSesTroupes()
    {
        return (joueurSourceAValideSesTroupes&&joueurCibleAValideSesTroupes);

    }

    private boolean joueurSourceALanceLesDes;

    public boolean isJoueurSourceALanceLesDes() {
        return joueurSourceALanceLesDes;
    }

    public void setJoueurSourceALanceLesDes(boolean joueurSourceALanceLesDes) {
        this.joueurSourceALanceLesDes = joueurSourceALanceLesDes;
    }



    public boolean isJoueurCibleALanceLesDes() {
        return joueurCibleALanceLesDes;
    }

    public void setJoueurCibleALanceLesDes(boolean joueurCibleALanceLesDes) {
        this.joueurCibleALanceLesDes = joueurCibleALanceLesDes;
    }

    private boolean joueurCibleALanceLesDes;



    public boolean resoudreLaBatailleEnCoursApresLeLancerDeDes(){
        if (joueurSourceALanceLesDes&&joueurCibleALanceLesDes){
            nbTroupesPerduesEnAttaque=0;
            nbTroupesPerduesEnDefense=0;
            int maxDeAComparer = Math.min(territoireSource.getArmeeEngagees(), territoireCible.getArmeeEngagees());
            for (int i=0;i<maxDeAComparer;i++){
                if (!attaquantGagneLesEgalites) {
                    if (resultatsDesAttaquant.get(i).getValeurPlusBonus() > resultatsDesDefenseur.get(i).getValeurPlusBonus()) {
                        nbTroupesPerduesEnDefense++;
                    } else {
                        nbTroupesPerduesEnAttaque++;
                    }
                }
                else{
                    if (resultatsDesAttaquant.get(i).getValeurPlusBonus() >= resultatsDesDefenseur.get(i).getValeurPlusBonus()) {
                        nbTroupesPerduesEnDefense++;
                    } else {
                        nbTroupesPerduesEnAttaque++;
                    }
                }
            }
            return true;
        }
        else return false;
    }


    public boolean resoudreLaBatailleEnCoursApresToutesLesValidations() {
        if (joueurSourceAValideLeResultatDeLaBataille && joueurCibleAValideLeResultatDeLaBataille) {
            nbTroupesPerduesEnAttaque = 0;
            nbTroupesPerduesEnDefense = 0;
            int maxDeAComparer = Math.min(territoireSource.getArmeeEngagees(), territoireCible.getArmeeEngagees());
            for (int i = 0; i < maxDeAComparer; i++) {
                if (!attaquantGagneLesEgalites) {
                    if (resultatsDesAttaquant.get(i).getValeurPlusBonus() > resultatsDesDefenseur.get(i).getValeurPlusBonus()) {
                        nbTroupesPerduesEnDefense++;
                        territoireCible.ajouteDesTroupes(-1);
                    } else {
                        nbTroupesPerduesEnAttaque++;
                        territoireSource.ajouteDesTroupes(-1);
                    }
                } else {
                    if (resultatsDesAttaquant.get(i).getValeurPlusBonus() >= resultatsDesDefenseur.get(i).getValeurPlusBonus()) {
                        nbTroupesPerduesEnDefense++;
                        territoireCible.ajouteDesTroupes(-1);
                    } else {
                        nbTroupesPerduesEnAttaque++;
                        territoireSource.ajouteDesTroupes(-1);
                    }
                }
            }
            return true;
        } else return false;
    }

    public int getNbTroupesPerduesEnAttaque() {
        return nbTroupesPerduesEnAttaque;
    }

    public int getNbTroupesPerduesEnDefense() {
        return nbTroupesPerduesEnDefense;
    }

    private int nbTroupesPerduesEnAttaque;
    private int nbTroupesPerduesEnDefense;


    public ArrayList<DeTypeValeur> getResultatsDesAttaquant() {
        return resultatsDesAttaquant;
    }


    public ArrayList<DeTypeValeur> getResultatsDesDefenseur() {
        return resultatsDesDefenseur;
    }

    public void resetResultatDesDefenseur(){
        resultatsDesDefenseur=new ArrayList<>();
    }

    public void resetResultatDesAttaquant(){
        resultatsDesAttaquant=new ArrayList<>();
    }



    private ArrayList<DeTypeValeur> resultatsDesAttaquant;
    private ArrayList<DeTypeValeur> resultatsDesDefenseur;


    public String getResultatDeLaBatailleEnCoursString() {
        String result = "RESULTAT DE BATAILLE: \n";
        if (nbTroupesPerduesEnAttaque == 0) {
            result = result + "Le joueur attaquant " + territoireSource.getAppartientAJoueur().getNomAtFamille() + " ne perd AUCUNE troupe sur cette bataille\n";
        } else {
            result = result + "Le joueur attaquant " + territoireSource.getAppartientAJoueur().getNomAtFamille() + " perd " + nbTroupesPerduesEnAttaque + " troupe(s) sur cette bataille\n";
        }

        if (nbTroupesPerduesEnDefense == 0) {
            result = result+ "Le joueur défenseur " + territoireCible.getAppartientAJoueur().getNomAtFamille() + " ne perd AUCUNE troupe sur cette bataille";
        } else {
            result = result+ "Le joueur défenseur " + territoireCible.getAppartientAJoueur().getNomAtFamille() + " perd " + nbTroupesPerduesEnDefense + " troupe(s) sur cette bataille";
        }
        return result;
    }

    public boolean isJoueurSourceAValideLeResultatDeLaBataille() {
        return joueurSourceAValideLeResultatDeLaBataille;
    }

    public void setJoueurSourceAValideLeResultatDeLaBataille(boolean joueurSourceAValideLeResultatDeLaBataille) {
        this.joueurSourceAValideLeResultatDeLaBataille = joueurSourceAValideLeResultatDeLaBataille;
    }

    public boolean isJoueurCibleAValideLeResultatDeLaBataille() {
        return joueurCibleAValideLeResultatDeLaBataille;
    }

    public void setJoueurCibleAValideLeResultatDeLaBataille(boolean joueurCibleAValideLeResultatDeLaBataille) {
        this.joueurCibleAValideLeResultatDeLaBataille = joueurCibleAValideLeResultatDeLaBataille;
    }

    private boolean joueurSourceAValideLeResultatDeLaBataille;
    private boolean joueurCibleAValideLeResultatDeLaBataille;

    public void victoireAttaquant(){
        this.getTerritoireCible().setArmeeEngagees(0);
        this.getTerritoireSource().setArmeeEngagees(0);
        this.getTerritoireCible().supprimerLesUniteSpeciales(); //Les unités sépciales du défenseur sont détruites

        //Les unitées spéciales de l''attaquant sont déplacée sur le territoire gagné
        for (int i=0;i<this.getTerritoireSource().getChevaliersEngagesDansLaBataille();i++){
            this.getTerritoireCible().getUniteSpeciales().add(CarteTerritoire.UniteSpeciale.CHEVALIER);
            this.getTerritoireSource().getUniteSpeciales().remove((CarteTerritoire.UniteSpeciale.CHEVALIER));
            i++;
        }
        this.getTerritoireSource().setChevaliersEngagesDansLaBataille(0);

        for (int i=0;i<this.getTerritoireSource().getEnginsDeSiegeEngagesDansLaBataille();i++){
            this.getTerritoireCible().getUniteSpeciales().add(CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE);
            this.getTerritoireSource().getUniteSpeciales().remove((CarteTerritoire.UniteSpeciale.ENGIN_DE_SIEGE));
            i++;
        }
        this.getTerritoireSource().setEnginsDeSiegeEngagesDansLaBataille(0);

        this.getTerritoireCible().setEnginsDeSiegeEngagesDansLaBataille(0);
        this.getTerritoireCible().setFortificationsEngagesDansLaBataille(0);
        this.getTerritoireCible().setChevaliersEngagesDansLaBataille(0);

        this.getTerritoireCible().setAppartientAJoueur(this.getTerritoireSource().getAppartientAJoueur());
        this.getTerritoireCible().getAppartientAJoueur().ajouterUnTerritoireGagnePendantLeTour(this.getTerritoireCible());
    }

    public void victoireDefenseur() {
        this.getTerritoireCible().setArmeeEngagees(0);
        this.getTerritoireSource().setArmeeEngagees(0);
        this.getTerritoireSource().setEnginsDeSiegeEngagesDansLaBataille(0);
        this.getTerritoireSource().setFortificationsEngagesDansLaBataille(0);
        this.getTerritoireSource().setChevaliersEngagesDansLaBataille(0);
        this.getTerritoireCible().setEnginsDeSiegeEngagesDansLaBataille(0);
        this.getTerritoireCible().setFortificationsEngagesDansLaBataille(0);
        this.getTerritoireCible().setChevaliersEngagesDansLaBataille(0);
    }
    private int bonusAttaquant = 0;
    public void ajouterUnBonusALattaquant(int i) {
        bonusAttaquant=bonusAttaquant+i;
    }

    public int getBonusAttaquant() {
        return bonusAttaquant;
    }

    public int getBonusDefenseur() {
        return bonusDefenseur;
    }

    private int bonusDefenseur = 0;
    public void ajouterUnBonusAuDefenseur(int i) {
        bonusDefenseur=bonusDefenseur+i;
    }
}
