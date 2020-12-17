package model;

import observer.VehicleSaver;
import ui.UbcParking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public abstract class Parking extends Observable implements Parkade, Loadable, Savable {

    private String name;
    private UbcParking myParking;
    protected Map<Stall, Vehicle> listParking;

    public Parking(String name, int numOfStalls) {
        this.name = name;
        this.listParking = new HashMap<>(numOfStalls);
        addObserver(new VehicleSaver());
    }

    public abstract double getRate();

    public Map<Stall, Vehicle> getEntry() {
        return listParking;
    }

    // EFFECTS: return parking name
    @Override
    public String getParkingName() {
        return this.name;
    }

    // EFFECTS: return total space available in the parking
    @Override
    public abstract int getTotalFreeSpace();

    // EFFECTS: return number of parked stalls
    @Override
    public abstract int getTotalParkedSpace();

    @Override
    public abstract int getTotalReservedSpace();

    @Override
    public abstract int getTotalHandicappedSpace();

    @Override
    public abstract int getTotalEVSpace();

    // REQUIRES: lowerBound >= 0 and upperBound <= listParking.size()
    // EFFECTS: return no greater than listParking.size()
    @Override
    public int getSpaceByInterval(int lowerBound, int upperBound) {
        int c = 0;
        for (Map.Entry<Stall, Vehicle> entry : listParking.entrySet()) {
            int stallNum = entry.getKey().getStallNum();
            if (stallNum >= lowerBound && stallNum <= upperBound && entry.getValue() != null) {
                c++;
            }
        }
        return c;
    }

    @Override
    public abstract String getParkArea(Stall stall);

    // EFFECT: return vehicle by stall if found, return null otherwise
    public Vehicle getVehicleByStall(Stall stall) {
        if (listParking.containsKey(stall)) {
            return listParking.get(stall);
        }
        return null;
    }

    // EFFECTS: return the stall the given vehicle is parked at if the vehicle exists, return null otherwise
    @Override
    public Stall getStallByVehicle(Vehicle vehicle) {
        if (vehicleExist(vehicle)) {
            return vehicle.getStall();
        }
        return null;
    }

    // EFFECTS: return true is parkade is full, false if otherwise
    @Override
    public boolean isFull() {
        return getTotalFreeSpace() == 0;
    }

    // EFFECTS: return true is parkade is empty, false if otherwise
    @Override
    public boolean isEmpty() {
        return getTotalParkedSpace() == 0;
    }

    // MODIFIES: this
    // EFFECTS: add a vehicle to parking list if parking is not full and the stall is not parked
    @Override
    public void addVehicle(Stall stall, Vehicle vehicle) throws ParkedStallException, FullParkException {
        if (!isFull()) {
            if (stall.isParked()) {
                throw new ParkedStallException();
            } else {
                vehicle.setParking(this);
                vehicle.setStall(stall);
                stall.setVehicle(vehicle);
                setChanged();
                notifyObservers(vehicle);
                listParking.put(stall, vehicle);
            }
        } else {
            throw new FullParkException();
        }
    }

    // MODIFIES: this
    // EFFECTS: remove a vehicle from the parking list
    @Override
    public void removeVehicle(Vehicle vehicle) throws EmptyParkException, VehicleNotFoundException {
        if (!isEmpty()) {
            if (!vehicleExist(vehicle)) {
                throw new VehicleNotFoundException();
            } else {
                Stall stall = getStallByVehicle(vehicle);
                listParking.remove(stall, vehicle);
                vehicle.removeParking();
//                stall.removeVehicle();
            }
        } else {
            throw new EmptyParkException();
        }
    }

    // EFFECTS: return true if vehicle exists in the parking
    @Override
    public boolean vehicleExist(Vehicle vehicle) {
        return listParking.containsValue(vehicle);
    }

    // MODIFIES: this
    // EFFECTS: read each line from list of lines and add vehicles to list
    protected void loadByLines(List<String> lines) {
        for (String line : lines) {
            if (!line.equals("Vehicles Parking Log")) {
                int index = line.indexOf(' ');
                Vehicle vehicle = new Vehicle(line.substring(0, index));
                int stallNum = Integer.parseInt(line.substring(index + 1));
                Stall stall = new Stall(stallNum);
                listParking.put(stall, vehicle);
            }
        }
    }

    // EFFECTS: save parking list as logs for output
    protected void writeByLines(PrintWriter writer) {
        writer.println("Vehicles Parking Log");
        for (Map.Entry<Stall, Vehicle> entry : listParking.entrySet()) {
            if (entry.getValue() != null) {
                String plate = entry.getValue().getLicensePlate();
                int stallNum = entry.getKey().getStallNum();
                writer.println(plate + " " + stallNum);
            }
        }
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: read lines from specific file and import data to parking
    @Override
    public void load(String filename) throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            loadByLines(lines);
        } else {
            throw new FileNotFoundException();
        }
    }

    // MODIFIES: this
    // EFFECTS: read lines from default file and import data to parking
    public void load() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputDefault.txt"));
        loadByLines(lines);
    }

    // EFFECTS: save data to specific file by name
    @Override
    public void save(String filename) throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writeByLines(writer);
            System.out.println("All vehicles logs have been saved at " + filename);
        } else {
            throw new FileNotFoundException();
        }
    }

    // EFFECTS: save data to default file
    public void save() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("outputDefault.txt", "UTF-8");
        writeByLines(writer);
        System.out.println("All vehicles logs have been saved at outputDefault.txt");
    }

}
