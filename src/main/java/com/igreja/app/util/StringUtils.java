package com.igreja.app.util;

import java.text.Normalizer;

public class StringUtils {

    public static String normalize(String s) {
        if (s == null) return "";

        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }
}