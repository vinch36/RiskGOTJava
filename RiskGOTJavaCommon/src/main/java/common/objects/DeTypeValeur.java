package common.objects;

public class DeTypeValeur {
    public enum TypeDe{SIX,HUIT}

    public TypeDe getTypeDe() {
        return typeDe;
    }

    public void setTypeDe(TypeDe typeDe) {
        this.typeDe = typeDe;
    }

    private TypeDe typeDe;

    public int getValeur() {
        return valeur;
    }

    public int getValeurPlusBonus() {
        return valeur+bonus;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    private int valeur;

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    private int bonus;


    public DeTypeValeur(TypeDe pTypeDe, int pValeur, int pBonus){
        this.typeDe=pTypeDe;
        this.valeur=pValeur;
        this.bonus=pBonus;

    }
}
