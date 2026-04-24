package org.example.model;

public class Vehicle {
    private VehicleType vehicleType;
    private String condition;
    private int year;

    public Vehicle(VehicleType vehicleType, String condition, int year){
        this.vehicleType = vehicleType;
        this.condition = condition;
        this.year = year;
    }

    public String getCondition() {
        return condition;
    }

    public int getYear() {
        return year;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
