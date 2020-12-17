package model;

import java.util.HashMap;

public class OutdoorParking extends Parking {

    private double rate = 3.00;

    public OutdoorParking(String name) {
        super(name, 60);
    }

    public double getRate() {
        return rate;
    }

    @Override
    public int getTotalFreeSpace() {
        return 60 - listParking.size();
    }

    @Override
    public int getTotalParkedSpace() {
        return listParking.size();
    }


    // REQUIRES: Parking stalls between number 46 and 50.
    // EFFECTS: return no greater than 5.
    @Override
    public int getTotalReservedSpace() {
        return 5 - getSpaceByInterval(46, 50);
    }


    // REQUIRES: Parking stalls between number 51 and 55.
    // EFFECTS: return no greater than 5.
    @Override
    public int getTotalHandicappedSpace() {
        return 5 - getSpaceByInterval(51, 55);
    }

    // REQUIRES: Parking stalls between number 56 and 60.
    // EFFECTS: return no greater than 5.
    @Override
    public int getTotalEVSpace() {
        return 5 - getSpaceByInterval(56, 60);
    }

    @Override
    public String getParkArea(Stall stall) {
        int stallNum = stall.getStallNum();
        if (stallNum >= 1 && stallNum <= 45) {
            return "Regular Parking.";
        } else if (stallNum >= 46 && stallNum <= 50) {
            return "Reserved Parking.";
        } else if (stallNum >= 51 && stallNum <= 55) {
            return "Handicapped Parking.";
        } else {
            return "EV Charging Station.";
        }
    }

}
