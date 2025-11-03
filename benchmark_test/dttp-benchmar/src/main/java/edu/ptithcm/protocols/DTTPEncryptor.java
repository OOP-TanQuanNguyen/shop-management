package edu.ptithcm.protocols;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DTTPEncryptor {
    public static String encrypt(String plain, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plain.getBytes()));
        } catch (Exception e) {
            return plain;
        }
    }

    public static String decrypt(String enc, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, spec);
            byte[] dec = Base64.getDecoder().decode(enc);
            return new String(cipher.doFinal(dec));
        } catch (Exception e) {
            return enc;
        }
    }
}
