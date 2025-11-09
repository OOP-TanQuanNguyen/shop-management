package edu.ptithcm.protocols;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DTTPEncryptor {
    private static final String ALGO = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;   // bytes
    private static final int TAG_LENGTH = 128; // bits = 16 bytes

    private DTTPEncryptor() {}

    public static String encrypt(String plain, String key) {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(ALGO);

            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, spec, gcmSpec);
            byte[] cipherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            byte[] full = ByteBuffer.allocate(iv.length + cipherText.length)
                    .put(iv)
                    .put(cipherText)
                    .array();

            return Base64.getEncoder().encodeToString(full);
        } catch (Exception e) {
            System.err.println("Encrypt error: " + e.getMessage());
            return null;
        }
    }

    public static String decrypt(String enc, String key) {
        try {
            if (enc == null) return null;

            enc = enc.trim()
                     .replaceAll("\\\\n", "")
                     .replaceAll("\\\\r", "")
                     .replaceAll("[\\n\\r\\t\\f ]", "")
                     .replaceAll("[^A-Za-z0-9+/=]", "");

            while (enc.length() % 4 != 0) enc += "="; // auto-fix missing padding

            byte[] decoded = Base64.getDecoder().decode(enc);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            int remaining = buffer.remaining();
            if (remaining < 16) throw new IllegalArgumentException("Invalid ciphertext length: " + remaining);

            byte[] cipherText = new byte[remaining - 16];
            buffer.get(cipherText);
            byte[] tag = new byte[16];
            buffer.get(tag);

            byte[] cipherData = new byte[cipherText.length + tag.length];
            System.arraycopy(cipherText, 0, cipherData, 0, cipherText.length);
            System.arraycopy(tag, 0, cipherData, cipherText.length, tag.length);

            SecretKeySpec spec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(ALGO);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, spec, gcmSpec);

            byte[] plain = cipher.doFinal(cipherData);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Decrypt error: " + e.getMessage());
            return null;
        }
    }
}
