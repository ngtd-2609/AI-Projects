package util;

public class Validator {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return true;
        return phone.matches("^0\\d{9}$");
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return true;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }

    public static boolean isPositiveNumber(String s) {
        try { return Integer.parseInt(s.trim()) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static boolean isPositiveDouble(String s) {
        try { return Double.parseDouble(s.trim()) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static boolean isValidDate(String date) {
        return date != null && date.matches("^\\d{2}/\\d{2}/\\d{4}$");
    }

    public static boolean isValidTime(String time) {
        return time != null && time.matches("^\\d{2}:\\d{2}$");
    }

    // "2026-05-14" -> "14/05/2026"
    public static String toDisplayDate(String dbDate) {
        if (dbDate == null || dbDate.length() != 10) return dbDate;
        String[] parts = dbDate.split("-");
        if (parts.length != 3) return dbDate;
        return parts[2] + "/" + parts[1] + "/" + parts[0];
    }

    // "14/05/2026" -> "2026-05-14"
    public static String toDatabaseDate(String displayDate) {
        if (displayDate == null) return displayDate;
        String[] parts = displayDate.split("/");
        if (parts.length != 3) return displayDate;
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    public static String todayDisplay() {
        return toDisplayDate(java.time.LocalDate.now().toString());
    }

    public static String firstDayOfMonthDisplay() {
        return toDisplayDate(java.time.LocalDate.now().withDayOfMonth(1).toString());
    }
}
