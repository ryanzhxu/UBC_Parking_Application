package ui;

import model.Vehicle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DisplayMenu {

    private static Date today = Calendar.getInstance().getTime();

    // EFFECTS: display main menu for program
    static void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t[1] -> park");
        System.out.println("\t[2] -> leave");
        System.out.println("\t[3] -> check total space");
        System.out.println("\t[4] -> load from file");
        System.out.println("\t[0] -> quit");
    }

    // EFFECTS: display parking options
    static void displayParkingMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t[1] -> Indoor");
        System.out.println("\t[2] -> Outdoor");
    }

    // EFFECTS: display invoice for full day parking
    static void displayInvoiceFullDay(Vehicle vehicle) {
        System.out.println("\t\tInvoice");
        System.out.println("\t\t" + vehicle.getLicensePlate());
        System.out.println("\tExpiration Time:");
        System.out.println("\t\t11:59 PM");
        System.out.println(today);
    }

    // EFFECTS: display invoice for a given duration
    static void displayInvoice(Vehicle vehicle, int duration) {
        System.out.println("\t\tInvoice");
        System.out.println("\t\t" + vehicle.getLicensePlate());
        System.out.println("\tExpiration Time:");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, +duration);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        System.out.println("\t\t" + hour + ":" + min);
        System.out.println(today);
    }

    // EFFECTS: display indoor parking rates
    static void displayIndoorRates() {
        System.out.println("\n Rates for indoor parking are as follows:");
        System.out.println("\t$4.00/hr max $20.00");
        System.out.println("\t$20.00/day until 11:59 PM");
    }

    // EFFECTS: display outdoor parking rates
    static void displayOutdoorRates() {
        System.out.println("\n Rates for outdoor parking are as follows:");
        System.out.println("\t$3.00/hr max $15");
        System.out.println("\t$15.00/day until 11:59 PM");
    }

    // EFFECTS: display menu for savings
    static void displaySaveMenu() {
        System.out.println("\nSelect from: ");
        System.out.println("\t[1] -> save by default");
        System.out.println("\t[2] -> provide file name");
    }

    // EFFECTS: display menu for loading
    static void displayLoadMenu() {
        System.out.println("\nSelect from: ");
        System.out.println("\t[1] -> load by default");
        System.out.println("\t[2] -> provide file name");
    }
}
