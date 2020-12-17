import model.Stall;
import model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestVehicle {

    private Vehicle vehicle;

    @BeforeEach
    void runBefore() {
        vehicle = new Vehicle("TestVehicle");
    }

    @Test
    void testConstructor() {
        assertEquals("TestVehicle", vehicle.getLicensePlate());
    }

    @Test
    void testEquals() {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("test");
        Vehicle vehicle2 = stall.getVehicle();
        assertNotEquals(vehicle, vehicle2);
        assertNotEquals(vehicle, vehicle2);
        assertNotEquals(vehicle, stall);
    }

    @Test
    void testSetStall() {
        Stall stall = new Stall(1);
        vehicle.setStall(stall);
        assertEquals(stall, vehicle.getStall());
    }

    @Test
    void testRemoveStall() {
        Stall stall = new Stall(1);
        vehicle.setStall(stall);
        vehicle.removeStall();
        assertNull(vehicle.getStall());
    }
}
