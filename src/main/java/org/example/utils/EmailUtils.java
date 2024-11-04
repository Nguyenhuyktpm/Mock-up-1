package org.example.utils;

import java.util.List;

public class EmailUtils {
    public static boolean emailExists(List<String> emails, String email) {
        return emails.stream().anyMatch(email::equals);
    }
}
