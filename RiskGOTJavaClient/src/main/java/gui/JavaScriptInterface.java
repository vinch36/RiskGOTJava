package gui;


// JavaScriptInterface class : classe publique dont les méthodes sont appelables depuis le code Javsscript.
public class JavaScriptInterface {

    private MainView mainView;


    public JavaScriptInterface(MainView pMainView) {
        this.mainView = pMainView;
    }


    public void callFromJavaScript(String s) {
        System.out.println("COMMANDE RECU DE JAVASCRIPT: " + s);
        if (mainView.isCarteCliquable())
            switch (mainView.getEtatPrincipal()) {
                case CHOISIR_LES_TERRITOIRES_DEMARRAGE:
                    mainView.fromJSAChoisiUnTerritoireDemarrage(s);
                    break;
                case PLACER_LES_TROUPES_DEMARRAGE:
                    mainView.fromJSAAjouteUneTroupe(s);
                    break;
                case TOUR_DE_JEU:
                    switch (mainView.getSousEtat()) {
                        case RENFORCEZ:
                            switch (mainView.getSousEtatRenforcez()){
                                case DEPLOYEZ_DES_TROUPES:
                                    mainView.fromJSAAjouteUneTroupe(s);
                                    break;
                                case DEPLOYEZ_DES_UNITES_SPECIALES:
                                    mainView.fromJSAAjouteUneUniteSpeciale(s);
                                default:
                                    break;
                            }
                            break;
                        case ENVAHISSEZ:
                            switch (mainView.getSousEtatEnvahissez()) {
                                case CHOIX_TERRITOIRE_SOURCE:
                                    mainView.fromJSAChoisiUnTerritoireSourceInvasion(s);
                                    break;
                                case CHOIX_TERRITOIRE_CIBLE:
                                    mainView.fromJSAChoisiUnTerritoireCibleInvasion(s);
                                    break;
                                case CHOIX_TERRITOIRE_CIBLE_MANOEUVRE:
                                    mainView.fromJSAChoisiUnTerritoireCibleManouvrerEnFinDInvasion(s);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case MANOEUVREZ:
                            switch (mainView.getSousEtatManoeuvrez()) {
                                case CHOIX_TERRITOIRE_SOURCE:
                                    mainView.fromJSAChoisiUnTerritoireSourceManoeuvre(s);
                                    break;
                                case CHOIX_TERRITOIRE_CIBLE:
                                    mainView.fromJSAChoisiUnTerritoireCibleManoeuvre(s);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                default:
                    break;

            }
        else {
            System.out.println("Care isNonCliquable !");
        }
    }
}

