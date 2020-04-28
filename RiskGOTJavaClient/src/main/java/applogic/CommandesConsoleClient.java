package applogic;



import network.ClientConnexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandesConsoleClient implements Runnable {
    ClientConnexion clientConnexion;
    BufferedReader _in; // pour gestion du flux d'entrée (celui de la console)
    String _strCommande=""; // contiendra la commande tapée
    Thread _t; // contiendra le thread

    //** Constructeur : initialise les variables nécessaires **
    public CommandesConsoleClient(ClientConnexion pClt)
    {
        // le flux d'entrée de la console sera géré plus pratiquement dans un BufferedReader
        clientConnexion=pClt;
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
                    System.out.println("Nombre de joueurs : "+ clientConnexion.getAdversaires().toString());
                    System.out.println("--------");
                }
                else
                {
                    // si la commande n'est ni "total", ni "quit", on informe l'utilisateur et on lui donne une aide
                    System.out.println("Cette commande n'est pas supportee");
                    System.out.println("Quitter : \"quit\"");
                    System.out.println("Nombre de connectes : \"total\"");
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