package applogic.objects;

import applogic.AppLogicServer;
import common.Famille;
import network.ClientProcessor;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class JoueurServer {


    public Famille getFamille() {
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
        famille.setaUnJoueurAssocie(true);
    }

    private Famille famille;

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    private LocalDateTime creationTime;

    public int getCurrentDeStartResult() {
        return currentDeStartResult;
    }

    public void setCurrentDeStartResult(int currentDeStartResult) {
        this.currentDeStartResult = currentDeStartResult;
    }

    int currentDeStartResult;

    public boolean isHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    private boolean hasPlayed;

    public String getNom() {
        String nomGet="NONAME";
        if (aUnNom()) {
            nomGet=this.nom;
        }
        return nomGet;

    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    private String nom;

    public JoueurServer(){
        this.nom="";
        this.hasPlayed=false;
        this.creationTime =LocalDateTime.now();
        //System.out.println(ts.toString());
    }

     public void setClientProcessor(ClientProcessor clientProcessor) {
        this.clientProcessor = clientProcessor;
    }

    ClientProcessor clientProcessor;

    public boolean aUnNom(){
        if (nom.equals("")) {
            return false;
        }
        else return true;
    }

    public void envoieMessage(String message)
    {
        this.clientProcessor.write(message);
    }








}
