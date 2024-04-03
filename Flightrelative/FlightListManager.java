package Flightrelative;

import Flightrelative.Flight;

import java.util.HashMap;

public class FlightListManager {

    final private HashMap<String, Flight> flightMap;

    public FlightListManager(HashMap<String, Flight> flightMap) {
        this.flightMap = flightMap;
    }

    public void addFlight(Flight flight) {
        flightMap.put(flight.getFlightCode(), flight);
    }

    // Get flights based on booking reference code
    public Flight getFlight(String flightCode) {
        return flightMap.get(flightCode);
    }

    // Delete flights based on booking reference code
    public void removeFlight(String flightCode) {
        flightMap.remove(flightCode);
    }

    // Print all passenger information
    public void exportFlightList() {
        for (Flight flight : flightMap.values()) {
            System.out.println(flight.toString());
        }
    }
}
