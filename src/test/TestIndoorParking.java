import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestIndoorParking {

    private Parking myParking;

    @BeforeEach
    void runBefore() {
        myParking = new IndoorParking("TestIndoorParking");
    }

    @Test
    void testGetTotalReservedSpace() throws FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        assertEquals(20, myParking.getTotalReservedSpace());
        Stall stall = new Stall(256);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals(19, myParking.getTotalReservedSpace());
        myParking.removeVehicle(vehicle);
        assertEquals(20, myParking.getTotalReservedSpace());
    }

    @Test
    void testGetTotalHandicappedSpace() throws FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        assertEquals(20, myParking.getTotalHandicappedSpace());
        Stall stall = new Stall(271);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals(19, myParking.getTotalHandicappedSpace());
        myParking.removeVehicle(vehicle);
        assertEquals(20, myParking.getTotalHandicappedSpace());
    }

    @Test
    void testGetTotalEVSpace() throws FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        assertEquals(10, myParking.getTotalEVSpace());
        Stall stall = new Stall(299);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals(9, myParking.getTotalEVSpace());
        myParking.removeVehicle(vehicle);
        assertEquals(10, myParking.getTotalEVSpace());
    }


    @Test
    void testGetParkArea() throws FullParkException, ParkedStallException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertEquals("Regular Parking.", myParking.getParkArea(stall));

        stall = new Stall(256);
        vehicle = new Vehicle("test2");
        myParking.addVehicle(stall, vehicle);
        assertEquals("Reserved Parking.", myParking.getParkArea(stall));

        stall = new Stall(280);
        vehicle = new Vehicle("test3");
        myParking.addVehicle(stall, vehicle);
        assertEquals("Handicapped Parking.", myParking.getParkArea(stall));

        stall = new Stall(299);
        vehicle = new Vehicle("test4");
        myParking.addVehicle(stall, vehicle);
        assertEquals("EV Charging Station.", myParking.getParkArea(stall));
    }

    @Test
    void testGetRate() {
        assertEquals(4.0, myParking.getRate());
    }
}
