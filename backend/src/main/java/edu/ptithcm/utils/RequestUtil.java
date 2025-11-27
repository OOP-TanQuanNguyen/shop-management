package edu.ptithcm.utils;

import java.math.BigDecimal;
import java.sql.Date;

public class RequestUtil {

    public static Integer toInt(Object value) {
        if (value == null) return null;

        if (value instanceof Number n) {
            return n.intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int toInt(Object value, int defaultValue) {
        Integer i = toInt(value);
        return i != null ? i : defaultValue;
    }

    public static String toStr(Object value) {
        return value != null ? value.toString() : null;
    }

    public static Double toDouble(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Double.valueOf(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean toBool(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Boolean.valueOf(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean toBool(Object value, boolean defaultValue) {
        Boolean b = toBool(value);
        return b != null ? b : defaultValue;
    }

    public static Date toDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        try {
            return Date.valueOf(value.toString()); // yyyy-MM-dd
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
