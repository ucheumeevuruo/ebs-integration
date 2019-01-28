/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.helper;

public class DataConverter {
    private static Integer number;

    public static Integer toInt(String value) {
        try {
            number = Integer.parseInt(value);
        }
        catch (NumberFormatException ex) {
            number = 0;
        }
        return number;
    }

    public static Float toFloat(String value) {
        Float number;
        try {
            number = Float.valueOf(Float.parseFloat(value));
        }
        catch (NumberFormatException ex) {
            number = Float.valueOf(Float.parseFloat("0.00"));
        }
        return number;
    }

    public static String nullToString(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }
}

