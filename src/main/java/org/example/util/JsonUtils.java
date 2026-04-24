package org.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {
    public static String extractString(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher m = p.matcher(json);
        if (m.find())
            return m.group(1);
        throw new RuntimeException("Key tidak ditemukan: " + key);
    }

    public static int extractInt(String json, String key) {
        return (int) extractNumber(json, key);
    }

    public static long extractLong(String json, String key) {
        return (long) extractNumber(json, key);
    }

    private static double extractNumber(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*([\\d.]+)");
        Matcher m = p.matcher(json);
        if (m.find())
            return Double.parseDouble(m.group(1));
        throw new RuntimeException("Key numerik tidak ditemukan: " + key);
    }
}
