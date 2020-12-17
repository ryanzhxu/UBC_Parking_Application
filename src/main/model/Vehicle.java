package model;

public class Vehicle {

    private String licensePlate;
    private Stall stall;
    private Parking parking;

    public Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Stall getStall() {
        return stall;
    }

    // EFFECTS: overwritten equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vehicle vehicle = (Vehicle) o;

        return licensePlate.equals(vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return licensePlate.hashCode();
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    // MODIFIES: this
    // EFFECTS: set parking to null
    public void removeParking() {
        this.parking = null;
    }


    // MODIFIES: this
    // EFFECTS: set stall to this stall, set this to specific stall
    public void setStall(Stall stall) {
        if (this.stall != stall) {
            this.stall = stall;
            stall.setVehicle(this);
        }
    }

    // MODIFIES: this
    // EFFECTS: set stall and parking to null
    public void removeStall() {
        this.stall = null;
        this.parking = null;
    }

    // EFFECTS: return string to observer's update method
    @Override
    public String toString() {
        return "Vehicle {"
                + "licensePlate = '" + licensePlate + '\''
                + ", stall = " + stall.getStallNum() + '\''
                + ", parkade = " + parking.getParkingName()
                + '}';
    }
}
