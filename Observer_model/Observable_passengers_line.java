package Observer_model;

import Checkrelative.CheckMachine;
import Exceptionrelative.FormatException;
import Passengerrelative.Passenger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

public class Observable_passengers_line {
    private Queue<Observer> passengers = new LinkedList<>();

    // Default constructor
    public Observable_passengers_line() {

    }

    // Getters and setters for passengers queue
    public Queue<Observer> getPassengers() {
        return passengers;
    }

    public void setPassengers(Queue<Observer> passengers) {
        this.passengers = passengers;
    }

    // Method to get passengers from CSV and add them to the queue
    public Queue<Observer> getinqueue(){
        HashMap<String, Passenger> plist = null;
        try {
            plist = CheckMachine.readPassenger("PassengerList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }
        for(Passenger value:plist.values()){
            // Add passengers to the queue
            if(!value.isChecked()) {
                passengers.offer(value);
            }
        }
        return passengers;
    }

    // Method to get the next passenger to check
    public Observer gotocheck(){
        return this.passengers.poll();
    }
}
