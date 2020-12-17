import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestParking {
    private Parking myParking;

    @BeforeEach
    void runBefore() {
        myParking = new IndoorParking("TestParking");
    }

    @Test
    void testPark() throws FullParkException, ParkedStallException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("CPSC210");
        Vehicle notExist = new Vehicle("");
        myParking.addVehicle(stall, vehicle);
        Map<Stall, Vehicle> entry = myParking.getEntry();
        assertTrue(entry.containsValue(vehicle));
        assertFalse(entry.containsValue(notExist));
    }

    @Test
    void testVehicleExist() throws FullParkException, ParkedStallException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("CPSC210");
        Vehicle notExist = new Vehicle("");
        myParking.addVehicle(stall, vehicle);
        assertTrue(myParking.vehicleExist(vehicle));
        assertFalse(myParking.vehicleExist(notExist));
    }

    @Test
    void testGetParkingName() {
        assertEquals("TestParking", myParking.getParkingName());
    }

    @Test
    void testGetTotalSpace() throws FullParkException, ParkedStallException {
        Stall stall = new Stall(3);
        Stall stall2 = new Stall(5);
        Vehicle vehicle = new Vehicle("CPSC210");
        Vehicle vehicle2 = new Vehicle("UBC123");
        myParking.addVehicle(stall, vehicle);
        myParking.addVehicle(stall2, vehicle2);
        assertEquals(298, myParking.getTotalFreeSpace());
    }

    @Test
    void testRemovePark() throws VehicleNotFoundException, FullParkException, ParkedStallException, EmptyParkException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("CPSC210");
        myParking.addVehicle(stall, vehicle);
        assertTrue(myParking.vehicleExist(vehicle));
        myParking.removeVehicle(vehicle);
        assertFalse(myParking.vehicleExist(vehicle));
    }

    @Test
    void testIsFull() throws VehicleNotFoundException, EmptyParkException, FullParkException, ParkedStallException {
        Stall stall;
        Vehicle vehicle;
        for (int i = 0; i < 299; i++) {
            stall = new Stall(i);
            vehicle = new Vehicle("");
            myParking.addVehicle(stall, vehicle);
        }
        stall = new Stall(300);
        vehicle = new Vehicle("test");
        myParking.addVehicle(stall, vehicle);
        assertTrue(myParking.isFull());
        myParking.removeVehicle(vehicle);
        assertFalse(myParking.isFull());
    }

    @Test
    void testIsEmpty() throws FullParkException, ParkedStallException {
        assertTrue(myParking.isEmpty());
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("");
        myParking.addVehicle(stall, vehicle);
        assertFalse(myParking.isEmpty());
    }

//    @Test
//    void testGetStallByVehicle() throws VehicleNotFoundException, FullParkException, ParkedStallException, EmptyParkException {
//        Stall stall = new Stall(1);
//        Vehicle vehicle = new Vehicle("");
//        myParking.addVehicle(stall, vehicle);
//        myParking.removeVehicle(vehicle);
//        assertNull(myParking.getStallByVehicle(vehicle));
//        myParking.addVehicle(stall, vehicle);
//        assertEquals(stall, myParking.getStallByVehicle(vehicle));
//    }

    @Test
    void testGetSpaceByInterval() throws FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        assertEquals(0, myParking.getSpaceByInterval(300, 1000));
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("Test");
        myParking.addVehicle(stall, vehicle);
        assertEquals(1, myParking.getSpaceByInterval(1, 2));
        myParking.removeVehicle(vehicle);
        assertTrue(myParking.isEmpty());
        assertEquals(0, myParking.getSpaceByInterval(300, 0));
   }

    @Test
    void testGetVehicle() throws FullParkException, ParkedStallException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("");
        myParking.addVehicle(stall, vehicle);
        assertEquals(vehicle, myParking.getVehicleByStall(stall));
    }

}
