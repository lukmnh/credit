package org.example.factory;

import org.example.model.Vehicle;
import org.example.model.VehicleType;

public class VehicleFactory {
    public static Vehicle createVehicle(String inputType, String condition, Integer year) {
        if(inputType == null || inputType.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis kendaraan tidak boleh kosong");
        }

        String type = inputType.trim().toLowerCase();

        switch(type) {
            case "mobil" -> {
                return new Vehicle(VehicleType.MOBIL, condition, year);
            }
            case "motor" -> {
                return new Vehicle(VehicleType.MOTOR, condition, year);
            }
            default -> {
                throw new IllegalArgumentException("Jenis kendaraan tidak valid: '" + inputType + "'. Gunakan 'Mobil' atau 'Motor'.");
            }
        }
    }
}
