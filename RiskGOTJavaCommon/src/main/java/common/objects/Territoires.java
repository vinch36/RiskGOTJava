package common.objects;


import java.util.ArrayList;

public class Territoires {

    private ArrayList<Territoire> territoires;


    public ArrayList<Territoire> getTerritoires() {
        return territoires;
    }

    public Territoire getTerritoireParNom(Territoire.TerritoireNames territoireNames) {
        for (Territoire territoire : territoires) {

            if (territoireNames.equals(territoire.getNom())) {
                return territoire;
            }

        }
        return null;
    }

    public Territoire getTerritoireParNomStr(String pNom) {
        for (Territoire territoire : territoires) {

            if (pNom.equals(territoire.getNom().name())) {
                return territoire;
            }

        }
        return null;
    }



    public Territoires(Regions pRegions, Familles pFamilles) {
        this.territoires = new ArrayList<>();
        initTerritoires(pRegions,pFamilles);
        initConnections();
    }


    private void initTerritoires(Regions pRegions, Familles pFamilles)
    {
        //North (13)
        territoires.add(new Territoire(Territoire.TerritoireNames.SKAGOS, false, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.DON, false, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.KARHOLD, false, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.FORT_TERREUR, true, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.WINTERFELL, true, false, pFamilles.getFamilleParNom(Famille.FamilyNames.Stark), pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.ILE_AUX_OURS, false, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.BOIS_AUX_LOUPS, false, true, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.LA_VEUVE, false, true, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.BLANCPORT, false, true, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.TERTRES, true, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.ROCHES, false, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.NECK, false, false, null, pRegions.getRegionByName(Region.RegionNames.NORD)));
        territoires.add(new Territoire(Territoire.TerritoireNames.CAP_KRAKEN, false, true, null, pRegions.getRegionByName(Region.RegionNames.NORD)));

        //RiverLands (5)
        territoires.add(new Territoire(Territoire.TerritoireNames.LES_JUMEAUX, true, false, null, pRegions.getRegionByName(Region.RegionNames.CONFLANS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.TRIDENT, false, true, null, pRegions.getRegionByName(Region.RegionNames.CONFLANS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.VIVESAIGUES, true, false, null, pRegions.getRegionByName(Region.RegionNames.CONFLANS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.HARRENHAL, true, false, null, pRegions.getRegionByName(Region.RegionNames.CONFLANS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.PIERRE_MOUTIER, false, false, null, pRegions.getRegionByName(Region.RegionNames.CONFLANS)));


        //Iron Islands (2)
        territoires.add(new Territoire(Territoire.TerritoireNames.HARLOI, false, true, null, pRegions.getRegionByName(Region.RegionNames.LES_ILES_DE_FER)));
        territoires.add(new Territoire(Territoire.TerritoireNames.PYK, true, true, null, pRegions.getRegionByName(Region.RegionNames.LES_ILES_DE_FER)));

        //Westerlands (5)
        territoires.add(new Territoire(Territoire.TerritoireNames.FALAISE, false, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LOUEST)));
        territoires.add(new Territoire(Territoire.TerritoireNames.LA_DENT_DOR, false, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LOUEST)));
        territoires.add(new Territoire(Territoire.TerritoireNames.CASTRAL_ROC, true, true, pFamilles.getFamilleParNom(Famille.FamilyNames.Lannister), pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LOUEST)));
        territoires.add(new Territoire(Territoire.TerritoireNames.MONTARGENT, false, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LOUEST)));
        territoires.add(new Territoire(Territoire.TerritoireNames.CRAKENHALL, true, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LOUEST)));

        //Vale of Arryn (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE, false, false, null, pRegions.getRegionByName(Region.RegionNames.VALE_DARRYN)));
        territoires.add(new Territoire(Territoire.TerritoireNames.DOIGTS, false, false, null, pRegions.getRegionByName(Region.RegionNames.VALE_DARRYN)));
        territoires.add(new Territoire(Territoire.TerritoireNames.LES_EYRIE, true, false, null, pRegions.getRegionByName(Region.RegionNames.VALE_DARRYN)));
        territoires.add(new Territoire(Territoire.TerritoireNames.GOEVILLE, false, true, null, pRegions.getRegionByName(Region.RegionNames.VALE_DARRYN)));


