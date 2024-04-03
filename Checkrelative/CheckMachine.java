package Checkrelative;

import Exceptionrelative.FormatException;
import Flightrelative.Flight;
import Passengerrelative.Passenger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.HashMap;

public class CheckMachine {
    String scannedReferenceCode;
    String scannedPassengerName;
    String scannedFlightCode;
    String scannedLuggageSize;
    float scannedLuggageWeight;

    public CheckMachine(String scannedReferenceCode, String scannedPassengerName, String scannedFlightCode, String scannedLuggageSize, float scannedLuggageWeight) {
        this.scannedReferenceCode = scannedReferenceCode;
        this.scannedPassengerName = scannedPassengerName;
        this.scannedFlightCode = scannedFlightCode;
        this.scannedLuggageSize = scannedLuggageSize;
        this.scannedLuggageWeight = scannedLuggageWeight;
    }

    public static void checkInformation(String scannedReferenceCode, String scannedPassengerName, HashMap<String, Passenger> list, HashMap<String, Flight> list2) throws FormatException {
        Passenger p = list.get(scannedReferenceCode);
        try {
            if (scannedReferenceCode.length() != 9) {
                throw new FormatException("The length of the reference code does not match", "000");
            } else {
                String firstFourChars = scannedReferenceCode.substring(0, 4);
                boolean allLetters = true;

                for (int i = 0; i < firstFourChars.length(); i++) {
                    char ch = firstFourChars.charAt(i);
                    if (!Character.isLetter(ch)) {
                        allLetters = false;
                        break; // No need to continue checking once a non-letter character is found
                    }
                }
                if (!allLetters) {
                    throw new FormatException("The letter verification code of the reference code is incorrect", "001");
                } else {
                    String lastFiveChars = scannedReferenceCode.substring(scannedReferenceCode.length() - 5);
                    boolean allNumbers = true;

                    for (int i = 0; i < lastFiveChars.length(); i++) {
                        char ch = lastFiveChars.charAt(i);
                        if (!Character.isDigit(ch)) {
                            allNumbers = false;
                            break;
                        }
                    }
                    if (!allNumbers) {
                        throw new FormatException("The numeric verification code of the reference code is incorrect", "002");
                    }
                }
            }
        } catch (FormatException e) {
            e.logException(); // Call custom method to log exception information
            System.err.println("Exception handled, error code: " + e.getErrorCode()); // Directly access the additional property
        }

        try {
            Passenger a = list.get(scannedReferenceCode);
            if (a == null) {
                throw new FormatException("Reference code could not be found", "003");
            }
        } catch (FormatException e) {
            e.logException(); // Call custom method to log exception information
            System.err.println("Exception handled, error code: " + e.getErrorCode()); // Directly access the additional property
        }

        if (!Objects.equals(p.getName(), scannedPassengerName)) {
            System.out.println("Sorry. Your name cannot be found. Please retype the name!\n");
        } else {
            if (!p.isChecked()) {
                Flight f = list2.get(p.getFlightCode());
                if (CheckMachine.calculateFee(p.getLuggageWeight(), p.getLuggageSize(), p.getFlightCode(), list2) > 10100) {

                }
                if (CheckMachine.calculateFee(p.getLuggageWeight(), p.getLuggageSize(), p.getFlightCode(), list2) < 10200 && CheckMachine.calculateFee(p.getLuggageWeight(), p.getLuggageSize(), p.getFlightCode(), list2) > 10000) {

                }
                if(CheckMachine.calculateFee(p.getLuggageWeight(), p.getLuggageSize(), p.getFlightCode(), list2) < 10200 && CheckMachine.calculateFee(p.getLuggageWeight(), p.getLuggageSize(), p.getFlightCode(), list2) > 10300){

                }
                else {

                }
            } else {
                System.out.println(p.toString()+"You have already checked in.\n");
            }
        }

        for (Passenger m : list.values()) {
            for(Flight f:list2.values()){
                if(Objects.equals(m.getFlightCode(),f.getFlightCode())){
                    if(m.isChecked()) {
                        f.addCheckinNum();
                        f.addLuggageNum();
                        f.addTotalWeight(m.getLuggageWeight());
                        if(CheckMachine.calculateFee(m.getLuggageWeight(), m.getLuggageSize(), m.getFlightCode(), list2)>10000){
                            int a =0;
                            f.addTotalFee(a);
                        }else {
                            f.addTotalFee(CheckMachine.calculateFee(m.getLuggageWeight(), m.getLuggageSize(), m.getFlightCode(), list2));
                        }
                    }
                }
            }
        }
    }

