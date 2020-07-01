package common;

public enum ClientCommandes {
    INCONNUE,
    CONNECT,
    WELCOME,
    SEND_NAME,
    CHAT,
    TOUS_CONNECTES,
    LANCE_1DE_START,
    RESULTAT_1_DE,
    INFO,

    FAIRE_CHOIX_FAMILLE,
    JOUEUR_A_FAIT_CHOIX_FAMILLE,
    CHOIX_FAMILLE_TERMINE,

    CHOISIR_LES_CARTES_OBJECTIFS_DEMARRAGE,
    JOUEUR_A_CHOISI_SES_OBJECTIFS_DEMARRAGE,
    CHOIX_CARTES_OBJECTIFS_DEMARRAGE_TERMINE,

    CHOISIR_UN_TERRITOIRE_DEMARRAGE,
    JOUEUR_A_CHOISI_UN_TERRITOIRE_DEMARRAGE,
    CHOIX_TERRITOIRES_DEMARRAGE_TERMINE,

    DEPLOYEZ_UNE_TROUPE,
    JOUEUR_A_RENFORCE_UN_TERRITOIRE,
    PLACEMENT_DEMARRAGE_TERMINE,
    TOUR_1_RENFORCEZ,
    TOUR_1_ACHETEZ_DES_CARTES,
    TOUR_1_ENVAHISSEZ,
    JOUEUR_DEMANDE_A_ACHETER_UN_OBJECTIF,
    CHOISIR_UN_OBJECTIF,
    JOUEUR_A_CHOISI_UN_OBJECTIF,
    JOUEUR_PASSE_ACHAT_DE_CARTES,
    LANCER_INVASION,
    JOUEUR_LANCE_UNE_INVASION,
    JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_ATTAQUE,
    JOUEUR_A_VALIDE_NOMBRE_DE_TROUPES_EN_DEFENSE,
    LANCEZ_VOS_DES_POUR_LA_BATAILLE,
    JOUEUR_A_LANCE_LES_DES_EN_ATTAQUE,
    JOUEUR_A_LANCE_LES_DES_EN_DEFENSE,

    JOUEUR_PEUT_METTRE_UN_DES_DES_A_SA_VALEUR_MAXIMALE,
    JOUEUR_REMPACE_DES_DES_SIX_PAR_DES_DES_HUIT_PENDANT_LINVASION,
    ATTAQUANT_GAGNE_LES_EGALITES_PENDANT_LINVASION,

    LA_BATAILLE_EST_TERMINEE,
    JOUEUR_A_VALIDE_LE_RESULTAT_DE_LA_BATAILLE,
    LA_BATAILLE_EST_TERMINEE_ET_LE_RESULTAT_EST_VALIDE,
    INVASION_TERMINEE_DEFAITE_DEFENSEUR,
    INVASION_TERMINEE_DEFAITE_ATTAQUANT,
    JOUEUR_ATTAQUANT_REALISE_SA_DEFAITE,
    INVASION_PEUT_CONTINUER,
    JOUEUR_CONTINUE_INVASION,
    JOUEUR_ARRETE_UNE_INVASION,
    JOUEUR_EFFECTUE_UNE_MANOEUVRE_EN_FIN_DINVASION,
    JOUEUR_ARRETE_LES_INVASIONS,
    MANOEUVREZ_EN_FIN_DE_TOUR,
    JOUEUR_A_EFFECTUE_UNE_MANOEUVRE,
    JOUEUR_PASSE_LA_MANOEUVRE,
    ATTEIGNEZ_UN_OBJECTIF_EN_FIN_DE_TOUR,
    JOUEUR_A_ATTEINT_UN_OBJECTIF_EN_FIN_DE_TOUR,
    JOUEUR_EST_VICTORIEUX,
    JOUEUR_NATTEINT_PAS_DOBJECTIF,
    JOUEUR_PEUT_PIOCHER_UNE_CARTE_TERRITOIRE,
    JOUEUR_NE_PEUT_PAS_PIOCHER_UNE_CARTE_TERRITOIRE,
    JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE_EN_FIN_DE_TOUR,
    JOUEUR_A_PIOCHE_UNE_CARTE_TERRITOIRE,
    JOUEUR_TERMINE_SON_TOUR,
    CONVERTISSEZ_VOS_CARTES_TERRITOIRES_EN_TROUPES_OU_UNITE_SPECIALES,
    JOUEUR_A_CONVERTI_UNE_CARTE_TERRITOIRE_EN_UNITE_SPECIALE,
    JOUEUR_A_CONVERTI_TROIS_CARTES_TERRITOIRE_EN_TROUPES_SUPPLEMENTAIRES,
    JOUEUR_PASSE_LA_CONVERSION_DE_CARTES_TERRITOIRES,
    JOUEUR_A_DEPLOYE_UNE_UNITE_SPECIALE,
    JOUEUR_VEUT_JOUER_UNE_CARTE_PERSONNAGE,
    JOUEUR_VA_UTILISER_UNE_CARTE_PERSONNAGE,
    CARTE_PERSONNAGE_NON_UTILISABLE,
    JOUEUR_NA_PAS_ASSEZ_DARGENT_POUR_JOUER_SA_CARTE_PERSONNAGE,
    JOUEUR_VOL_DE_LARGENT,
    JOUEUR_ACTIF;
}
