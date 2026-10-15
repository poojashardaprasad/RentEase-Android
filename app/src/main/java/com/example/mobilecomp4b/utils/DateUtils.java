package com.example.mobilecomp4b.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    private static final SimpleDateFormat DISPLAY_TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.US);

    public static String formatDateForDisplay(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return "";
        try {
            Date date = DATE_FORMAT.parse(dateStr);
            return date != null ? DISPLAY_FORMAT.format(date) : dateStr;
        } catch (Exception e) {
            return dateStr;
        }
    }

    public static String formatTimeForDisplay(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) return "";
        try {
            Date time = TIME_FORMAT.parse(timeStr);
            return time != null ? DISPLAY_TIME_FORMAT.format(time) : timeStr;
        } catch (Exception e) {
            return timeStr;
        }
    }

    public static String formatTimestamp(long timestamp) {
        return DISPLAY_FORMAT.format(new Date(timestamp));
    }

    public static String getTodayString() {
        return DATE_FORMAT.format(new Date());
    }

    public static boolean isPastDate(int year, int month, int dayOfMonth) {
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(year, month, dayOfMonth, 0, 0, 0);
        selectedCalendar.set(Calendar.MILLISECOND, 0);

        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        return selectedCalendar.before(todayCalendar);
    }
}
