package com.formssafe.global.util;

import java.util.Random;

public final class CommonUtil {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NICKNAME_LENGTH = 12;
    private static final Random RANDOM = new Random();

    private CommonUtil() {
    }

    public static String generateRandomNickname(){
        StringBuilder sb = new StringBuilder();
        sb.append("user-");

        for(int i=0; i<NICKNAME_LENGTH; i++){
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