        //Crownlands (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.PRESQU_ILE_DE_CLAQUEPINCE, false, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LA_COURONNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.PORT_REAL, true, true, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LA_COURONNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.PEYREDRAGON, true, true, pFamilles.getFamilleParNom(Famille.FamilyNames.Baratheon), pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LA_COURONNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.BOIS_DU_ROI, false, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LA_COURONNE)));


        //StormLands (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.ACCALMIE, true, true, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LORAGE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.ILE_DE_TORTH, false, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LORAGE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.BOIS_LA_PLUIE, false, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LORAGE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.MARCHES_DE_DORNE, true, false, null, pRegions.getRegionByName(Region.RegionNames.TERRES_DE_LORAGE)));

        //Reach (7)
        territoires.add(new Territoire(Territoire.TerritoireNames.ROUTE_DE_LOCEAN, false, false, null, pRegions.getRegionByName(Region.RegionNames.BIEF)));
        territoires.add(new Territoire(Territoire.TerritoireNames.NERA, false, false, null, pRegions.getRegionByName(Region.RegionNames.BIEF)));
        territoires.add(new Territoire(Territoire.TerritoireNames.MANDER, true, false, null, pRegions.getRegionByName(Region.RegionNames.BIEF)));
        territoires.add(new Territoire(Territoire.TerritoireNames.HAUTJARDIN, true, false, pFamilles.getFamilleParNom(Famille.FamilyNames.Tyrell), pRegions.getRegionByName(Region.RegionNames.BIEF)));
        territoires.add(new Territoire(Territoire.TerritoireNames.VILLEVIEILLE, true, true, null, pRegions.getRegionByName(Region.RegionNames.BIEF)));
        territoires.add(new Territoire(Territoire.TerritoireNames.TROIS_TOURS, false, false, null, pRegions.getRegionByName(Region.RegionNames.BIEF)));
        territoires.add(new Territoire(Territoire.TerritoireNames.LA_TREILLE, false, false, null, pRegions.getRegionByName(Region.RegionNames.BIEF)));

