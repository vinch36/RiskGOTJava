package common.objects;

import java.util.ArrayList;

public class Regions {
    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    private ArrayList<Region> regions;

    public Regions() {
        this.regions=new ArrayList<>();
        regions.add(new Region(Region.RegionNames.NORD,5));
        regions.add(new Region(Region.RegionNames.CONFLANS,2));
        regions.add(new Region(Region.RegionNames.LES_ILES_DE_FER,1));
        regions.add(new Region(Region.RegionNames.VALE_DARRYN,1));
        regions.add(new Region(Region.RegionNames.TERRES_DE_LA_COURONNE,2));
        regions.add(new Region(Region.RegionNames.TERRES_DE_LOUEST,2));
        regions.add(new Region(Region.RegionNames.TERRES_DE_LORAGE,1));
        regions.add(new Region(Region.RegionNames.BIEF,4));
        regions.add(new Region(Region.RegionNames.DORNE,1));
    }

    public Region getRegionByName(Region.RegionNames pNom)
    {
        for (Region r : regions)
        {
            if (r.getNom().equals(pNom)) return r;
        }
        return null;
    }
}
