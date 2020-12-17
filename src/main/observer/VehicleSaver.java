package observer;

import java.util.Observable;
import java.util.Observer;

public class VehicleSaver implements Observer {

    // EFFECTS: print out what each action has accomplished so far
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Being parked: " + arg);
    }
}
