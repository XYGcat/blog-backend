package com.xc.blogbackend.utils;

import java.security.SecureRandom;

public class RandomUsernameGenerator {
    private static final String PREFIX = "星";

    private static final int LENGTH = 6;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static SecureRandom random = new SecureRandom();

    public static String generateRandomUsername() {
        StringBuilder username = new StringBuilder(PREFIX);
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            username.append(randomChar);
        }
        return username.toString();
    }
}
