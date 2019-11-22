package com.moratuwa.events.utils;

import java.security.SecureRandom;

public class Code {

    private static final String DIGITS                  = "0123456789";
    private static final SecureRandom RANDOM        = new SecureRandom();

    public static String getVerificationCode(int size){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }


}
