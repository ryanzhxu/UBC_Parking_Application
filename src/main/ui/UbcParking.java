package ui;

import model.*;
import network.ReadWebPageEx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class UbcParking {
    private Scanner scanner;
    private Parking indoorParking;
    private Parking outdoorParking;
    private Parking myParking;
    private boolean keepGoing;
    Date today = Calendar.getInstance().getTime();


    public UbcParking() throws IOException {
        scanner = new Scanner(System.in);
        indoorParking = new IndoorParking("Indoor Parking");
        outdoorParking = new OutdoorParking("Outdoor Parking");
        keepGoing = true;
    }

    public static void main(String[] args) throws IOException {
        new ReadWebPageEx();
        UbcParking park = new UbcParking();
        park.tryGo();
        park.trySave();
    }

    private void go() throws InvalidSelException, IOException {
        WelcomeMessage w = WelcomeMessage.getInstance();
        System.out.println(w.str);
        System.out.println("Welcome to UBC Parking!\nWhere would you like to park?");
        DisplayMenu.displayParkingMenu();
        try {
            selectParking(scanner.nextInt());
        } catch (InvalidSelException e) {
            System.out.println("Invalid selection...");
        }
    }

    private void selectParking(int command) throws IOException, InvalidSelException {
        if (command == 1) {
            DisplayMenu.displayIndoorRates();
            myParking = indoorParking;
            System.out.println(myParking.getParkingName() + " is selected.");

        } else if (command == 2) {
            DisplayMenu.displayOutdoorRates();
            myParking = outdoorParking;
            System.out.println(myParking.getParkingName() + " is selected.");
        } else {
            throw new InvalidSelException();
        }
    }

    private void tryGo() throws IOException {
        while (keepGoing) {
            try {
                go();
            } catch (InvalidSelException e) {
                System.out.println("Selection not valid...");
            }
            DisplayMenu.displayMenu();
            int command = scanner.nextInt();
            run(command);
        }
    }

    private void run(int command) throws IOException {
        if (command == 1) {
            tryDoPark();
        } else if (command == 2) {
            tryDoRemove();
        } else if (command == 3) {
            System.out.println("There are " + myParking.getTotalFreeSpace() + " parking spaces available.");
        } else if (command == 4) {
            tryLoad();
        } else {
            System.out.println("You have selected to: exit the program.");
            keepGoing = false;
        }
    }

    private void save() throws IOException {
        System.out.println("Save Logs?");
        System.out.println("\nSelect from: ");
        System.out.println("\t[y] -> yes, save");
        System.out.println("\t[n] -> no, do not save");
        String command = scanner.next();
        command = command.toLowerCase();
        if (command.equals("y")) {
            String location = saveLocation();
            myParking.save(location);
        } else if (command.equals("n")) {
            System.out.println("Logs have not been saved...");
        } else {
            System.out.println("Selection not valid...");
        }
    }

    private String saveLocation() {
        System.out.println("Please select one of the following to save file");
        while (true) {
            DisplayMenu.displaySaveMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    return "outputDefault.txt";
                case 2:
                    System.out.println("Please enter the file name: ");
                    return scanner.next();
                default:
                    System.out.println("Selection not valid...");
                    break;
            }
        }
    }

    private void trySave() throws IOException {
        try {
            save();
        } catch (FileNotFoundException e) {
            System.out.println("File not found... \nCreate file by name? \n\t[y] -> yes \n\t[n] -> no");
            String command = scanner.next();
            command = command.toLowerCase();
            if (command.equals("y")) {
                System.out.println("Please enter the file name:");
                String location = scanner.next();
                myParking.save(location);
                System.out.println("Logs have been saved at " + location);
            } else if (command.equals("n")) {
                System.out.println("Logs have not been saved...");
            }
        } finally {
            System.out.println("Thank you for using UBC Parking!");
        }
    }

    private String loadLocation() {
        System.out.println("Please select one of the following to load file");
        while (true) {
            DisplayMenu.displayLoadMenu();
            int command = scanner.nextInt();
            if (command == 1) {
                return "inputDefault.txt";
            } else if (command == 2) {
                System.out.println("please enter the file name:");
                return scanner.next();
            } else {
                System.out.println("Selection not valid...");
            }
        }
    }

    private void tryLoad() throws IOException {
        try {
            myParking.load(loadLocation());
        } catch (FileNotFoundException e) {
            System.out.println("File not found... \nRetry? \n\t[y] -> yes \n\t[n] -> no");
            String command = scanner.next();
            command = command.toLowerCase();
            if (command.equals("y")) {
                System.out.println("Please enter the file name:");
                String location = scanner.next();
                myParking.load(location);
                System.out.println("Logs have been loaded from " + location);
            } else if (command.equals("n")) {
                System.out.println("Logs have not been loaded...");
            }
        }
    }

    private void doPark() throws FullParkException, ParkedStallException {
        if (myParking.isFull()) {
            throw new FullParkException();
        } else {
            System.out.println("Please enter your vehicle's plate number:");
            Vehicle vehicle = new Vehicle(scanner.next());
            System.out.println("Please enter the number of the stall you have parked at:");
            Stall stall = new Stall(scanner.nextInt());
            int stallNum = stall.getStallNum();
            String plate = vehicle.getLicensePlate();
            if (tryPark(stall, vehicle)) {
                displayReceipt(vehicle);
            } else {
                throw new ParkedStallException();
            }
        }
    }

    private void displayReceipt(Vehicle vehicle) {
        System.out.println("How long would you like to park");
        int duration = scanner.nextInt();
        if (duration >= 5) {
            DisplayMenu.displayInvoiceFullDay(vehicle);
            System.out.println("\tTotal Due: $" + 5 * myParking.getRate());
            System.out.println("Setting: " + myParking.getParkingName() + "\n");
        } else {
            DisplayMenu.displayInvoice(vehicle, duration);
            System.out.println("\tTotal Due: $" + duration * myParking.getRate());
            System.out.println("Setting: " + myParking.getParkingName() + "\n");
        }
    }

    private boolean tryPark(Stall stall, Vehicle vehicle) {
        try {
            myParking.addVehicle(stall, vehicle);
            return true;
        } catch (FullParkException | ParkedStallException e) {
            return false;
        }
    }

    private void tryDoPark() {
        try {
            doPark();
        } catch (FullParkException e) {
            System.out.println("This parking is full...");
        } catch (ParkedStallException e) {
            System.out.println("This stall has been parked...");
        } finally {
            System.out.println("Please come again...Thank you for choosing UBC Parking.\n");
        }
    }

    private void doRemove() throws EmptyParkException, VehicleNotFoundException {
        if (myParking.isEmpty()) {
            throw new EmptyParkException();
        } else {
            System.out.println("Please enter your vehicle's plate number");
            Vehicle vehicle = new Vehicle(scanner.next());
            Stall stall = myParking.getStallByVehicle(vehicle);
            int stallNum = stall.getStallNum();
            String plate = vehicle.getLicensePlate();
            if (myParking.vehicleExist(vehicle)) {
                myParking.removeVehicle(vehicle);
                System.out.println(plate + " has left stall " + stallNum + " at " + myParking.getParkingName());
            } else {
                throw new VehicleNotFoundException();
            }
        }
    }

    private void tryDoRemove() {
        try {
            doRemove();
        } catch (EmptyParkException e) {
            System.out.println("This parking is empty...");
        } catch (VehicleNotFoundException e) {
            System.out.println("Vehicle not found at this stall...");
        }
    }

}

