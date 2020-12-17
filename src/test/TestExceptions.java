import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestExceptions {

    private Parking myParking;
    private Stall stall = new Stall(1);
    private Vehicle vehicle = new Vehicle("main");

    @BeforeEach
    void runBefore() {
        myParking = new IndoorParking("TestExceptions");
    }

    @Test
    void TestEmptyParkException() {
        assertTrue(myParking.isEmpty());
        try {
            myParking.removeVehicle(vehicle);
        } catch (VehicleNotFoundException e) {
            fail();
        } catch (EmptyParkException e) {
            // expected
        }
    }

    @Test
    void TestFullParkException() {
        try {
            for (int i = 0; i < 300; i++) {
                stall = new Stall(i);
                vehicle = new Vehicle("");
                myParking.addVehicle(stall, vehicle);
            }
            assertTrue(myParking.isFull());
            myParking.addVehicle(stall, vehicle);
        } catch (FullParkException e) {
            // expected
        } catch (ParkedStallException e) {
            fail();
        }
    }

    @Test
    void TestParkedStallException() {
        try {
            myParking.addVehicle(stall, vehicle);
            myParking.addVehicle(stall, new Vehicle("123"));
        } catch (ParkedStallException e) {
            // expected
        } catch (FullParkException e) {
            fail();
        }
    }

    @Test
    void TestVehicleNotFoundException() {
        try {
            myParking.addVehicle(stall, vehicle);
            myParking.removeVehicle(new Vehicle("NotFound"));
        } catch (EmptyParkException | FullParkException | ParkedStallException e) {
            fail();
        } catch (VehicleNotFoundException e) {
            // expected
        }

    }

}
