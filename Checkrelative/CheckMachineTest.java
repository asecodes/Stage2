package Checkrelative;

import Exceptionrelative.FormatException;
import Flightrelative.Flight;
import Passengerrelative.Passenger;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CheckMachineTest {

    @Test
    void checkInformation() {

        HashMap<String, Passenger> passengerMap = new HashMap<>();
        HashMap<String, Flight> flightMap = new HashMap<>();


        Passenger passenger = new Passenger("ABCD12345", "John Doe", "FL123", false, "20 * 15 * 10", 25.0f,1);
        passengerMap.put("ABCD12345", passenger);

        Flight flight = new Flight("FL123", "New York", "Los Angeles", 200, 50.0f, 100.0f, "20 * 20 * 20");
        flightMap.put("FL123", flight);


        assertDoesNotThrow(() -> CheckMachine.checkInformation("ABCD12345", "John Doe", passengerMap, flightMap));
    }

    @Test
    void calculateFee() {

        HashMap<String, Flight> flightMap = new HashMap<>();
        Flight flight = new Flight("FL123", "New York", "Los Angeles", 200, 50.0f, 100.0f, "20 * 20 * 20");
        flightMap.put("FL123", flight);


        float fee = CheckMachine.calculateFee(25.0f, "20 * 15 * 10", "FL123", flightMap);
        assertEquals(250.0f, fee);
    }

    @Test
    void readPassenger() {

        HashMap<String, Passenger> passengerMap = null;
        try {
            passengerMap = CheckMachine.readPassenger("passengerList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(passengerMap);
        assertFalse(passengerMap.isEmpty());
    }

    @Test
    void readFlight() throws FormatException {

        HashMap<String, Flight> flightMap = CheckMachine.readFlight("flightList.csv");
        assertNotNull(flightMap);
        assertFalse(flightMap.isEmpty());
    }
}
