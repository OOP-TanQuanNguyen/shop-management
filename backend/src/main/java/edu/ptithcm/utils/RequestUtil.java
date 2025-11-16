package edu.ptithcm.utils;

import java.math.BigDecimal;
import java.sql.Date;

public class RequestUtil {
    public static Integer toInt(Object value) {
        if (value == null) return null;
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static String toStr(Object value) {
        return value != null ? value.toString() : null;
    }

    public static Double toDouble(Object value) {
        if (value == null) return null;
        try {
            return Double.valueOf(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean toBool(Object value) {
        if (value == null) return null;
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
        if (value == null) return null;
        if (value instanceof Date) return (Date) value;
        try {
            return Date.valueOf(value.toString()); // yyyy-MM-dd
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) return null;
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
