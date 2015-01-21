package edu.ucla.fusa.android.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorPasswords {

    private static final String PATRON_PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static Matcher matcher;
    private static Pattern pattern;

    public static boolean validarPassword(String paramString) {
        pattern = Pattern.compile(PATRON_PASSWORD);
        matcher = pattern.matcher(paramString);
        return matcher.matches();
    }
}