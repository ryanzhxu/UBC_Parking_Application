package model;

public class Stall {

    private int stallNum;
    private boolean isParked;
    private Vehicle vehicle;

    public Stall(int stallNum) {
        this.stallNum = stallNum;
    }

    public int getStallNum() {
        return stallNum;
    }

    public boolean isParked() {
        return isParked;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    private void setParked() {
        isParked = true;
    }

    // MODIFIES: this
    // EFFECTS: set stall as parked, set vehicle as parked as well using reflexive relationship
    public void setVehicle(Vehicle vehicle) {
        setParked();
        if (this.vehicle != vehicle) {
            this.vehicle = vehicle;
            vehicle.setStall(this);
        }
    }

    // REQUIRES: vehicle has a stall
    // MODIFIES: this
    // EFFECTS: set isParked to false, remove stall from vehicle
    public void removeVehicle() {
        vehicle.removeStall();
        this.vehicle = null;
        isParked = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stall stall = (Stall) o;

        if (stallNum != stall.stallNum) return false;
        if (isParked != stall.isParked) return false;
        return vehicle.equals(stall.vehicle);
    }

    @Override
    public int hashCode() {
        int result = stallNum;
        result = 31 * result + (isParked ? 1 : 0);
        result = 31 * result + vehicle.hashCode();
        return result;
    }
}
