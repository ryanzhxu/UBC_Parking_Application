import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class TestSavable {
    private Parking myParking;
//    private FileOperations fo;

    @BeforeEach
    void runBefore() {
        myParking = new IndoorParking("TestParking");
//        fo = new FileOperations(myParking);
    }

    @Test
    void testSave() throws IOException, FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("testSave");
        myParking.addVehicle(stall, vehicle);
        assertTrue(myParking.vehicleExist(vehicle));
        myParking.save("testOutput.txt");
        myParking.removeVehicle(vehicle);
        myParking.load("testOutput.txt");
        assertTrue(myParking.vehicleExist(vehicle));
    }

    @Test
    void testSaveDefault() throws IOException, FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
        Stall stall = new Stall(1);
        Vehicle vehicle = new Vehicle("testSave");
        myParking.addVehicle(stall, vehicle);
        assertTrue(myParking.vehicleExist(vehicle));
        myParking.save();
        myParking.removeVehicle(vehicle);
        myParking.load("testOutput.txt");
        assertTrue(myParking.vehicleExist(vehicle));
    }

//    @Test
//    void testWriteByLines() throws IOException, FullParkException, ParkedStallException, EmptyParkException, VehicleNotFoundException {
//        PrintWriter writer = new PrintWriter("testWriteByLines.txt", "UTF-8");
//        Stall stall = new Stall(1);
//        Vehicle vehicle = new Vehicle("testWriteByLines");
//        myParking.addVehicle(stall, vehicle);
//        myParking.writeByLines(writer);
//        myParking.removeVehicle(vehicle);
//        myParking.load("testWriteByLines.txt");
//        assertTrue(myParking.vehicleExist(vehicle));
//    }

    @Test
    void testFileNotFoundExc() throws IOException {
        try {
            myParking.save("NotExist.txt");
            fail("I was not expecting to reach this line of code!");
        } catch (FileNotFoundException e) {
            System.out.println("Great!");
        }
    }

    @Test
    void testFileFoundExc() throws IOException {
        try {
            myParking.save("Output.txt");
            System.out.println("Great!");
        } catch (FileNotFoundException e) {
            fail("I was not expecting FileNotFoundException!");
        }
    }


}
