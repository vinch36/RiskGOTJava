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

        //North (13)
        territoires.add(new Territoire(Territoire.TerritoireNames.SKAGOS, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_GIFT, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.KARHOLD, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_DREADFORT, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.WINTERFELL, true, false, pFamilles.getFamilleParNom(Famille.FamilyNames.Stark), pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.BEAR_ISLAND, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.WOLFSWOOD, false, true, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.WIDOWS_WATCH, false, true, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.WHITE_HARBOR, false, true, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.BARROWLANDS, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.STONLEY_SHORE, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_NECK, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.CAPE_KRAKEN, false, true, null, pRegions.getRegionByName(Region.RegionNames.THE_NORTH)));

        //RiverLands (5)
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_TWINS, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_RIVERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_TRIDENT, false, true, null, pRegions.getRegionByName(Region.RegionNames.THE_RIVERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.RIVERRUN, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_RIVERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.MARRENHAL, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_RIVERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.STONEY_KEPT, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_RIVERLANDS)));


        //Iron Islands (2)
        territoires.add(new Territoire(Territoire.TerritoireNames.HARLAW, false, true, null, pRegions.getRegionByName(Region.RegionNames.THE_IRON_ISLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.PYKE, true, true, null, pRegions.getRegionByName(Region.RegionNames.THE_IRON_ISLANDS)));

        //Westerlands (5)
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_CRAG, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_WESTERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.GOLDEN_TOOTH, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_WESTERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.CASTERLT_ROCK, true, true, pFamilles.getFamilleParNom(Famille.FamilyNames.Lannister), pRegions.getRegionByName(Region.RegionNames.THE_WESTERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.SILVERKILL, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_WESTERLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.CRAKENHALL, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_WESTERLANDS)));

        //Vale of Arryn (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.MOUNTAINS_OF_THE_MOON, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_VALE_OF_ARRYN)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_FINGERS, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_VALE_OF_ARRYN)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_EYRIE, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_VALE_OF_ARRYN)));
        territoires.add(new Territoire(Territoire.TerritoireNames.GULLTOWN, false, true, null, pRegions.getRegionByName(Region.RegionNames.THE_VALE_OF_ARRYN)));


        //Crownlands (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.CRACKCLAW_POINT, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_CROWNLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.KINGS_LANDING, true, true, null, pRegions.getRegionByName(Region.RegionNames.THE_CROWNLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.DRAGONSTONE, true, true, pFamilles.getFamilleParNom(Famille.FamilyNames.Baratheon), pRegions.getRegionByName(Region.RegionNames.THE_CROWNLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.KINGSWOOD, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_CROWNLANDS)));


        //StormLands (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.STORMS_END, true, true, null, pRegions.getRegionByName(Region.RegionNames.THE_STORMLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.TARTH, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_STORMLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.RAINWOOD, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_STORMLANDS)));
        territoires.add(new Territoire(Territoire.TerritoireNames.DORNISH_MARCHES, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_STORMLANDS)));

        //Reach (7)
        territoires.add(new Territoire(Territoire.TerritoireNames.SEAROAD_MARSHES, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_REACH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.BLACKWATER_RUSH, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_REACH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_MANDER, true, false, null, pRegions.getRegionByName(Region.RegionNames.THE_REACH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.HIGHGARDEN, true, false, pFamilles.getFamilleParNom(Famille.FamilyNames.Tyrell), pRegions.getRegionByName(Region.RegionNames.THE_REACH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.OLDTOWN, true, true, null, pRegions.getRegionByName(Region.RegionNames.THE_REACH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THREE_TOWERS, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_REACH)));
        territoires.add(new Territoire(Territoire.TerritoireNames.THE_ARBOR, false, false, null, pRegions.getRegionByName(Region.RegionNames.THE_REACH)));

        //Dorne (4)
        territoires.add(new Territoire(Territoire.TerritoireNames.RED_MOUNTAINS, false, false, null, pRegions.getRegionByName(Region.RegionNames.DORNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.SANDSTONE, true, false, null, pRegions.getRegionByName(Region.RegionNames.DORNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.GREENBLOD, false, false, null, pRegions.getRegionByName(Region.RegionNames.DORNE)));
        territoires.add(new Territoire(Territoire.TerritoireNames.SUNSPEAR, true, true, pFamilles.getFamilleParNom(Famille.FamilyNames.Martell), pRegions.getRegionByName(Region.RegionNames.DORNE)));

        initConnections();
    }


        private void initConnections()
        {
            //NORTH
            getTerritoireParNom(Territoire.TerritoireNames.SKAGOS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_GIFT));
            getTerritoireParNom(Territoire.TerritoireNames.SKAGOS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KARHOLD));

            getTerritoireParNom(Territoire.TerritoireNames.THE_GIFT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KARHOLD));
            getTerritoireParNom(Territoire.TerritoireNames.THE_GIFT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_DREADFORT));
            getTerritoireParNom(Territoire.TerritoireNames.THE_GIFT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL));

            getTerritoireParNom(Territoire.TerritoireNames.KARHOLD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_DREADFORT));

            getTerritoireParNom(Territoire.TerritoireNames.THE_DREADFORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH));
            getTerritoireParNom(Territoire.TerritoireNames.THE_DREADFORT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL));

            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BARROWLANDS));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR));
            getTerritoireParNom(Territoire.TerritoireNames.WINTERFELL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BEAR_ISLAND));

            getTerritoireParNom(Territoire.TerritoireNames.BEAR_ISLAND).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD));
            getTerritoireParNom(Territoire.TerritoireNames.BEAR_ISLAND).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STONLEY_SHORE));

            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BARROWLANDS));
            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STONLEY_SHORE));
            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CAPE_KRAKEN));
            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLAW));
            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYKE));
            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT));
            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));
            getTerritoireParNom(Territoire.TerritoireNames.WOLFSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN));

            getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR));
            getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GULLTOWN));
            getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE));
            getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING));
            getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STORMS_END));
            getTerritoireParNom(Territoire.TerritoireNames.WIDOWS_WATCH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SUNSPEAR));

            getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_NECK));
            getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BARROWLANDS));
            getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GULLTOWN));
            getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE));
            getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING));
            getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STORMS_END));
            getTerritoireParNom(Territoire.TerritoireNames.WHITE_HARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SUNSPEAR));

            getTerritoireParNom(Territoire.TerritoireNames.BARROWLANDS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STONLEY_SHORE));
            getTerritoireParNom(Territoire.TerritoireNames.BARROWLANDS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_NECK));

            getTerritoireParNom(Territoire.TerritoireNames.THE_NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CAPE_KRAKEN));
            getTerritoireParNom(Territoire.TerritoireNames.THE_NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_TWINS));
            getTerritoireParNom(Territoire.TerritoireNames.THE_NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MOUNTAINS_OF_THE_MOON));
            getTerritoireParNom(Territoire.TerritoireNames.THE_NECK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLAW));

            getTerritoireParNom(Territoire.TerritoireNames.CAPE_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLAW));
            getTerritoireParNom(Territoire.TerritoireNames.CAPE_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYKE));
            getTerritoireParNom(Territoire.TerritoireNames.CAPE_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT));
            getTerritoireParNom(Territoire.TerritoireNames.CAPE_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));
            getTerritoireParNom(Territoire.TerritoireNames.CAPE_KRAKEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN));

            //RiverLands (5)
            getTerritoireParNom(Territoire.TerritoireNames.THE_TWINS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLAW));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TWINS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MOUNTAINS_OF_THE_MOON));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TWINS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT));

            getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MOUNTAINS_OF_THE_MOON));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.RIVERRUN));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MARRENHAL));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HARLAW));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYKE));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));
            getTerritoireParNom(Territoire.TerritoireNames.THE_TRIDENT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN));

            getTerritoireParNom(Territoire.TerritoireNames.RIVERRUN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MARRENHAL));
            getTerritoireParNom(Territoire.TerritoireNames.RIVERRUN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STONEY_KEPT));
            getTerritoireParNom(Territoire.TerritoireNames.RIVERRUN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_CRAG));
            getTerritoireParNom(Territoire.TerritoireNames.RIVERRUN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GOLDEN_TOOTH));
            getTerritoireParNom(Territoire.TerritoireNames.RIVERRUN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYKE));

            getTerritoireParNom(Territoire.TerritoireNames.MARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.MOUNTAINS_OF_THE_MOON));
            getTerritoireParNom(Territoire.TerritoireNames.MARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STONEY_KEPT));
            getTerritoireParNom(Territoire.TerritoireNames.MARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING));
            getTerritoireParNom(Territoire.TerritoireNames.MARRENHAL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH));

            getTerritoireParNom(Territoire.TerritoireNames.STONEY_KEPT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GOLDEN_TOOTH));
            getTerritoireParNom(Territoire.TerritoireNames.STONEY_KEPT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));
            getTerritoireParNom(Territoire.TerritoireNames.STONEY_KEPT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SILVERKILL));
            getTerritoireParNom(Territoire.TerritoireNames.STONEY_KEPT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH));

            //Iron Islands (2)
            getTerritoireParNom(Territoire.TerritoireNames.HARLAW).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.PYKE));
            getTerritoireParNom(Territoire.TerritoireNames.HARLAW).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));
            getTerritoireParNom(Territoire.TerritoireNames.HARLAW).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN));

            getTerritoireParNom(Territoire.TerritoireNames.PYKE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_CRAG));
            getTerritoireParNom(Territoire.TerritoireNames.PYKE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));
            getTerritoireParNom(Territoire.TerritoireNames.PYKE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN));

            //Westerlands (5)
            getTerritoireParNom(Territoire.TerritoireNames.THE_CRAG).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GOLDEN_TOOTH));
            getTerritoireParNom(Territoire.TerritoireNames.THE_CRAG).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));

            getTerritoireParNom(Territoire.TerritoireNames.GOLDEN_TOOTH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK));

            getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SILVERKILL));
            getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CRAKENHALL));
            getTerritoireParNom(Territoire.TerritoireNames.CASTERLT_ROCK).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN));

            getTerritoireParNom(Territoire.TerritoireNames.SILVERKILL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.CRAKENHALL));
            getTerritoireParNom(Territoire.TerritoireNames.SILVERKILL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SEAROAD_MARSHES));
            getTerritoireParNom(Territoire.TerritoireNames.SILVERKILL).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH));

            //Vale of Arryn (4)
            getTerritoireParNom(Territoire.TerritoireNames.MOUNTAINS_OF_THE_MOON).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_FINGERS));
            getTerritoireParNom(Territoire.TerritoireNames.MOUNTAINS_OF_THE_MOON).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_EYRIE));

            getTerritoireParNom(Territoire.TerritoireNames.THE_EYRIE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GULLTOWN));

            getTerritoireParNom(Territoire.TerritoireNames.GULLTOWN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE));
            getTerritoireParNom(Territoire.TerritoireNames.GULLTOWN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING));
            getTerritoireParNom(Territoire.TerritoireNames.GULLTOWN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STORMS_END));
            getTerritoireParNom(Territoire.TerritoireNames.GULLTOWN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SUNSPEAR));

            //Crownlands (4)
            getTerritoireParNom(Territoire.TerritoireNames.CRACKCLAW_POINT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE));
            getTerritoireParNom(Territoire.TerritoireNames.CRACKCLAW_POINT).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING));

            getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.KINGSWOOD));
            getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH));
            getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE));
            getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STORMS_END));
            getTerritoireParNom(Territoire.TerritoireNames.KINGS_LANDING).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SUNSPEAR));

            getTerritoireParNom(Territoire.TerritoireNames.KINGSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE));
            getTerritoireParNom(Territoire.TerritoireNames.KINGSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH));
            getTerritoireParNom(Territoire.TerritoireNames.KINGSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_MANDER));
            getTerritoireParNom(Territoire.TerritoireNames.KINGSWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STORMS_END));

            getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.STORMS_END));
            getTerritoireParNom(Territoire.TerritoireNames.DRAGONSTONE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SUNSPEAR));

            //StormLands (4)
            getTerritoireParNom(Territoire.TerritoireNames.STORMS_END).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.TARTH));
            getTerritoireParNom(Territoire.TerritoireNames.STORMS_END).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.RAINWOOD));
            getTerritoireParNom(Territoire.TerritoireNames.STORMS_END).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DORNISH_MARCHES));
            getTerritoireParNom(Territoire.TerritoireNames.STORMS_END).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_MANDER));
            getTerritoireParNom(Territoire.TerritoireNames.STORMS_END).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SUNSPEAR));

            getTerritoireParNom(Territoire.TerritoireNames.TARTH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.RAINWOOD));

            getTerritoireParNom(Territoire.TerritoireNames.RAINWOOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.DORNISH_MARCHES));

            getTerritoireParNom(Territoire.TerritoireNames.DORNISH_MARCHES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_MANDER));
            getTerritoireParNom(Territoire.TerritoireNames.DORNISH_MARCHES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HIGHGARDEN));
            getTerritoireParNom(Territoire.TerritoireNames.DORNISH_MARCHES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.RED_MOUNTAINS));

            //Reach (7)
            getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SEAROAD_MARSHES));
            getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HIGHGARDEN));
            getTerritoireParNom(Territoire.TerritoireNames.BLACKWATER_RUSH).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_MANDER));

            getTerritoireParNom(Territoire.TerritoireNames.THE_MANDER).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HIGHGARDEN));

            getTerritoireParNom(Territoire.TerritoireNames.SEAROAD_MARSHES).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.HIGHGARDEN));

            getTerritoireParNom(Territoire.TerritoireNames.HIGHGARDEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.RED_MOUNTAINS));
            getTerritoireParNom(Territoire.TerritoireNames.HIGHGARDEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN));
            getTerritoireParNom(Territoire.TerritoireNames.HIGHGARDEN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THREE_TOWERS));

            getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THREE_TOWERS));
            getTerritoireParNom(Territoire.TerritoireNames.OLDTOWN).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THE_ARBOR));

            getTerritoireParNom(Territoire.TerritoireNames.THE_ARBOR).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.THREE_TOWERS));

            getTerritoireParNom(Territoire.TerritoireNames.THREE_TOWERS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.RED_MOUNTAINS));

            //Dorne (4)
            getTerritoireParNom(Territoire.TerritoireNames.RED_MOUNTAINS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SANDSTONE));
            getTerritoireParNom(Territoire.TerritoireNames.RED_MOUNTAINS).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GREENBLOD));

            getTerritoireParNom(Territoire.TerritoireNames.SANDSTONE).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.GREENBLOD));

            getTerritoireParNom(Territoire.TerritoireNames.GREENBLOD).ajouterTerritoireConnexe(getTerritoireParNom(Territoire.TerritoireNames.SUNSPEAR));




















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
}
