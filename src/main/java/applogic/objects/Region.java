package applogic.objects;

public class Region {


    public void set_nom(String _nom) {
        this._nom = _nom;
    }

    public void set_nombreDeTroupes(int _nombreDeTroupes) {
        this._nombreDeTroupes = _nombreDeTroupes;
    }

    private String _nom;

    public String get_nom() {
        return _nom;
    }

    public int get_nombreDeTroupes() {
        return _nombreDeTroupes;
    }

    private int _nombreDeTroupes = 0;


    public Region(String nom)
    {
        this._nom = nom;
    }


}
