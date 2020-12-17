import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestOutdoorParking {
    private Parking myParking;

    @BeforeEach
    void runBefore() {
        myParking = new OutdoorParking("TestOutdoorParking");
    }

    // 46 50
    @Test
    void testGetTotalReservedSpace() throws FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        assertEquals(5, myParking.getTotalReservedSpace());
        Stall stall = new Stall(46);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals(4, myParking.getTotalReservedSpace());
        myParking.removeVehicle(vehicle);
        assertEquals(5, myParking.getTotalReservedSpace());
    }

    // 51 55
    @Test
    void testGetTotalHandicappedSpace() throws FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        assertEquals(5, myParking.getTotalHandicappedSpace());
        Stall stall = new Stall(51);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals(4, myParking.getTotalHandicappedSpace());
        myParking.removeVehicle(vehicle);
        assertEquals(5, myParking.getTotalHandicappedSpace());
    }

    // 56 60
    @Test
    void testGetTotalEVSpace() throws FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        assertEquals(5, myParking.getTotalEVSpace());
        Stall stall = new Stall(56);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals(4, myParking.getTotalEVSpace());
        myParking.removeVehicle(vehicle);
        assertEquals(5, myParking.getTotalEVSpace());
    }


    @Test
    void testGetParkArea() throws FullParkException, ParkedStallException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals("Regular Parking.", myParking.getParkArea(stall));

        stall = new Stall(46);
        vehicle = new Vehicle("test2");
        myParking.addVehicle(stall, vehicle);
        assertEquals("Reserved Parking.", myParking.getParkArea(stall));

        stall = new Stall(51);
        vehicle = new Vehicle("test3");
        myParking.addVehicle(stall, vehicle);
        assertEquals("Handicapped Parking.", myParking.getParkArea(stall));

        stall = new Stall(56);
        vehicle = new Vehicle("test4");
        myParking.addVehicle(stall, vehicle);
        assertEquals("EV Charging Station.", myParking.getParkArea(stall));
    }

    @Test
    void testGetRate() {
        assertEquals(3.0, myParking.getRate());
    }

}
