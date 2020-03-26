package de.chriis.vatlog.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormatter {

    public static String getFormattedDate(long millis) {
        Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC-4"));

        return formatter.format(date);
    }

    public static String formatUntil(long millis) {
        if(millis <= 0) {
            return "Just Now";
        }

        int minutes = 0;
        int hours = 0;
        int days = 0;

        while(millis >= 60000) {
            millis -= 60000;
            minutes ++;
        }

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        while(hours >= 24) {
            hours -= 24;
            days ++;
        }

        return (days > 0 ? days + "d" : "") + (days > 0 && hours > 0 ? " " : "") + (hours > 0 ? hours + "h" : "") + (hours > 0 && minutes > 0 ? " " : "") + (minutes > 0 ? minutes + "m" : "");
    }

}
