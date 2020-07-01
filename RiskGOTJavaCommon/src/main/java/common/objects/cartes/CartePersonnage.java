package common.objects.cartes;

import common.objects.Famille;
import common.util.SousEtat;

public class CartePersonnage {

    public enum PersonnageNames{CATELYN_STARK, JON_SNOW, NED_STARK, ROBB_STARK, DEVOS_MERVAULT, MELISANDRE, SLADHOR_SAAN, STANIS_BARATHEON, CERSEI_LANNISTER, JAIME_LANNISTER, TYRION_LANNISTER, TYWIN_LANNISTER, BRIENNE, LORAS_TYRELL, MARGAERY_TYRELL, RENLY_BARATHEON, AREO_HOTAH, DORAN_MARTELL, ELLARIA_SAND, OBERYN_MARTELL};

    private PersonnageNames name;
    private Famille famille;
    private  int cout;
    private String capacite;
    private boolean utilisee;

    public String getRaisonPourLaquelLaCarteNePeutPasEtreUtilisee() {
        return raisonPourLaquelLaCarteNePeutPasEtreUtilisee;
    }

    public void setRaisonPourLaquelLaCarteNePeutPasEtreUtilisee(String raisonPourLaquelLaCarteNePeutPasEtreUtilisee) {
        this.raisonPourLaquelLaCarteNePeutPasEtreUtilisee = raisonPourLaquelLaCarteNePeutPasEtreUtilisee;
    }

    private String raisonPourLaquelLaCarteNePeutPasEtreUtilisee;

    public PersonnageNames getName() {
        return name;
    }

    public void setName(PersonnageNames name) {
        this.name = name;
    }

    public Famille getFamille() {
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public String getCapacite() {
        return capacite;
    }

    public void setCapacite(String capacite) {
        this.capacite = capacite;
    }

    public boolean isUtilisee() {
        return utilisee;
    }

    public void setUtilisee(boolean utilisee) {
        this.utilisee = utilisee;
    }



    public CartePersonnage(PersonnageNames pName, Famille pFamille, int pCout,String pCapacite, SousEtat pSousEtatAuquelCartePeutEtreJouee, boolean pPeutEtreJoueePendantSonTour, boolean pPeutEtreJoueePendantLeTourDesAutres)
    {
        this.name=pName;
        this.famille=pFamille;
        this.cout=pCout;
        this.capacite=pCapacite;
        this.utilisee=false;
        this.sousEtatAuquelCartePeutEtreJouee = pSousEtatAuquelCartePeutEtreJouee;
        this.peutEtreJoueePendantSonTour = pPeutEtreJoueePendantSonTour;
        this.peutEtreJoueePendantLeTourDesAutres=pPeutEtreJoueePendantLeTourDesAutres;
        this.raisonPourLaquelLaCarteNePeutPasEtreUtilisee ="";
    }

    SousEtat sousEtatAuquelCartePeutEtreJouee;
    boolean peutEtreJoueePendantSonTour;

    public SousEtat getSousEtatAuquelCartePeutEtreJouee() {
        return sousEtatAuquelCartePeutEtreJouee;
    }

    public void setSousEtatAuquelCartePeutEtreJouee(SousEtat sousEtatAuquelCartePeutEtreJouee) {
        this.sousEtatAuquelCartePeutEtreJouee = sousEtatAuquelCartePeutEtreJouee;
    }

    public boolean isPeutEtreJoueePendantSonTour() {
        return peutEtreJoueePendantSonTour;
    }

    public void setPeutEtreJoueePendantSonTour(boolean peutEtreJoueePendantSonTour) {
        this.peutEtreJoueePendantSonTour = peutEtreJoueePendantSonTour;
    }

    public boolean isPeutEtreJoueePendantLeTourDesAutres() {
        return peutEtreJoueePendantLeTourDesAutres;
    }

    public void setPeutEtreJoueePendantLeTourDesAutres(boolean peutEtreJoueePendantLeTourDesAutres) {
        this.peutEtreJoueePendantLeTourDesAutres = peutEtreJoueePendantLeTourDesAutres;
    }

    boolean peutEtreJoueePendantLeTourDesAutres;






}
