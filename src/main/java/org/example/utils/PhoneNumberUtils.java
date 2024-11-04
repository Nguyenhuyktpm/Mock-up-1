package org.example.utils;

import java.util.List;

public class PhoneNumberUtils {
    public static boolean phoneExisted(List<String> phoneNumbers, String phoneNumber) {
        return phoneNumbers.stream().anyMatch(element -> element.equals(phoneNumber));
    }

}
