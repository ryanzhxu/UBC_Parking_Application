package model;

import java.util.HashMap;

public class IndoorParking extends Parking {

    private double rate = 4.00;

    public IndoorParking(String name) {
        super(name, 300);
    }

    public double getRate() {
        return rate;
    }

    @Override
    public int getTotalFreeSpace() {
        return 300 - listParking.size();
    }

    @Override
    public int getTotalParkedSpace() {
        return listParking.size();
    }

    // REQUIRES: Parking stalls between number 251 and 270.
    // EFFECTS: return no greater than 20.
    @Override
    public int getTotalReservedSpace() {
        return 20 - getSpaceByInterval(251, 270);
    }

    // REQUIRES: Parking stalls between number 271 and 290.
    // EFFECTS: return no greater than 20.
    @Override
    public int getTotalHandicappedSpace() {
        return 20 - getSpaceByInterval(271, 290);
    }

    // REQUIRES: Parking stalls between number 291 and 300.
    // EFFECTS: return no greater than 10.
    @Override
    public int getTotalEVSpace() {
        return 10 - getSpaceByInterval(291, 300);
    }


    // EFFECT: return type of different parking spots in terms of stall number
    @Override
    public String getParkArea(Stall stall) {
        int stallNum = stall.getStallNum();
        if (stallNum >= 1 && stallNum <= 250) {
            return "Regular Parking.";
        } else if (stallNum >= 251 && stallNum <= 270) {
            return "Reserved Parking.";
        } else if (stallNum >= 271 && stallNum <= 290) {
            return "Handicapped Parking.";
        } else {
            return "EV Charging Station.";
        }
    }



}
