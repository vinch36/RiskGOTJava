package gui;


// JavaScriptInterface class : classe publique dont les méthodes sont appelables depuis le code Javsscript.
public class JavaScriptInterface {

    private MainView mainView;


    public JavaScriptInterface(MainView pMainView){
        this.mainView=pMainView;
    }


    public void callFromJavaScript(String s) {
        System.out.println("COMMANDE RECU DE JAVASCRIPT: " + s);
        if (mainView.isCarteCliquable())
        switch (mainView.getEtatPrincipal()){
            case CHOISIR_LES_TERRITOIRES_DEMARRAGE:
                System.out.println("J'appelle From JSAChoisiUnTerritoireDemarrage");
                mainView.fromJSAChoisiUnTerritoireDemarrage(s);
                break;
            case PLACER_LES_TROUPES_DEMARRAGE:
                System.out.println("J'appelle From JSAChoisiUnTerritoireDemarrage");
                mainView.fromJSAAjouteUneTroupeDemarrage(s);
                break;
            default:
                break;
        }
        else
            {
                System.out.println("Care isNonCliquable !");
            }
    }
}

