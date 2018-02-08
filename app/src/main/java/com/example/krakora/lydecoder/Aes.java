package com.example.krakora.lydecoder;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by KRAKORA on 08.02.2018.
 */

public class Aes {

    private static final String ENCRYPTION_KEY = "qwertyuiopasdfghjklzxcvbnmqwerty";
    private static final String ENCRYPTION_IV = "qbmocwtttkttpqvv";

    public static String decrypt(String src, String password) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            // Decode input
            byte[] array = Base64.decode(src, android.util.Base64.NO_WRAP);
            // Read first 16 bytes (IV data)
            byte[] ivData = Arrays.copyOfRange(array, 0, 16);
            // Read last 16 bytes (encrypted text)
            byte[] encrypted = Arrays.copyOfRange(array, 16, array.length);
            // Init the cipher with decrypt mode, key, and IV bytes array (no more hardcoded)
            cipher.init(Cipher.DECRYPT_MODE, makeKey(password), new IvParameterSpec(ivData));
            // Decrypt same old way
            decrypted = new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted.trim();
    }

    private static Key makeKey(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] key = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
