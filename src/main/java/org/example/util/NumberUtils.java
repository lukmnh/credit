package org.example.util;

public class NumberUtils {
    public static String formatRate(double rate) {
        if (rate == Math.floor(rate)) {
            return String.valueOf((int) rate);
        }
        return String.format("%.1f", rate).replace(".", ",");
    }
    
    public static int parseIntSafe(String input, String fieldName) {
        try {
            return Integer.parseInt(input.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " harus berupa angka.");
        }
    }

    public static long parseLongSafe(String input, String fieldName) {
        try {
            return Long.parseLong(input.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " harus berupa angka.");
        }
    }
}
