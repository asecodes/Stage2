package GUIrelative;

import Checkrelative.CheckMachine;
import Exceptionrelative.FormatException;
import Flightrelative.Flight;
import Observer_model.Observable_flights_line;
import Observer_model.Observable_passengers_line;
import Observer_model.Observer;
import Passengerrelative.Passenger;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ShowerGUI extends JFrame {
    private JPanel queuePanel, desksPanel, flightsPanel, deskbuttonPanel; // Panels for displaying queue, desks, flights, and desk buttons
    private JLabel queueLabel; // Label for displaying queue information

    private JLabel flightsLabel; // Label for displaying flights information
    private JLabel[] deskLabel = new JLabel[3]; // Labels for displaying desk information
    private JList<Passenger> queueList; // List for displaying queue of passengers
    private JList<String> flightList; // List for displaying flights
    private DefaultListModel<Passenger> passengerlistModel; // Data model for queue list

    private DefaultListModel<String> flightlistModel; // Data model for flight list
    //private DefaultListModel<Passenger>[] passengerinformation = new DefaultListModel[3]; // Data model for desk passenger information
    private JList<Passenger>[] queueinformation = new JList[3]; // Lists for displaying queue information for each desk
    private JTextArea flightArea; // Text area for displaying flight details
    private JScrollPane queueScrollPane, flightsScrollPane; // Scroll panes for queue and flights lists
    private static final Random random = new Random(); // Random object for generating random numbers
    private JTextArea[] deskAreas = new JTextArea[3]; // Text areas for displaying desk details
    private JScrollPane[] deskScrollPanes = new JScrollPane[3]; // Scroll panes for desk text areas

    private JButton button = new JButton("Default"); // Button with default text

    private JButton deskbutton[] = new JButton[3]; // Buttons for desk operations
    public JButton[] getDeskbutton() { // Getter for desk buttons
        return deskbutton;
    }

    public void setDeskbutton(JButton[] deskbutton) { // Setter for desk buttons
        this.deskbutton = deskbutton;
    }
    public JButton getButton() { // Getter for default button
        return button;
    }

    public void setButton(JButton button) { // Setter for default button
        this.button = button;
    }

    private HashMap<String, Flight> map; // HashMap for storing flights
    private HashMap<String, Passenger> map2; // HashMap for storing passengers

    public HashMap<String, Passenger> getMap2() { // Getter for passenger HashMap
        return map2;
    }

    public void setMap2(HashMap<String, Passenger> map2) { // Setter for passenger HashMap
        this.map2 = map2;
    }

    public HashMap<String, Flight> getMap() { // Getter for flight HashMap
        return map;
    }

    public void setMap(HashMap<String, Flight> map) { // Setter for flight HashMap
        this.map = map;
    }

    public JLabel getFlightsLabel() { // Getter for flights label
        return flightsLabel;
    }

    public void setFlightsLabel(JLabel flightsLabel) { // Setter for flights label
        this.flightsLabel = flightsLabel;
    }

    public DefaultListModel<String> getFlightlistModel() { // Getter for flight list model
        return flightlistModel;
    }

    public void setFlightlistModel(DefaultListModel<String> flightlistModel) { // Setter for flight list model
        this.flightlistModel = flightlistModel;
    }




    public ShowerGUI(Queue<Observer>[] pl,Queue<Observer> fl,String[] futureTimes) {
        setTitle("Airport Queue System");
        setSize(1500, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initQueuePanel(pl);
        initDesksPanel();
        initFlightsPanel(fl,futureTimes);
        initDeskButtonPanel();

        add(queuePanel, BorderLayout.WEST);
        add(desksPanel, BorderLayout.CENTER);
        add(flightsPanel, BorderLayout.SOUTH);
        add(deskbuttonPanel,BorderLayout.EAST);
        // 调整组件的样式
        setComponentStyles();
    }

    private void initQueuePanel(Queue<Observer>[] pl) {
        int totalSize = 0;
        // Calculate the total number of passengers in the queue
        for (Queue<Observer> queue : pl) {
            totalSize += queue.size();
        }
        // Initialize the queue panel with BorderLayout
        queuePanel = new JPanel(new BorderLayout());
        // Create a label to display the total number of people in the queue
        queueLabel = new JLabel("There are currently " + totalSize + " people waiting in the queue:");
        // Initialize the data model for the passenger list
        passengerlistModel = new DefaultListModel<>();
        // Initialize the JList with the passenger data model
        queueList = new JList<>(passengerlistModel);
        // Create a scroll pane for the queue list
        queueScrollPane = new JScrollPane(queueList);
        queueScrollPane.setPreferredSize(new Dimension(300, 200));

        // Iterate through each observer in the queue
        for (Queue<Observer> queue : pl) {
            for (Observer observer : queue) {
                // Check if the observer is an instance of Passenger
                if (observer instanceof Passenger) {
                    // Check if the passenger is not checked in
                    if (!((Passenger) observer).isChecked()) {
                        // Convert the observer to a Passenger object
                        Passenger passenger = (Passenger) observer;
                        // Add the passenger to the data model
                        passengerlistModel.addElement(passenger);
                    }
                }
            }
        }

        // Add the queue label to the north and queue scroll pane to the center of the queue panel
        queuePanel.add(queueLabel, BorderLayout.NORTH);
        queuePanel.add(queueScrollPane, BorderLayout.CENTER);
    }

    // 更新队列面板的数据
    public void updateQueueData(Queue<Observer>[] pl) {
        int totalSize = 0;
        // Calculate the total number of passengers in the queue
        for (Queue<Observer> queue : pl) {
            totalSize += queue.size();
        }
        // Update the label to display the number of people waiting in the queue
        queueLabel.setText("There are currently " + totalSize + " people waiting in the queue:");

        // Update the content of the queue list
        // queueList.setListData(newQueueData);
    }

    // Method to add new data to the JList
    public void removeNewDataToQueue(Passenger newPassenger) {
        // Remove the corresponding passenger from the data model
        passengerlistModel.removeElement(newPassenger);
        // Update the label display
        // updateQueueLabel();
    }

    // Assume this method is called when new queue data is received
    public void onNewQueuePassengerReceived(Passenger newPassenger) {
        removeNewDataToQueue(newPassenger);
    }


    private void initDesksPanel() {
        desksPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        // desksPanel.setPreferredSize(new Dimension(600, 600)); // Set the preferred size of desksPanel
        for (int i = 0; i < 3; i++) {
            deskLabel[i] = new JLabel("Desk " + (i + 1));
            deskAreas[i] = new JTextArea("Desk " + (i + 1) + ":\n");
            deskAreas[i].setRows(10); // Set fixed number of rows
            deskAreas[i].setColumns(30); // Set fixed number of columns
            deskAreas[i].setLineWrap(true); // Enable automatic line wrapping
            deskAreas[i].setWrapStyleWord(true); // Wrap text by words
            // deskAreas[i].setPreferredSize(new Dimension(20, 200)); // Set fixed size
            deskScrollPanes[i] = new JScrollPane(deskAreas[i]);
            // deskScrollPanes[i].setPreferredSize(new Dimension(50, 300)); // Set fixed size
            desksPanel.add(deskScrollPanes[i], BorderLayout.CENTER);
        }
    }


    private void initDeskButtonPanel() {
        deskbuttonPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        deskbuttonPanel.setPreferredSize(new Dimension(200, 600)); // Set the preferred size of deskbuttonPanel
        // Create 3 panels
        //JPanel Panel[] = new JPanel[3];

        for (int i = 0; i < 3; i++) {
            deskbutton[i] = new JButton("DESK " + (i + 1) + ":" + "\n" + "OPEN"); // Initialize button and set text
            deskbutton[i].setPreferredSize(new Dimension(20, 30)); // Set the size of the button
            //Panel[i].add(deskbutton[i], BorderLayout.CENTER);
            // Add action listener to the button
            int finalI = i; // Variable must be declared final or effectively final

            // Add action listener to the button
            deskbutton[i].addActionListener(new ActionListener() {
                boolean isOpen = true; // Initial state is open
                int buttonIndex; // Index of the current button

                @Override
                public void actionPerformed(ActionEvent e) {
                    // Determine which queue to close or open based on the clicked button
                    JButton clickedButton = (JButton) e.getSource();
                    String buttonText = clickedButton.getText(); // Get the text of the current button
                    buttonIndex = Integer.parseInt(buttonText.substring(5, 6)) - 1; // Extract the index of the button
                    // Update the text of the button
                    if (isOpen) {
                        deskbutton[buttonIndex].setText("DESK " + (buttonIndex + 1) + ":" + "\n" + "CLOSED");
                    } else {
                        deskbutton[buttonIndex].setText("DESK " + (buttonIndex + 1) + ":" + "\n" + "OPEN");
                    }
                    isOpen = !isOpen; // Toggle the state
                }
            });

            deskbuttonPanel.add(deskbutton[i], BorderLayout.NORTH);
        }
    }



    // Method to append text to all JTextAreas
    public void appendToDeskAreas(Observer p, int i, Queue<Observer> fl, String[] futureTimes) {
        // Calculate the passenger's fee
        for (Observer flight : fl) {
            if (Objects.equals(((Passenger) p).getFlightCode(), ((Flight) flight).getFlightCode())) {
                boolean foundIndex = false; // Flag to track if a matching index is found

                for (int k = 0; k < futureTimes.length; k++) {
                    int index = flightlistModel.indexOf(((Flight) flight).getFlightCode() + " " + ((Flight) flight).getDestination()
                            + " " + ((Flight) flight).getFlightCode() +
                            ": " + ((Flight) flight).getCheckinNum() +
                            " checked in of " + ((Flight) flight).getMaxPassengerNum() + "."
                            + " Take-off time: " + futureTimes[k]);

                    if (index != -1) {
                        foundIndex = true;
                        float fee = CheckMachine.calculateFee(((Passenger) p).getLuggageWeight(), ((Passenger) p).getLuggageSize(), ((Passenger) p).getFlightCode(), map);
                        if (fee > 10100) {
                            deskAreas[i].append(p.getName() + " is dropping off 1 bag of" + ((Passenger) p).getLuggageWeight() + " kg, size of " + ((Passenger) p).getLuggageSize() + ". Your luggage size is over the limit. Please contact the staff!\n");
                            System.out.println(p.getName() + " is dropping off 1 bag of" + ((Passenger) p).getLuggageWeight() + " kg, size of " + ((Passenger) p).getLuggageSize() + ". Your luggage size is over the limit. Please contact the staff!\n");
                        }
                        if (fee < 10200 & fee > 10000) {
                            deskAreas[i].append(p.getName() + " is dropping off 1 bag of" + ((Passenger) p).getLuggageWeight() + " kg, size of " + ((Passenger) p).getLuggageSize() + ". Sorry. Your luggage weight is over the limit. Please contact the staff!\n");
                            System.out.println(p.getName() + " is dropping off 1 bag of" + ((Passenger) p).getLuggageWeight() + " kg, size of " + ((Passenger) p).getLuggageSize() + ". Sorry. Your luggage weight is over the limit. Please contact the staff!\n");
                        }
                        if (fee < 10000) {
                            deskAreas[i].append(p.getName() + " is dropping off 1 bag of" + ((Passenger) p).getLuggageWeight() + " kg, size of " + ((Passenger) p).getLuggageSize() + ". Checks in successfully! You need to pay " + String.format("%.1f", fee) + " dollars for the luggage weight!\n");
                            System.out.println(p.getName() + " is dropping off 1 bag of" + ((Passenger) p).getLuggageWeight() + " kg, size of " + ((Passenger) p).getLuggageSize() + ". Checks in successfully! You need to pay " + String.format("%.1f", fee) + " dollars for the luggage weight!\n");
                        }
                        break;
                    }
                }

                if (!foundIndex) {
                    // Execute specific operation if no matching index is found within the loop
                    deskAreas[i].append("Dear " + p.getName() + ". Sorry. There is not any information about the flight you searched. Maybe you have missed the flight.\n");
                    System.out.println("Dear " + p.getName() + ". Sorry. There is not any information about the flight you searched. Maybe you have missed the flight.\n");
                }
            }
        }

        deskAreas[i].append("\n");
    }



    private void initFlightsPanel(Queue<Observer> fl, String[] futureTimes) {
        // Create the flights panel with a BorderLayout
        flightsPanel = new JPanel(new BorderLayout());
        flightsPanel.setPreferredSize(new Dimension(500, 300));

        // Generate random boolean value to simulate flight status
        Random random = new Random();
        boolean isPUNCTUAL = random.nextBoolean();
        if (isPUNCTUAL) {
            flightsLabel = new JLabel("Flight Information STATUS: PUNCTUAL");
        } else {
            flightsLabel = new JLabel("Flight Information STATUS: DELAYED");
        }

        String labelText = flightsLabel.getText();

        // Create left and right panels to hold flight information and buttons
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(400, 400));
        rightPanel.setPreferredSize(new Dimension(1125, 400));

        // Add left and right panels to the flights panel
        flightsPanel.add(leftPanel, BorderLayout.WEST);
        flightsPanel.add(rightPanel, BorderLayout.EAST);

        try {
            // Read flight data from file and store in map
            map = CheckMachine.readFlight("FlightList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }

        try {
            // Read passenger data from file and store in map2
            map2 = CheckMachine.readPassenger("PassengerList.csv");
        } catch (FormatException e) {
            throw new RuntimeException(e);
        }

        // Create a default list model to hold flight information
        flightlistModel = new DefaultListModel<>();
        flightList = new JList<>(flightlistModel);
        flightsScrollPane = new JScrollPane(flightList);

        int i = 0;
        // Iterate through the flight queue and update the flight list model
        for (Observer observer : fl) {
            if (observer instanceof Flight) {
                for (Passenger m : map2.values()) {
                    if (!m.isChecked()) {
                        if (Objects.equals(m.getFlightCode(), ((Flight) observer).getFlightCode())) {
                            ((Flight) observer).addCheckinNum();
                        }
                    }
                }
                if (labelText.contains("PUNCTUAL")) {
                    flightlistModel.addElement(((Flight) observer).getFlightCode() + " " + ((Flight) observer).getDestination()
                            + " " + ((Flight) observer).getFlightCode() + ": " + ((Flight) observer).getCheckinNum() + " checked in of " + ((Flight) observer).getMaxPassengerNum()
                            + ". Take-off time: " + futureTimes[i]);
                } else if (labelText.contains("DELAYED")) {
                    flightlistModel.addElement(((Flight) observer).getFlightCode() + " " + ((Flight) observer).getDestination()
                            + " " + ((Flight) observer).getFlightCode() + ": " + " Take-off time: Flights are delayed indefinitely.");
                }
            }
            i++;
        }

        // Set preferred size for flights scroll pane
        flightsScrollPane.setPreferredSize(new Dimension(400, 250));
        // Add flight label and scroll pane to the left panel
        leftPanel.add(flightsLabel, BorderLayout.NORTH);
        leftPanel.add(flightsScrollPane, BorderLayout.CENTER);

        // Set preferred size for button and layout for the right panel
        button.setPreferredSize(new Dimension(250, 150));
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        // Add button to the right panel
        rightPanel.add(button, gbc);

        // Initialize a counter to track button clicks
        AtomicInteger clickCount = new AtomicInteger(0);

        // Add action listener to the button to handle button clicks
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int count = clickCount.getAndIncrement();

                switch (count) {
                    case 0:
                        button.setText("x1.5");
                        break;
                    case 1:
                        button.setText("x0.5");
                        break;
                    case 2:
                        clickCount.set(0);
                        button.setText("Default");
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void updateFlightsPanel(Observer tp, Queue<Observer> fl, String[] futureTimes) {
        // Iterate through the flight queue
        for (Observer observer : fl) {
            // Check if the passenger's flight code matches the flight observer's flight code
            if (Objects.equals(((Passenger) tp).getFlightCode(), ((Flight) observer).getFlightCode())) {
                // Iterate through the future times
                for (int i = 0; i < futureTimes.length; i++) {
                    // Find the index of the flight in the flight list model
                    int index = flightlistModel.indexOf(((Flight) observer).getFlightCode() + " " + ((Flight) observer).getDestination()
                            + " " + ((Flight) observer).getFlightCode() +
                            ": " + ((Flight) observer).getCheckinNum() +
                            " checked in of " + ((Flight) observer).getMaxPassengerNum() + "."
                            + " Take-off time: " + futureTimes[i]);

                    // If the flight is found in the flight list model
                    if (index != -1) {
                        // Increment the check-in number for the flight
                        ((Flight) observer).addCheckinNum();
                        // Update the flight information in the flight list model
                        flightlistModel.set(index, ((Flight) observer).getFlightCode() + " " + ((Flight) observer).getDestination()
                                + " " + ((Flight) observer).getFlightCode() + ": " + ((Flight) observer).getCheckinNum() + " checked in of " + ((Flight) observer).getMaxPassengerNum()
                                + "." + " Take-off time: " + futureTimes[i]);
                    }
                }
            }
        }
    }


    public void deleteFlight(Observer tp, Queue<Observer> fl, String[] futureTimes) {
        // Iterate through the flight queue
        for (Observer observer : fl) {
            // Iterate through the future times
            for (int i = 0; i < futureTimes.length; i++) {
                // Find the index of the flight in the flight list model
                int index = flightlistModel.indexOf(((Flight) observer).getFlightCode() + " " + ((Flight) observer).getDestination()
                        + " " + ((Flight) observer).getFlightCode() +
                        ": " + ((Flight) observer).getCheckinNum() +
                        " checked in of " + ((Flight) observer).getMaxPassengerNum() + "."
                        + " Take-off time: " + futureTimes[i]);

                // If the flight is found in the flight list model
                if (index != -1) {
                    // Remove the flight information from the flight list model
                    flightlistModel.set(index, "");
                }
            }
        }
    }




    private void setComponentStyles() {
        queueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));



        flightsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        queueList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        for(int i =0;i<3;i++){
            deskAreas[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            deskLabel[i].setFont(new Font("SansSerif", Font.BOLD, 16));
        }

        //flightArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        queuePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        desksPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        flightsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        deskbuttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public static void main(String[] args) {
        // Create an array of Observable_passengers_line objects
        Observable_passengers_line[] opl = new Observable_passengers_line[1];
        // Create an Observable_flights_line object
        Observable_flights_line ofl = new Observable_flights_line();

        // Initialize each Observable_passengers_line object in the array
        for (int i = 0; i < 1; i++) {
            opl[i] = new Observable_passengers_line();
        }

        // Load passenger queues into each Observable_passengers_line object in the array
        Queue<Observer>[] pl = new Queue[1];
        for (int i = 0; i < 1; i++) {
            pl[i] = opl[i].getinqueue();
        }

        // Get the flight queue from Observable_flights_line
        Queue<Observer> fl = ofl.getinqueue();

        // Create an array to store future times
        String[] futureTimes = new String[fl.size()];

        // Get the current time
        Calendar calendar = Calendar.getInstance();
        // Set the time increment to 7 seconds in milliseconds
        long increment = 7000;

        // Generate future time array
        for (int i = 0; i < futureTimes.length; i++) {
            // Calculate the time for each future moment
            calendar.setTimeInMillis(System.currentTimeMillis() + i * increment);
            // Format the time and store it in the array
            futureTimes[i] = String.format("%04d-%02d-%02d %02d:%02d:%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1, // Month starts from 0, so add 1
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));
        }

        // Launch the GUI application using the SwingUtilities.invokeLater method
        SwingUtilities.invokeLater(() -> new ShowerGUI(pl, fl, futureTimes).setVisible(true));

    }
}
