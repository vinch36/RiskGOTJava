package server;

import applogic.AppLogicServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandesConsoleServer implements Runnable {
    AppLogicServer app;
    BufferedReader _in; // pour gestion du flux d'entrée (celui de la console)
    String _strCommande=""; // contiendra la commande tapée
    Thread _t; // contiendra le thread

    //** Constructeur : initialise les variables nécessaires **
    CommandesConsoleServer(AppLogicServer pApp)
    {
        // le flux d'entrée de la console sera géré plus pratiquement dans un BufferedReader
        app=pApp;
        _in = new BufferedReader(new InputStreamReader(System.in));
        _t = new Thread(this); // instanciation du thread
        _t.start(); // démarrage du thread, la fonction run() est ici lancée
    }

    //** Methode : attend les commandes dans la console et exécute l'action demandée **
    public void run() // cette méthode doit obligatoirement être implémentée à cause de l'interface Runnable
    {
        try
        {
            // si aucune commande n'est tapée, on ne fait rien (bloquant sur _in.readLine())
            while ((_strCommande=_in.readLine())!=null)
            {
                if (_strCommande.equalsIgnoreCase("quit")) // commande "quit" detectée ...
                    System.exit(0); // ... on ferme alors le serveur
                else if(_strCommande.equalsIgnoreCase("total")) // commande "total" detectée ...
                {
                    // ... on affiche le nombre de joueurs  connectés
                    System.out.println("Nombre de joueurs : "+ app.getNbJoueurs());
                    System.out.println("--------");
                }

                else if(_strCommande.equalsIgnoreCase("init-ter")) // commande "init3" detectée ...
                {
                    //On initialise une partie en mode rapide (pour debug only)
                    System.out.println("Finalisation du choix des territoires en aléatoire");
                    app.initChoixTerritoire4Debug();
                    System.out.println("--------");
                }
                else if(_strCommande.equalsIgnoreCase("init-troupes")) // commande "init3" detectée ...
                {
                    //On initialise une partie en mode rapide (pour debug only)
                    System.out.println("Finalisation de l'affectation des troupes sur les territoires");
                    app.initPlacementDesTroupes4Debug();
                    System.out.println("--------");
                }

                else if(_strCommande.equalsIgnoreCase("init")) // commande "init" detectée ...
                {
                    //On initialise une partie en mode rapide (pour debug only)
                    System.out.println("Finalisation de l'affectation des troupes sur les territoires");
                    app.init4Debug();
                    System.out.println("--------");
                }


                else
                {
                    // si la commande n'est ni "total", ni "quit", on informe l'utilisateur et on lui donne une aide
                    System.out.println("Cette commande n'est pas supportee");
                    System.out.println("Quitter : \"quit\"");
                    System.out.println("Nombre de connectes : \"total\"");
                    System.out.println("Finalisation du choix des territoires en aléatoire: \"init-ter\" - prérequis: joueurs connectés et statut = Choix Famille fait");
                    System.out.println("Finalisation de l'affectation des troupes sur les territoires en alétoire: \"init-troupes\" - prérequis: joueurs connectés et statut = PLACER_TROUPES");
                    System.out.println("Tout initaliser en aléatoire automatiquement: \"init\" - prérequis:etat = en attente des connections");
                    System.out.println("--------");
                }
                System.out.flush(); // on affiche tout ce qui est en attente dans le flux
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}