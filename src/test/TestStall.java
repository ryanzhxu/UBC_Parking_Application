import model.Stall;
import model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStall {

    private Stall stall;
    private Vehicle vehicle;

    @BeforeEach
    void runBefore() {
        stall = new Stall(210);
        vehicle = new Vehicle("testVehicle");
    }

    @Test
    void testConstructor() {
        assertEquals(210, stall.getStallNum());
    }

    @Test
    void testIsParked() {
        stall.setVehicle(vehicle);
        assertTrue(stall.isParked());
        stall.removeVehicle();
        assertFalse(stall.isParked());
    }

    @Test
    void testSetVehicle() {
        stall.setVehicle(vehicle);
        stall.setVehicle(vehicle);
    }

}
