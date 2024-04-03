package Observer_model;

import Checkrelative.CheckMachine;
import Exceptionrelative.FormatException;
import Flightrelative.Flight;

import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

public class Observable_flights_line {
    private Queue<Observer> flights = new LinkedList<>();

    // Getters and setters for flights queue
    public Queue<Observer> getFlights() {
        return flights;
    }

    public void setFlights(Queue<Observer> flights) {
        this.flights = flights;
    }

    // Default constructor
    public Observable_flights_line() {

    }

    // Method to register an observer
    public void register(Observer observer){
        flights.add(observer);
    }

    // Method to get flights from CSV and add them to the queue
    public Queue<Observer> getinqueue(){
        HashMap<String, Flight> flist = null;
        try {
            flist = CheckMachine.readFlight("FlightList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }
        for(Flight value:flist.values()){
            // Add flights to the queue
            flights.offer(value);
        }
        return flights;
    }

    // Method to notify all registered observers with a message
    public void notifyObservers(String message) {
        // Iterate through each registered observer in the list
        for (Observer observer : flights) {
            // Call the update method of each observer to pass the message
            observer.update(message);
        }
    }

}
