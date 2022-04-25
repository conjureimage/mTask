package com.conjureimage.mtask.service.utils;

import java.security.SecureRandom;

public class StringUtils {
    private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public static String transpose(String toTranspose) {
        StringBuilder transposed = new StringBuilder();
        for (String s:
                toTranspose.split("")) {
                transposed.append(s);
        }
        return transposed.toString();
    }

}
