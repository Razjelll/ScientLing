package com.dyszlewskiR.edu.scientling.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Razjelll on 19.04.2017.
 */

public class MD5 {
    private static final String MD5_CODE = "MD5";

    public static final String getMD5(final String value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(MD5_CODE);
        digest.update(value.getBytes());
        byte[] messageDigest = digest.digest();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xff & messageDigest[i]);
            if (hex.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hex);
        }

        /*String md5Value = new String(messageDigest);
        return md5Value;*/
        return stringBuilder.toString();
    }
}
