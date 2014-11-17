package edu.ucla.fusa.android.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorEmails {

    private static final String PATRON_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static Matcher matcher;
    private static Pattern pattern;

    public static boolean validarEmail(String paramString) {
        pattern = Pattern.compile(PATRON_EMAIL);
        matcher = pattern.matcher(paramString);
        return matcher.matches();
    }
}