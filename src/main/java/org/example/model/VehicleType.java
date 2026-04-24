package org.example.model;

public enum VehicleType {
    MOBIL("Mobil", 8.0),
    MOTOR("Motor", 9.0);

    private final String displayVehicleName;
    private final Double baseInterestRate;

    VehicleType(String displayVehicleName, Double baseInterestRate) {
        this.displayVehicleName = displayVehicleName;
        this.baseInterestRate = baseInterestRate;
    }

    public String getDisplayVehicleName(){
        return displayVehicleName;
    }

    public Double getBaseInterestRate(){
        return baseInterestRate;
    }
}
