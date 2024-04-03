package Passengerrelative;

import Passengerrelative.Passenger;

import java.util.HashMap;

public class PassengerListManager {
    // Use a HashMap to store passenger information, the key is the booking reference code and the value is the Passenger object
    final private HashMap<String, Passenger> passengerMap;

    // Constructor to initialise the HashMap
    public PassengerListManager(HashMap<String, Passenger> passengerMap) {
        this.passengerMap = passengerMap;
    }

    // Add Passenger
    public void addPassenger(Passenger passenger) {
        passengerMap.put(passenger.getReferenceCode(), passenger);
    }

    // Getting passengers based on booking reference code
    public Passenger getPassenger(String referenceCode) {
        return passengerMap.get(referenceCode);
    }

    // Deletion of passengers based on booking reference code
    public void removePassenger(String referenceCode) {
        passengerMap.remove(referenceCode);
    }

    // Print all passenger information
    public void exportPassengerList() {
        for (Passenger passenger : passengerMap.values()) {
            System.out.println(passenger.toString());
        }
    }
}
