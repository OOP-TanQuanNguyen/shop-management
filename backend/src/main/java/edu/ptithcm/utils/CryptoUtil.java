package edu.ptithcm.utils;

import java.security.MessageDigest;

public class CryptoUtil {
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verifyPassword(String plain, String hash) {
        return md5(plain).equals(hash);
    }
}
