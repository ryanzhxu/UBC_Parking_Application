package model;

public class WelcomeMessage {

    private static WelcomeMessage single_instance = null;

    public String str;

    private WelcomeMessage() {
        str = "Welcome to the University of British Columbia!";
    }

    public static WelcomeMessage getInstance() {
        if (single_instance == null) {
            single_instance = new WelcomeMessage();
        }
        return single_instance;
    }
}
