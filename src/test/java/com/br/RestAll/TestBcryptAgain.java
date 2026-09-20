package com.br.RestAll;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBcryptAgain {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = "$2a$10$uLPKWWMeNKUz69wFw2VLA.NTdTIUUw2LURbxI4gOwXcXF0boEtMay";
        System.out.println("Matches admin123: " + encoder.matches("admin123", hash));
        System.out.println("Matches admin: " + encoder.matches("admin", hash));
        System.out.println("New hash: " + encoder.encode("admin123"));
    }
}
