/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.helper;

public class DataTypeCheck {
    private static Boolean booleanOutput = false;

    public static Boolean isString(String data) {
        if (!DataTypeCheck.isStringInt(data) && data instanceof String) {
            booleanOutput = true;
        }
        return booleanOutput;
    }

    public static boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isDoubleInt(double d) {
        double TOLERANCE = 1.0E-5;
        booleanOutput = Math.abs(Math.floor(d) - d) < TOLERANCE;
        return booleanOutput;
    }

    public static boolean isObjectInteger(Object o) {
        booleanOutput = o instanceof Integer;
        return booleanOutput;
    }
}

