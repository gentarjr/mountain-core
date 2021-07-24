package com.mountain.library.helper;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private final static Locale LOCALE = new Locale("in", "ID");
    private final static String defaultDatePattern = "dd/MM/yyyy";
    private final static String defaultDateTimePattern = "dd/MM/yyyy kk:mm";
    private final static String defaultTimePattern = "kk:mm";


    public static String formatDate(LocalDate date, String pattern, Locale locale) {
        return date.format(DateTimeFormatter.ofPattern(pattern, locale));
    }

    public static String formatDate(LocalDate date, String pattern) {
        return formatDate(date, pattern, LOCALE);
    }

    public static String formatDate(LocalDate date) {
        return formatDate(date, defaultDatePattern, LOCALE);
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern, Locale locale) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, locale));
    }

    public static String formatTime(LocalTime time, String pattern, Locale locale) {
        return time.format(DateTimeFormatter.ofPattern(pattern, locale));
    }

    public static String formatTime(LocalTime time, String pattern) {
        return formatTime(time, pattern, LOCALE);
    }

    public static String formatTime(LocalTime time) {
        return formatTime(time, defaultTimePattern, LOCALE);
    }

    public static String formatTimeOrDefault(LocalTime time, String pattern, String defaultStr) {
        String formattedStr = defaultStr;
        try {
            formattedStr = formatTime(time, pattern, LOCALE);
        } catch (Exception e) {
        }
        return formattedStr;
    }

    public static String formatTimeOrNull(LocalTime time, String pattern) {
        return formatTimeOrDefault(time, pattern, null);
    }

    public static String formatTimeOrNull(LocalTime time) {
        return formatTimeOrDefault(time, defaultTimePattern, null);
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return formatDateTime(dateTime, pattern, LOCALE);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, defaultDateTimePattern, LOCALE);
    }

    public static String formatDateOrDefault(LocalDate date, String pattern, String defaultStr) {
        String formattedStr = defaultStr;
        try {
            formattedStr = formatDate(date, pattern, LOCALE);
        } catch (Exception e) {
        }
        return formattedStr;
    }

    public static String formatDateOrNull(LocalDate date, String pattern) {
        return formatDateOrDefault(date, pattern, null);
    }

    public static String formatDateOrNull(LocalDate date) {
        return formatDateOrDefault(date, defaultDatePattern, null);
    }

    public static String formatDateOrEmptyString(LocalDate date, String pattern) {
        return formatDateOrDefault(date, pattern, "");
    }

    public static String formatDateOrEmptyString(LocalDate date) {
        return formatDateOrDefault(date, defaultDatePattern, "");
    }

    public static String formatDateTimeOrDefault(LocalDateTime dateTime, String pattern, String defaultStr) {
        String formattedStr = defaultStr;
        try {
            formattedStr = formatDateTime(dateTime, pattern, LOCALE);
        } catch (Exception e) {
        }
        return formattedStr;
    }

    public static String formatDateTimeOrNull(LocalDateTime date, String pattern) {
        return formatDateTimeOrDefault(date, pattern, null);
    }

    public static String formatDateTimeOrNull(LocalDateTime date) {
        return formatDateTimeOrDefault(date, defaultDateTimePattern, null);
    }

    public static String formatDateTimeOrEmptyString(LocalDateTime date, String pattern) {
        return formatDateTimeOrDefault(date, pattern, "");
    }

    public static String formatDateTimeOrEmptyString(LocalDateTime date) {
        return formatDateTimeOrDefault(date, defaultDateTimePattern, "");
    }

    public static LocalDate parseDate(String str, String pattern, Locale locale)
            throws Exception {

        if (str == null || str.isEmpty()) {
            throw new NullPointerException("tanggal cannot be null.");
        }

        String p = defaultDatePattern;
        if (pattern != null && !pattern.isEmpty()) {
            p = pattern;
        }

        LocalDate date = null;
        try {
            date = LocalDate.parse(
                    str, DateTimeFormatter.ofPattern(p, locale == null ? LOCALE : locale));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return date;
    }

    public static LocalDate parseDate(String str, String pattern) throws Exception {
        return parseDate(str, pattern, null);
    }

    public static LocalDate parseDate(String str) throws Exception {
        return parseDate(str, null, null);
    }

    public static LocalDate parseDateOrDefault(
            String str, String pattern, LocalDate replacement) {

        return parseDateOrDefault(str, pattern, replacement, null);
    }

    public static LocalDate parseDateOrDefault(
            String str, String pattern, LocalDate replacement, Locale locale) {

        try {
            return parseDate(str, pattern, locale);
        } catch (Exception ex) {
            return replacement;
        }
    }

    public static LocalDate parseDateOrNow(String str, String pattern) {
        return parseDateOrDefault(str, pattern, LocalDate.now());
    }

    public static LocalDate parseDateOrNull(String str) {
        return parseDateOrDefault(str, null, null);
    }

    public static LocalDate parseDateOrNull(String str, String pattern) {
        return parseDateOrDefault(str, pattern, null);
    }

    public static LocalDate parseDateOrNull(String str, String pattern, Locale locale) {
        return parseDateOrDefault(str, pattern, null, locale);
    }

    public static LocalDateTime parseDateTime(String str, String pattern, Locale locale)
            throws Exception {

        if (str == null || str.isEmpty()) {
            throw new NullPointerException("tanggal cannot be null.");
        }

        String p = defaultDateTimePattern;
        if (pattern != null && !pattern.isEmpty()) {
            p = pattern;
        }

        LocalDateTime date = null;
        try {
            date = LocalDateTime.parse(
                    str, DateTimeFormatter.ofPattern(p, locale == null ? LOCALE : locale));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return date;
    }

    public static LocalDateTime parseDateTime(String str, String pattern) throws Exception {
        return parseDateTime(str, pattern, null);
    }

    public static LocalDateTime parseDateTime(String str) throws Exception {
        return parseDateTime(str, null, null);
    }

    public static LocalDateTime parseDateTimeOrDefault(
            String str, String pattern, LocalDateTime replacement) {

        return parseDateTimeOrDefault(str, pattern, replacement, null);
    }

    public static LocalDateTime parseDateTimeOrDefault(
            String str, String pattern, LocalDateTime replacement, Locale locale) {

        try {
            return parseDateTime(str, pattern, locale);
        } catch (Exception ex) {
            return replacement;
        }
    }

    public static LocalDateTime parseDateTimeOrNow(String str, String pattern) {
        return parseDateTimeOrDefault(str, pattern, LocalDateTime.now());
    }

    public static LocalDateTime parseDateTimeOrNull(String str, String pattern) {
        return parseDateTimeOrDefault(str, pattern, null);
    }

    public static LocalDateTime parseDateTimeOrNull(String str, String pattern, Locale locale) {
        return parseDateTimeOrDefault(str, pattern, null, locale);
    }

    public static LocalTime parseTime(String str, String pattern, Locale locale)
            throws DateTimeParseException {

        if (str == null || str.isEmpty()) {
            throw new NullPointerException("time cannot be null.");
        }

        String p = defaultTimePattern;
        if (pattern != null && !pattern.isEmpty()) {
            p = pattern;
        }

        LocalTime date = LocalTime.parse(
                str, DateTimeFormatter.ofPattern(p, locale == null ? LOCALE : locale));
        return date;
    }

    public static LocalTime parseTime(String str, String pattern) throws DateTimeParseException {
        return parseTime(str, pattern, null);
    }

    public static LocalTime parseTime(String str) throws DateTimeParseException {
        return parseTime(str, null, null);
    }

    public static LocalTime parseTimeOrDefault(
            String str, String pattern, LocalTime replacement) {

        return parseTimeOrDefault(str, pattern, replacement, null);
    }

    public static LocalTime parseTimeOrDefault(
            String str, String pattern, LocalTime replacement, Locale locale) {

        try {
            return parseTime(str, pattern, locale);
        } catch (Exception ex) {
            return replacement;
        }
    }

    public static LocalTime parseTimeOrNow(String str, String pattern) {
        return parseTimeOrDefault(str, pattern, LocalTime.now());
    }

    public static LocalTime parseTimeOrNull(String str) {
        return parseTimeOrNull(str, null);
    }

    public static LocalTime parseTimeOrNull(String str, String pattern) {
        return parseTimeOrNull(str, pattern, null);
    }

    public static LocalTime parseTimeOrNull(String str, String pattern, Locale locale) {
        return parseTimeOrDefault(str, pattern, null, locale);
    }

    public static String getFormattedCurrentDate(String pattern) {
        return formatDateOrEmptyString(LocalDate.now(), pattern);
    }

    public static String getFormattedCurrentDate() {
        return getFormattedCurrentDate(defaultDatePattern);
    }

    public static String getFormattedCurrentDateTime(String pattern) {
        return formatDateTimeOrEmptyString(LocalDateTime.now(), pattern);
    }

    public static String getFormattedCurrentDateTime() {
        return getFormattedCurrentDateTime(defaultDateTimePattern);
    }

    public static LocalDate ifNullGetDefault(LocalDate testingDate, LocalDate replacement) {
        return testingDate == null ? replacement : testingDate;
    }

    public static LocalDateTime toLocalDateTime(LocalDate tanggal) {
        LocalTime updatedTime = LocalTime.now();
        return updatedTime.atDate(tanggal);
    }

    public static LocalDate getStartDateOfTheMonth(LocalDate month){
        LocalDate startDateOfTheMonth = null;
        try {
            startDateOfTheMonth = month.withDayOfMonth(1);
        } catch (Exception e) {}
        return startDateOfTheMonth;
    }

    public static String getFormattedStartDateOfTheMonth(LocalDate month){
        return formatDateOrEmptyString(getStartDateOfTheMonth(month), defaultDatePattern);
    }

    public static LocalDate getEndDateOfTheMonth(LocalDate month){
        LocalDate endDateOfTheMonth = null;
        try {
            endDateOfTheMonth = month.withDayOfMonth(month.lengthOfMonth());
        } catch (Exception e) {}
        return endDateOfTheMonth;
    }

    public static String getFormattedEndDateOfTheMonth(LocalDate month){
        return formatDateOrEmptyString(getEndDateOfTheMonth(month), defaultDatePattern);
    }

    public static LocalDate getStartDateOfTheYear(LocalDate year){
        LocalDate startDateOfTheYear = null;
        try {
            startDateOfTheYear = year.withDayOfYear(1);
        } catch (Exception e) {}
        return startDateOfTheYear;
    }

    public static String getFormattedStartDateOfTheYear(LocalDate year){
        return formatDateOrEmptyString(getStartDateOfTheYear(year), defaultDatePattern);
    }

    public static LocalDate getEndDateOfTheYear(LocalDate year){
        LocalDate endDateOfTheYear = null;
        try {
            endDateOfTheYear = year.withDayOfYear(year.lengthOfYear());
        } catch (Exception e) {}
        return endDateOfTheYear;
    }

    public static String getFormattedEndDateOfTheYear(LocalDate year){
        return formatDateOrEmptyString(getEndDateOfTheYear(year), defaultDatePattern);
    }

    public static boolean isEqual(LocalDate checkingDate, LocalDate comparation){
        return checkingDate.isEqual(comparation);
    }

    public static boolean isEqual(LocalDateTime checkingDate, LocalDateTime comparation){
        return checkingDate.isEqual(comparation);
    }

    public static boolean isBefore(LocalDate checkingDate, LocalDate comparation){
        return checkingDate.isBefore(comparation);
    }

    public static boolean isBefore(LocalDateTime checkingDate, LocalDateTime comparation){
        return checkingDate.isBefore(comparation);
    }

    public static boolean isAfter(LocalDate checkingDate, LocalDate comparation){
        return checkingDate.isAfter(comparation);
    }

    public static boolean isAfter(LocalDateTime checkingDate, LocalDateTime comparation){
        return checkingDate.isAfter(comparation);
    }

    public static Date toDate(LocalDate localDate){
        return Date.from( localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime){
        return Date.from( localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date date){
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date){
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
