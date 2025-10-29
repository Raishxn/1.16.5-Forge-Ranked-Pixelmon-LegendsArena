package com.raishxn.legendsarena.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static long parseDuration(String durationStr) {
        try {
            char unit = durationStr.charAt(durationStr.length() - 1);
            long value = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));

            switch (Character.toLowerCase(unit)) {
                case 's':
                    return TimeUnit.SECONDS.toMillis(value);
                case 'm':
                    return TimeUnit.MINUTES.toMillis(value);
                case 'h':
                    return TimeUnit.HOURS.toMillis(value);
                case 'd':
                    return TimeUnit.DAYS.toMillis(value);
                default:
                    return -1L; // Formato inválido
            }
        } catch (Exception e) {
            return -1L; // Erro de parsing
        }
    }
}