package edu.ptithcm.utils;

public class ValidationUtil {
    public static boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
}
