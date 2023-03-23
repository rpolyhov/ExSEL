package org.exsel.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {
    private Date date;

    public static String getCurrent(String toFormat) {
        return getCurrentDate().format(DateTimeFormatter.ofPattern(toFormat)) ;
    }
    public static String getCurrentDateTime(String toFormat) {
        return getCurrentDateTime().format(DateTimeFormatter.ofPattern(toFormat)) ;
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }


    public static String addDays(LocalDate date,long days,String outFormat) {
        return date.plusDays(days).format(DateTimeFormatter.ofPattern(outFormat)) ;
    }
    public static LocalDate getDateFromFormat(String date, String fromFormat) {
        return LocalDate.parse(date,DateTimeFormatter.ofPattern(fromFormat));
    }
    public static LocalDateTime getDateTimeFromFormat(String dateTime,String fromFormat) {
        return LocalDateTime.parse(dateTime,DateTimeFormatter.ofPattern(fromFormat));
    }

    public static String changeDateFormat(String date, String fromFormat, String toFormat) {
        return getDateFromFormat(date,fromFormat).format(DateTimeFormatter.ofPattern(toFormat)) ;
    }

    public String addDaysFromCurrentDate(long days,String outFormat) {
        return getCurrentDate().plusDays(days).format(DateTimeFormatter.ofPattern(outFormat)) ;
    }

}