        //Dorne (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.MONTAGNES_ROUGES, false, false, null, pRegions.getRegionByName(Region.RegionNames.DORNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.LE_GRES, true, false, null, pRegions.getRegionByName(Region.RegionNames.DORNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.SANG_VERT, false, false, null, pRegions.getRegionByName(Region.RegionNames.DORNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.LANCEHELION, true, true, pFamilles.getFamilleParNom(Famille.FamilyNames.Martell), pRegions.getRegionByName(Region.RegionNames.DORNE)));

    }
        private void initConnections()
        {
            //NORTH
            getTerritoireParNom(Territoire.TerritoireNames.SKAGOS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DON));
            getTerritoireParNom(Territoire.TerritoireNames.SKAGOS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KARHOLD));

            getTerritoireParNom(Territoire.TerritoireNames.DON).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KARHOLD));
            getTerritoireParNom(Territoire.TerritoireNames.DON).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.FORT_TERREUR));
            getTerritoireParNom(Territoire.TerritoireNames.DON).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL));

            getTerritoireParNom(Territoire.TerritoireNames.KARHOLD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.FORT_TERREUR));

            getTerritoireParNom(Territoire.TerritoireNames.FORT_TERREUR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE));
            getTerritoireParNom(Territoire.TerritoireNames.FORT_TERREUR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL));

            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TERTRES));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ILE_AUX_OURS));

            getTerritoireParNom(Territoire.TerritoireNames.ILE_AUX_OURS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS));
            getTerritoireParNom(Territoire.TerritoireNames.ILE_AUX_OURS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ROCHES));

            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TERTRES));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ROCHES));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLOI));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYK));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TRIDENT));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_AUX_LOUPS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE));

            getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT));
            getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE));
            getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON));
            getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL));
            getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE));
            getTerritoireParNom(Territoire.TerritoireNames.LA_VEUVE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION));

            getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.NECK));
            getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TERTRES));
            getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE));
            getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON));
            getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL));
            getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE));
            getTerritoireParNom(Territoire.TerritoireNames.BLANCPORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION));

            getTerritoireParNom(Territoire.TerritoireNames.TERTRES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ROCHES));
            getTerritoireParNom(Territoire.TerritoireNames.TERTRES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.NECK));

            getTerritoireParNom(Territoire.TerritoireNames.NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN));
            getTerritoireParNom(Territoire.TerritoireNames.NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LES_JUMEAUX));
            getTerritoireParNom(Territoire.TerritoireNames.NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE));
            getTerritoireParNom(Territoire.TerritoireNames.NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLOI));

            getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLOI));
            getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYK));
            getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TRIDENT));
            getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));
            getTerritoireParNom(Territoire.TerritoireNames.CAP_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE));

            //RiverLands (5)
            getTerritoireParNom(Territoire.TerritoireNames.LES_JUMEAUX).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLOI));
            getTerritoireParNom(Territoire.TerritoireNames.LES_JUMEAUX).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE));
            getTerritoireParNom(Territoire.TerritoireNames.LES_JUMEAUX).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TRIDENT));

            getTerritoireParNom(Territoire.TerritoireNames.TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE));
            getTerritoireParNom(Territoire.TerritoireNames.TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VIVESAIGUES));
            getTerritoireParNom(Territoire.TerritoireNames.TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARRENHAL));
            getTerritoireParNom(Territoire.TerritoireNames.TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLOI));
            getTerritoireParNom(Territoire.TerritoireNames.TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYK));
            getTerritoireParNom(Territoire.TerritoireNames.TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));
            getTerritoireParNom(Territoire.TerritoireNames.TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE));

            getTerritoireParNom(Territoire.TerritoireNames.VIVESAIGUES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARRENHAL));
            getTerritoireParNom(Territoire.TerritoireNames.VIVESAIGUES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PIERRE_MOUTIER));
            getTerritoireParNom(Territoire.TerritoireNames.VIVESAIGUES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.FALAISE));
            getTerritoireParNom(Territoire.TerritoireNames.VIVESAIGUES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LA_DENT_DOR));
            getTerritoireParNom(Territoire.TerritoireNames.VIVESAIGUES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYK));

            getTerritoireParNom(Territoire.TerritoireNames.HARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE));
            getTerritoireParNom(Territoire.TerritoireNames.HARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PIERRE_MOUTIER));
            getTerritoireParNom(Territoire.TerritoireNames.HARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL));
            getTerritoireParNom(Territoire.TerritoireNames.HARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.NERA));

            getTerritoireParNom(Territoire.TerritoireNames.PIERRE_MOUTIER).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LA_DENT_DOR));
            getTerritoireParNom(Territoire.TerritoireNames.PIERRE_MOUTIER).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));
            getTerritoireParNom(Territoire.TerritoireNames.PIERRE_MOUTIER).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTARGENT));
            getTerritoireParNom(Territoire.TerritoireNames.PIERRE_MOUTIER).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.NERA));

            //Iron Islands (2)
            getTerritoireParNom(Territoire.TerritoireNames.HARLOI).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYK));
            getTerritoireParNom(Territoire.TerritoireNames.HARLOI).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));
            getTerritoireParNom(Territoire.TerritoireNames.HARLOI).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE));

            getTerritoireParNom(Territoire.TerritoireNames.PYK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.FALAISE));
            getTerritoireParNom(Territoire.TerritoireNames.PYK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));
            getTerritoireParNom(Territoire.TerritoireNames.PYK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE));

            //Westerlands (5)
            getTerritoireParNom(Territoire.TerritoireNames.FALAISE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LA_DENT_DOR));
            getTerritoireParNom(Territoire.TerritoireNames.FALAISE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));

            getTerritoireParNom(Territoire.TerritoireNames.LA_DENT_DOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC));

            getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTARGENT));
            getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CRAKENHALL));
            getTerritoireParNom(Territoire.TerritoireNames.CASTRAL_ROC).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE));

            getTerritoireParNom(Territoire.TerritoireNames.MONTARGENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CRAKENHALL));
            getTerritoireParNom(Territoire.TerritoireNames.MONTARGENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ROUTE_DE_LOCEAN));
            getTerritoireParNom(Territoire.TerritoireNames.MONTARGENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.NERA));

            getTerritoireParNom(Territoire.TerritoireNames.CRAKENHALL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ROUTE_DE_LOCEAN));


            //Vale of Arryn (4)
            getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DOIGTS));
            getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_DE_LA_LUNE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LES_EYRIE));

            getTerritoireParNom(Territoire.TerritoireNames.LES_EYRIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE));

            getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON));
            getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL));
            getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE));
            getTerritoireParNom(Territoire.TerritoireNames.GOEVILLE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION));

            //Crownlands (4)
            getTerritoireParNom(Territoire.TerritoireNames.PRESQU_ILE_DE_CLAQUEPINCE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON));
            getTerritoireParNom(Territoire.TerritoireNames.PRESQU_ILE_DE_CLAQUEPINCE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL));

            getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BOIS_DU_ROI));
            getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.NERA));
            getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON));
            getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE));
            getTerritoireParNom(Territoire.TerritoireNames.PORT_REAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION));

            getTerritoireParNom(Territoire.TerritoireNames.BOIS_DU_ROI).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_DU_ROI).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.NERA));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_DU_ROI).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MANDER));
            getTerritoireParNom(Territoire.TerritoireNames.BOIS_DU_ROI).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE));

            getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE));
            getTerritoireParNom(Territoire.TerritoireNames.PEYREDRAGON).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION));

            //StormLands (4)
            getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ILE_DE_TORTH));
            getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BOIS_LA_PLUIE));
            getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MARCHES_DE_DORNE));
            getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MANDER));
            getTerritoireParNom(Territoire.TerritoireNames.ACCALMIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION));

            getTerritoireParNom(Territoire.TerritoireNames.ILE_DE_TORTH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BOIS_LA_PLUIE));

            getTerritoireParNom(Territoire.TerritoireNames.BOIS_LA_PLUIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MARCHES_DE_DORNE));

            getTerritoireParNom(Territoire.TerritoireNames.MARCHES_DE_DORNE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MANDER));
            getTerritoireParNom(Territoire.TerritoireNames.MARCHES_DE_DORNE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN));
            getTerritoireParNom(Territoire.TerritoireNames.MARCHES_DE_DORNE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_ROUGES));

            //Reach (7)
            getTerritoireParNom(Territoire.TerritoireNames.NERA).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.ROUTE_DE_LOCEAN));
            getTerritoireParNom(Territoire.TerritoireNames.NERA).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN));
            getTerritoireParNom(Territoire.TerritoireNames.NERA).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MANDER));

            getTerritoireParNom(Territoire.TerritoireNames.MANDER).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN));

            getTerritoireParNom(Territoire.TerritoireNames.ROUTE_DE_LOCEAN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN));

            getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_ROUGES));
            getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE));
            getTerritoireParNom(Territoire.TerritoireNames.HAUTJARDIN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TROIS_TOURS));

            getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TROIS_TOURS));
            getTerritoireParNom(Territoire.TerritoireNames.VILLEVIEILLE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LA_TREILLE));

            getTerritoireParNom(Territoire.TerritoireNames.LA_TREILLE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TROIS_TOURS));

            getTerritoireParNom(Territoire.TerritoireNames.TROIS_TOURS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_ROUGES));

            //Dorne (4)
            getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_ROUGES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LE_GRES));
            getTerritoireParNom(Territoire.TerritoireNames.MONTAGNES_ROUGES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SANG_VERT));

            getTerritoireParNom(Territoire.TerritoireNames.LE_GRES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SANG_VERT));

            getTerritoireParNom(Territoire.TerritoireNames.SANG_VERT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.LANCEHELION));
        }


    public ArrayList<Territoire> getTerritoiresNonAttribues() {
        ArrayList<Territoire> result = new ArrayList<>();
        for (Territoire t : territoires) {
            if (t.getAppartientAJoueur() == null) {
                result.add(t);
            }
        }
        return result;

    }

    public String getTerritoiresNonAttribuesAsString() {
        String result = "";
        for (Territoire t : territoires) {
            if (t.getAppartientAJoueur() == null) {
                result = result + t.getNom().name() + ";";
            }
        }
        return result;
    }

    public boolean isEncoreAuMoinsUnTerritoireLibre() {

        for (Territoire ter : this.getTerritoires()) {
            if (ter.getAppartientAJoueur() == null) {
                return true;
            }
        }
        return false;

    }

    public boolean territoiresSontRelies(Territoire territoireSource, Territoire territoireCible){
        passerTousLesTerritoiresANonVisiteRelie();
        return  (territoireSource.relieA(territoireCible));
    }

    private void passerTousLesTerritoiresANonVisiteRelie() {
        for (Territoire ter : this.getTerritoires()) {
            ter.setVisitePourRelieA(false);

        }
    }
}
