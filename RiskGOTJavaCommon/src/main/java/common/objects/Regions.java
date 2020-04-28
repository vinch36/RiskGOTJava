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
        regions.add(new Region(Region.RegionNames.THE_NORTH,5));
        regions.add(new Region(Region.RegionNames.THE_RIVERLANDS,2));
        regions.add(new Region(Region.RegionNames.THE_IRON_ISLANDS,1));
        regions.add(new Region(Region.RegionNames.THE_VALE_OF_ARRYN,1));
        regions.add(new Region(Region.RegionNames.THE_CROWNLANDS,2));
        regions.add(new Region(Region.RegionNames.THE_WESTERLANDS,2));
        regions.add(new Region(Region.RegionNames.THE_STORMLANDS,1));
        regions.add(new Region(Region.RegionNames.THE_REACH,4));
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
