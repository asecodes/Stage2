package Checkrelative;

import Exceptionrelative.FormatException;
import Flightrelative.Flight;
import Observer_model.Observer;
import Passengerrelative.Passenger;

import java.util.HashMap;

public class FinalCheckinDesk extends Thread {
    private int deskID;

    private Observer passenger;
    private Observer flight;

    private boolean isRunning;
    private boolean Runnable;

    public boolean isRunnable() {
        return Runnable;
    }

    public void setRunnable(boolean runnable) {
        Runnable = runnable;
    }

    private HashMap<String, Passenger> plist;

    {
        try {
            // Read passenger information from file
            plist = CheckMachine.readPassenger("PassengerList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<String, Flight> flist;

    {
        try {
            // Read flight information from file
            flist = CheckMachine.readFlight("FlightList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }
    }

    public int getDeskID() {
        return deskID;
    }

    public void setDeskID(int deskID) {
        this.deskID = deskID;
    }

    public Observer getPassenger() {
        return passenger;
    }

    public void setPassenger(Observer passenger) {
        this.passenger = passenger;
    }

    public Observer getFlight() {
        return flight;
    }

    public void setFlight(Observer flight) {
        this.flight = flight;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public FinalCheckinDesk(int deskID) {
        // Initialize FinalCheckinDesk with deskID
        this.deskID = deskID;
        this.isRunning = false;
    }

    public FinalCheckinDesk() {
        this.isRunning = false;
    }

    // Override run method from Thread class
    public void run() {
        try {
            setRunning(true);
            if (isRunning) {
                System.out.println("CheckinDesk " + deskID + " is checking in for boarding, please be patient or search for available CheckinDesk.\n");
            }

            try {
                // Check passenger information
                CheckMachine.checkInformation(getPassenger().getReferenceCode(), getPassenger().getName(), plist, flist);
            } catch (FormatException ex) {
                // Handle format exception
            }
            // Simulate time-consuming operation, sleep for 1000 milliseconds.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Interrupted exception handled
            Thread.currentThread().interrupt(); // Reset interrupt status
        } finally {
            // Set isRunning to false after task completion
            setRunning(false);
            if (!isRunning) {
                System.out.println("CheckinDesk " + deskID + " is available now.\n");
            }
        }
    }
}
