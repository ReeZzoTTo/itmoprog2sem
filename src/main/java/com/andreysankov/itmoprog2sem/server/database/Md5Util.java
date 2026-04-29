package com.andreysankov.itmoprog2sem.server.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5Util {
    private Md5Util() {}

    public static String hash(String message) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();

            for (byte b : bytes) {
                result.append(String.format("%02x", b));
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Алгоритм MD5 не доступен: ", e);
        }
    }
}
