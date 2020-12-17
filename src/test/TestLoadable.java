import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class TestLoadable {
    private Parking myParking;
//    private FileOperations fo;

    @BeforeEach
    void runBefore() {
        myParking = new IndoorParking("TestParking");
//        fo = new FileOperations(myParking);
    }


    @Test
    void testLoad() throws IOException {
        myParking.load("testInput.txt");
        Map<Stall, Vehicle> listVehicles = myParking.getEntry();
        for (Map.Entry<Stall, Vehicle> entry: listVehicles.entrySet()) {
            Stall stall = entry.getKey();
            Vehicle vehicle = myParking.getVehicleByStall(stall);
            assertEquals(vehicle, entry.getValue());
        }
        Vehicle vehicle = new Vehicle("notExist");
        assertFalse(myParking.vehicleExist(vehicle));
        myParking.save("testOutput2.txt");

        List<String> inputLines = Files.readAllLines(Paths.get("testInput.txt"));
        List<String> outputLines = Files.readAllLines(Paths.get("testOutput2.txt"));

        boolean isSame = inputLines.equals(outputLines);
        assertTrue(isSame);
    }

    @Test
    void testLoadDefault() throws IOException {
        myParking.load();
        Vehicle vehicle = new Vehicle("UBC123");
        assertTrue(myParking.vehicleExist(vehicle));
        vehicle = new Vehicle("notExist");
        assertFalse(myParking.vehicleExist(vehicle));
        myParking.save("testOutputDefault.txt");

        List<String> inputLines = Files.readAllLines(Paths.get("inputDefault.txt"));
        List<String> outputLines = Files.readAllLines(Paths.get("testOutputDefault.txt"));

        boolean isSame = false;

        for (int i = 1; i < inputLines.size(); i++) {
            isSame = inputLines.get(i).equals(outputLines.get(i));
        }

        assertTrue(isSame);
    }

    @Test
    void testFileExist() {
        File file = new File("testExist.txt");
        assertFalse(file.exists());
    }

    @Test
    void testFileNotFoundExc() throws IOException {
        try {
            myParking.load("NotExist.txt");
            fail("I was not expecting to reach this line of code!");
        } catch (FileNotFoundException e) {
            System.out.println("Great!");
        }
    }

    @Test
    void testFileFoundExc() throws IOException {
        try {
            myParking.load("input.txt");
        } catch (FileNotFoundException e) {
            fail("I was not expecting FileNotFoundException!");
        }
    }

//    @Test
//    void testLoadByLines() throws IOException {
//        List<String> lines = Files.readAllLines(Paths.get("testLoadByLines.txt"));
//        myParking.loadByLines(lines);
//        assertTrue(myParking.isEmpty());
//        List<String> lines2 = Files.readAllLines(Paths.get("testInput.txt"));
//        myParking.loadByLines(lines2);
//        assertFalse(myParking.isEmpty());
//    }


}
