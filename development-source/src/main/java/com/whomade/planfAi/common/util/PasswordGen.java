package com.whomade.planfAi.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGen {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "1234";
        String hash = encoder.encode(raw);
        System.out.println("Raw: " + raw);
        System.out.println("Hash: " + hash);
        System.out.println("Valid: " + encoder.matches(raw, hash));
    }
}
