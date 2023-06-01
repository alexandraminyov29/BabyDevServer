package com.babydev.app.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FormatUtil {
	
	// used for jobs
	public static String formatPostDateToString(LocalDate date) {
    	LocalDate currentDate = LocalDate.now();
        long daysDiff = ChronoUnit.DAYS.between(date, currentDate);

        if (daysDiff < 1) {
            long minutesDifference = ChronoUnit.MINUTES.between(date.atStartOfDay(), currentDate.atStartOfDay());
            if (minutesDifference == 0) {
            	return "Just now";
            } else if (minutesDifference == 1) {
            	return "One minute ago";
            }
            return minutesDifference + " minutes ago";
        } else if (daysDiff < 7) {
            long hoursDiff = ChronoUnit.HOURS.between(date.atStartOfDay(), currentDate.atStartOfDay());
            if (hoursDiff == 1) {
            	return "One hour ago";
            }
            return hoursDiff + " hours ago";
        } else if (daysDiff <= 7) {
        	if (daysDiff == 1) {
        		return "Yesterday";
        	}
            return daysDiff + " days ago";
        } else {
            return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
    }
}
