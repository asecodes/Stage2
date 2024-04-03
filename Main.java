import Checkrelative.FinalCheckinDesk;
import GUIrelative.ShowerGUI;
import Observer_model.Observable_flights_line;
import Observer_model.Observable_passengers_line;
import Observer_model.Observer;
import Passengerrelative.Passenger;
import Log.Log;
import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        // Create an instance of the Log class
        Log log = Log.getInstance();

        // Atomic boolean to indicate whether the current time is within tolerance range
        AtomicBoolean isWithinRange = new AtomicBoolean(false);
        // Tolerance range in milliseconds
        long tolerance = 1500;
        // Increment for generating future times in milliseconds
        long increment = 30000;

        // Redirect standard output stream to the Log class
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
            }
        }) {
            @Override
            public void println(String x) {
                log.logMessage(x);
            }
        });

        // Create Observable_passengers_line and Observable_flights_line objects
        Observable_passengers_line opl = new Observable_passengers_line();
        Observable_flights_line ofl = new Observable_flights_line();

        // Calculate the target size for each queue
        int targetSize = (opl.getinqueue().size() / 3) + 1;

        // Create arrays of queues for passengers
        Queue<Observer>[] pl = new Queue[3];

        // Split passengers into three queues based on priority
        for (int i = 0; i < 3; i++) {
            pl[i] = new LinkedList<>();

            for (Observer passenger : opl.getPassengers()) {
                if (passenger.getPriority() == i) {
                    pl[i].offer(passenger);
                }
            }
        }

        // Queue for flights
        Queue<Observer> fl = ofl.getinqueue();

        // Array to store future times
        String[] futureTimes = new String[fl.size()];

        // Calendar instance for generating future times
        Calendar calendar = Calendar.getInstance();

        // Generate future times and store them in the array
        for (int i = 0; i < futureTimes.length; i++) {
            calendar.setTimeInMillis(System.currentTimeMillis() + i * increment);
            futureTimes[i] = String.format("%04d-%02d-%02d %02d:%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE));
        }

        // Executor service for managing threads
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // Array of FinalCheckinDesk instances
        FinalCheckinDesk[] desk = new FinalCheckinDesk[3];
        for (int i = 0; i < 3; i++) {
            desk[i] = new FinalCheckinDesk(i);
        }
        // Record start time
        long startTime = System.currentTimeMillis();
        System.out.println("Check-in start time：" + startTime + "\n");

        // Create an instance of ShowerGUI
        ShowerGUI showerGUI = new ShowerGUI(pl, fl, futureTimes);
        // Set the window visible
        showerGUI.setVisible(true);

        // Array to store delay time for updating the GUI
        final int[] t = {2000};
        for (int i = 0; i < pl.length; i++) {
            // Main loop for processing passengers
            while (!pl[i].isEmpty() && !executor.isShutdown() && showerGUI.getMap() != null) {
                // Atomic integer for counting flights within tolerance range
                AtomicInteger p = new AtomicInteger();
                try {
                    // Check if it's time to shut down the check-in process
                    if ((System.currentTimeMillis() - startTime) >= 600 * 1000) {
                        System.out.println("Dear all passengers, attention please! All check-in processes will cease soon in 15 seconds!\n");
                        executor.shutdown();
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            System.out.println("Dear all passengers, attention please! All check-in processes have ceased.\n");
                        }
                        break;
                    }

                    // Loop through each desk
                    for (int k = 0; k < 3; k++) {
                        // Check if the desk is closed
                        if ((showerGUI.getDeskbutton())[k].getText().equals("DESK " + (k+1) + ":" + "\n" + "CLOSED")) {
                            desk[k].setRunnable(false);
                        } else {
                            desk[k].setRunnable(true);
                        }

                        Observer tp;

                        // Check if the desk is available for check-in
                        if (!desk[k].isRunning() && desk[k].isRunnable()) {
                            // Retrieve passenger from the queue
                            tp = pl[i].poll();
                            if (tp != null && fl != null) {
                                // Assign passenger to the desk and submit to executor
                                desk[k].setPassenger(tp);
                                executor.submit(desk[k]);
                                int finalK = k;
                                // Update GUI on event dispatch thread
                                SwingUtilities.invokeLater(() -> {
                                    if (showerGUI.getFlightsLabel().getText().contains("PUNCTUAL")) {
                                        showerGUI.updateQueueData(pl);
                                        showerGUI.onNewQueuePassengerReceived((Passenger) tp);
                                        showerGUI.appendToDeskAreas(tp, finalK, fl, futureTimes);
                                        showerGUI.updateFlightsPanel(tp, fl, futureTimes);

                                        // Check if current time is within tolerance range of future times
                                        for (String time : futureTimes) {
                                            try {
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                Date futureTime = sdf.parse(time);

                                                if (Math.abs(System.currentTimeMillis() - futureTime.getTime()) <= tolerance) {
                                                    isWithinRange.set(true);
                                                    break;
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        // If within range, delete the first flight
                                        if (isWithinRange.get()) {
                                            if (showerGUI.getFlightlistModel().getSize() > 0 && p.get() < 1) {
                                                System.out.println("Implement delete");
                                                showerGUI.getFlightlistModel().removeElementAt(0);
                                                isWithinRange.set(false);
                                                p.getAndIncrement();
                                            }
                                        }
                                    }
                                });
                            }
                        }

                        // Print message if desk is occupied
                        if (desk[k].isRunning()) {
                            System.out.println("The desk " + k + " is occupied now.");
                        }
                    }

                    // Determine delay time for updating GUI based on button selection
                    switch (showerGUI.getButton().getText()) {
                        case "Default":
                            t[0] = 2000;
                            break;
                        case "x1.5":
                            t[0] = 1000;
                            break;
                        case "x0.5":
                            t[0] = 3000;
                            break;
                    }

                    // Pause execution for delay time
                    try {
                        Thread.sleep(t[0]);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Print current time for debugging
                    System.out.println("Current time：" + System.currentTimeMillis() + "\n");
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
