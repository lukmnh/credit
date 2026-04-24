package org.example.service.impl;

import org.example.factory.VehicleFactory;
import org.example.model.Loan;
import org.example.model.Vehicle;
import org.example.service.WebLoaderService;
import org.example.util.JsonUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebLoaderServiceImpl implements WebLoaderService {
    private static final String API_URL = "https://mocki.io/v1/efcb8213-46fa-45c2-8692-cd91b30c0e19";


    @Override
    public List<Loan> fetchLoanData() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("HTTP Error: " + connection.getResponseCode());
        }

        InputStream responseStream = connection.getInputStream();
        Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
        String json = scanner.hasNext() ? scanner.next() : "";
        List<Loan> loanList = new ArrayList<>();

        if (!json.startsWith("[")) {
            json = "[" + json + "]";
        }
        Pattern pattern = Pattern.compile("\\{(?:[^{}]|\\{[^{}]*\\})*\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);

        while (matcher.find()) {
            String itemJson = matcher.group(0);
            loanList.add(parseJsonToLoan(itemJson));
        }

        return loanList;
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
