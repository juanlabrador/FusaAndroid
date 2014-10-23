package edu.ucla.fusa.android.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by juanlabrador on 18/10/14.
 */
public class ValidadorEmails {

    private static Pattern pattern;
    private static Matcher matcher;

    private static final String PATRON_EMAIL = "^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\"\n" +
            "\"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$";

    public static boolean validarEmail(String email) {
        pattern = Pattern.compile(PATRON_EMAIL);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
