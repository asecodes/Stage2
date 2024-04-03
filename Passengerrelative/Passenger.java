package Passengerrelative;

import Observer_model.Observer;

public class Passenger implements Observer {
    private String referenceCode;
    private String name;
    private String flightCode;
    private boolean checked;
    private String luggageSize;
    private float luggageWeight;



    private int priority;


    public Passenger(String referenceCode, String name, String flightCode, boolean checked, String luggageSize, float luggageWeight,int priority) {
        this.referenceCode = referenceCode;
        this.name = name;
        this.flightCode = flightCode;
        this.checked = checked;
        this.luggageSize = luggageSize;
        this.luggageWeight = luggageWeight;
        this.priority = priority;
    }


    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getChecked(){
        if(this.checked){
            return "true";
        }
        else{
            return "false";
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getLuggageSize() {
        return luggageSize;
    }

    public void setLuggageSize(String luggageSize) {
        this.luggageSize = luggageSize;
    }

    public void setLuggageWeight(float luggageWeight) {
        this.luggageWeight = luggageWeight;
    }

    public float getLuggageWeight() {
        return luggageWeight;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "referenceCode='" + referenceCode + '\'' +
                ", name='" + name + '\'' +
                ", flightCode='" + flightCode + '\'' +
                //", checked=" + checked +
                ", luggageSize=" + luggageSize +
                ", luggageWeight=" + luggageWeight +
                ", priority=" + priority +
                '}';
    }

    public void update(String message) {

        System.out.println("Passenger "+referenceCode+"has received the message: "+ message +"\n");
    }
}
