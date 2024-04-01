package com.formssafe.global.util;

import java.time.LocalDateTime;
import java.util.Random;

public class CommonUtil {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NICKNAME_LENGTH = 12;
    public static String generateRandomNickname(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("user-");

        for(int i=0; i<NICKNAME_LENGTH; i++){
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
