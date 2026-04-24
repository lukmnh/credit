package org.example.util;

import org.example.factory.VehicleFactory;
import org.example.model.Loan;
import org.example.model.Vehicle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<Loan> parseFile(String filePath) throws IOException {
        List<Loan> loans = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                try {
                    loans.add(parseLine(line));
                } catch (Exception e) {
                    System.err.println("Baris " + lineNumber +
                            " dilewati: " + e.getMessage());
                }
            }
        }
        return loans;
    }

    public static Loan parseLine(String line) {
        String delimiter = line.contains(",") ? "," : ";";
        String[] parts = line.split(delimiter);

        String type = parts[0].trim();
        String condition = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        long totalLoan = Long.parseLong(parts[3].trim());
        int tenure = Integer.parseInt(parts[4].trim());
        long dp = Long.parseLong(parts[5].trim());

        Vehicle vehicle = VehicleFactory.createVehicle(type, condition, year);
        return new Loan(vehicle, totalLoan, tenure, dp);
    }
}
