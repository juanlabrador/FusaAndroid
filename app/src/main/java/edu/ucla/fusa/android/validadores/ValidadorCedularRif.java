package edu.ucla.fusa.android.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorCedularRif {

    private static final String PATRON_CEDULARIF = "^V-[0-9]{8}(-[0-9])?|[GJ]-[0-9]{8}-[0-9]$";
    private static Matcher matcher;
    private static Pattern pattern;

    public static boolean validarCedulaRif(String paramString) {
        pattern = Pattern.compile(PATRON_CEDULARIF);
        matcher = pattern.matcher(paramString);
        return matcher.matches();
    }
}