package com.example.socceritemsstore.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {
    
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }
    
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_FORMATTER);
    }
    
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMATTER);
    }
    
    public static LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.truncatedTo(ChronoUnit.DAYS);
    }
    
    public static LocalDateTime getEndOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
    }
    
    public static LocalDateTime getStartOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    }
    
    public static LocalDateTime getEndOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.withDayOfMonth(1).plusMonths(1).minusNanos(1);
    }
    
    public static long getDaysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }
    
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return dateTime.toLocalDate().equals(now.toLocalDate());
    }
    
    public static boolean isThisMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return dateTime.getYear() == now.getYear() && 
               dateTime.getMonth() == now.getMonth();
    }
}
