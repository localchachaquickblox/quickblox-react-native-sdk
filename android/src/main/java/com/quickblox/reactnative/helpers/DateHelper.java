package com.quickblox.reactnative.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    private DateHelper() {
        //empty
    }

    public static String convertDateToISO(Date date) {
        //Convert Date to ISO 8601
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String convertedDate = dateFormat.format(date);
        return convertedDate;
    }
}