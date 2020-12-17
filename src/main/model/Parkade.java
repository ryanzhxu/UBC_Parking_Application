package model;

import java.io.IOException;
import java.util.Map;

public interface Parkade {

    // EFFECTS: return name
    String getParkingName();

    // EFFECTS: return total space available in the parking
    int getTotalFreeSpace();

    int getTotalParkedSpace();

    int getTotalReservedSpace();

    int getTotalEVSpace();

    int getTotalHandicappedSpace();

    String getParkArea(Stall stall);

    boolean vehicleExist(Vehicle vehicle);

    void removeVehicle(Vehicle vehicle) throws VehicleNotFoundException, EmptyParkException;

    boolean isFull();

    boolean isEmpty();

    void addVehicle(Stall stall, Vehicle vehicle) throws ParkedStallException, FullParkException;

    Stall getStallByVehicle(Vehicle vehicle) throws VehicleNotFoundException;

    int getSpaceByInterval(int a, int b);
}
