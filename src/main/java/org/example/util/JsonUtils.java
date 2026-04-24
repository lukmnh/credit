package org.example.util;

import org.example.factory.VehicleFactory;
import org.example.model.Loan;
import org.example.model.Vehicle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {
    public static String getJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*\"?([^,\"}]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private static double extractNumber(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*([\\d.]+)");
        Matcher m = p.matcher(json);
        if (m.find())
            return Double.parseDouble(m.group(1));
        throw new RuntimeException("Key numerik tidak ditemukan: " + key);
    }
}
