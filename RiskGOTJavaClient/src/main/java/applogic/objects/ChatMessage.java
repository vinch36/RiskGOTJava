package applogic.objects;
import common.objects.Joueur;

public class ChatMessage {


    public enum ChatMessageType{CHAT,FROM_SERVEUR_DEBUG,TO_SERVEUR_DEBUG,ACTION,INFO, INFO_IMPORTANTE, INFOCHAT,INFOCHAT_IMPORTANTE, ERREUR}

    public ChatMessage(String messageTxt, ChatMessageType messageType, Joueur joueur) {
        this.messageTxt = messageTxt;
        this.messageType = messageType;
        this.joueur = joueur;
    }

    public ChatMessage(String messageTxt, ChatMessageType messageType) {
        this.messageTxt = messageTxt;
        this.messageType = messageType;
    }

    public String getMessageTxt() {
        return messageTxt;
    }

    public void setMessageTxt(String messageTxt) {
        this.messageTxt = messageTxt;
    }

    public ChatMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ChatMessageType messageType) {
        this.messageType = messageType;
    }


    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    private String messageTxt;
    private ChatMessageType messageType;
    private Joueur joueur;

    public String getColor() {
        String color = "#FFFFFF"; //WHITE
        if (joueur != null) {
            if (joueur.getFamille() != null) {
                color = joueur.getFamille().getWebColor();

            }
        }


        return color;


    }

}