    // Calculate luggage fee based on weight, size, and flight code
    public static float calculateFee(float luggageWeight, String luggageSize, String flightCode, HashMap<String, Flight> list) {
        float fee = 0;
        float freeLuggageWeight;
        float maxLuggageWeight;
        String maxLuggageSize;

        Flight f = list.get(flightCode);
        if(f!=null){
            freeLuggageWeight = f.getFreeLuggageWeight();
            maxLuggageWeight = f.getMaxLuggageWeight();
            maxLuggageSize = f.getMaxLuggageSize();

            String[] values1 = luggageSize.split("\\s*\\*\\s*");
            String[] values2 = maxLuggageSize.split("\\s*\\*\\s*");
            for (int i = 0; i < 3; i++) {

                if (Integer.parseInt(values1[i]) > Integer.parseInt(values2[i])) {
                    fee = 10000;
                    return fee + 200;
                } else {
                    fee = 0;

                }

            }
            if (luggageWeight < freeLuggageWeight) {
                return fee;
            } else if (luggageWeight > maxLuggageWeight) {
                fee = 10000;
                return fee + 100;
            } else {
                fee = (luggageWeight - freeLuggageWeight) * 10;
                return fee;
            }
        }else{
            fee = 100300;
            return fee;
        }
    }

    // Read passenger information from file and return a hashmap
    public static HashMap<String,Passenger> readPassenger(String passengerList)  throws FormatException {
        try(BufferedReader bf = new BufferedReader(new FileReader(passengerList))) {
            String line;
            boolean isFirstLine = true;
            HashMap<String,Passenger> pl = new HashMap<>();
            while((line = bf.readLine())!=null){
                if(isFirstLine){
                    isFirstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                if (values.length == 0) {
                    throw new FormatException("FileERROR:Empty line encountered", "102");
                }
                boolean v3;
                if (!values[3].equalsIgnoreCase("true") && !values[3].equalsIgnoreCase("false")) {
                    throw new FormatException("Invalid format for boolean value: " + values[3], "106");
                }
                v3 = Boolean.parseBoolean(values[3]);

                String v4 = values[4];

                float v5;
                if (!values[5].matches("^-?\\d+(\\.\\d+)?")) {
                    throw new FormatException("Invalid format for float value: " + values[5], "107");
                }
                v5 = Float.parseFloat(values[5]);
                int v6 = Integer.parseInt(values[6]);
                Passenger p = new Passenger(values[0],values[1],values[2],v3,v4,v5,v6);
                pl.put(values[0],p);
            }
            return pl;

        } catch (FileNotFoundException e) {
            throw new FormatException("File not found", "100");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Read flight information from file and return a hashmap
    public static HashMap<String,Flight> readFlight(String flightList) throws FormatException {
        try(BufferedReader bf = new BufferedReader(new FileReader(flightList))) {
            String line;
            boolean isFirstLine = true;
            HashMap<String,Flight> fl = new HashMap<>();
            while((line = bf.readLine())!=null){
                if(isFirstLine){
                    isFirstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                if (values.length == 0) {
                    throw new FormatException("FileERROR:Empty line encountered", "102");
                }
                int v3;
                if (!values[3].matches("-?\\d+")) {
                    throw new FormatException("Invalid format for integer value: " + values[3], "103");
                }
                v3 = Integer.parseInt(values[3]);

                float v4;
                if (!values[4].matches("^-?\\d+(\\.\\d+)?")) {
                    throw new FormatException("Invalid format for float value: " + values[4], "104");
                }
                v4 = Float.parseFloat(values[4]);

                float v5;
                if (!values[5].matches("^-?\\d+(\\.\\d+)?")) {
                    throw new FormatException("Invalid format for float value: " + values[5], "105");
                }
                v5 = Float.parseFloat(values[5]);

                String v6 = values[6];

                Flight f = new Flight(values[0],values[1],values[2],v3,v4,v5,v6);
                fl.put(values[2],f);
            }
            return fl;

        } catch (FileNotFoundException e) {
            throw new FormatException("File not found", "100");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
