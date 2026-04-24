package org.example.service.impl;

import org.example.factory.VehicleFactory;
import org.example.model.Loan;
import org.example.model.Vehicle;
import org.example.service.WebLoaderService;
import org.example.util.JsonUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WebLoaderServiceImpl implements WebLoaderService {
    private static final String API_URL = "https://mocki.io/v1/59da4f38-8cd8-4205-9405-6472ae19da0c";

    @Override
    public Loan fetchLoanData() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("HTTP Error: " + connection.getResponseCode());
        }

        InputStream responseStream = connection.getInputStream();
        Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
        String json = scanner.hasNext() ? scanner.next() : "";

        return parseJsonToLoan(json);
    }

    @Override
    public boolean isApiReady() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private Loan parseJsonToLoan(String json) {
        String type = JsonUtils.getJsonValue(json, "vehicleType");
        String condition = JsonUtils.getJsonValue(json, "vehicleCondition");
        int year = Integer.parseInt(JsonUtils.getJsonValue(json, "vehicleYear"));
        long totalLoan = Long.parseLong(JsonUtils.getJsonValue(json, "totalLoanAmount"));
        int tenure = Integer.parseInt(JsonUtils.getJsonValue(json, "loanTenure"));
        long dp = Long.parseLong(JsonUtils.getJsonValue(json, "downPayment"));

        Vehicle vehicle = VehicleFactory.createVehicle(type, condition, year);
        return new Loan(vehicle, totalLoan, tenure, dp);
    }
}
