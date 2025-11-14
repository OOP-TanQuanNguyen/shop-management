package edu.ptithcm.utils;

import org.mindrot.jbcrypt.BCrypt;

public class CryptoUtil {

    private static final int WORKLOAD = 12;

    private CryptoUtil() {}

    public static String hash(String plainPassword) {
        if (plainPassword == null) return null;
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORKLOAD));
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
