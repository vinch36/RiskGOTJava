package server;

import applogic.AppLogicServer;
import applogic.objects.Etat;

public class Server {




    public static void main( String[] args ) {

        AppLogicServer app = new AppLogicServer();
        app.setEtatprincipal(Etat.SAISIE_NB_JOUEURS);
        app.saisieNbJoueurs();
        new CommandesConsoleServer(app); // lance le thread de gestion des commandes
        app.setEtatprincipal(Etat.ATTENTE_CONNECTION_JOUEURS);
        app.attenteConnectionDesJoueurs();
        app.setEtatprincipal(Etat.ATTENTE_NOM_JOUEURS);



    }
}
