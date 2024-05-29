package com.example.asm.util;

import org.springframework.stereotype.Component;

@Component
public class Timestamp {
    public static java.sql.Timestamp timeNow() {
        java.sql.Timestamp timeN = new java.sql.Timestamp(System.currentTimeMillis());
        return timeN;
    }
}
