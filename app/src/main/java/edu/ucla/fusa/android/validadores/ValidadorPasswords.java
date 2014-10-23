package edu.ucla.fusa.android.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by juanlabrador on 18/10/14.
 */
public class ValidadorPasswords {

    private Pattern pattern;
    private Matcher matcher;

    private static final String PATRON_PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

    public boolean validarPassword(String password) {
        pattern = Pattern.compile(PATRON_PASSWORD);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
