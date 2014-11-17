package edu.ucla.fusa.android.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorPasswords {

    private static final String PATRON_PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private Matcher matcher;
    private Pattern pattern;

    public boolean validarPassword(String paramString) {
        this.pattern = Pattern.compile(PATRON_PASSWORD);
        this.matcher = this.pattern.matcher(paramString);
        return this.matcher.matches();
    }
}