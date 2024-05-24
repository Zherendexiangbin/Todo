package net.onest.time.utils;

public class StringUtil {

    public static boolean isEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isPhone(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }

    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
