package com.feedbackmanagement.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatUtility {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }

    public String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public String formatIso(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(ISO_PATTERN));
    }

    public LocalDateTime parse(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }

    public LocalDateTime parse(String dateTimeString, String pattern) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
    }
}