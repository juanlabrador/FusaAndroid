package edu.ucla.fusa.android.modelo;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.ucla.fusa.android.modelo.academico.Estudiante;

/**
 * Created by juanlabrador on 17/11/14.
 */

public class JSONParser {

    private static String URL = "http://10.0.3.2:8080/miserviciorest/";
    private String parametros;
    private RestTemplate restTemplate;

    public JSONParser() {}

    public Estudiante validarUsuario(ArrayList<NameValuePair> params) {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            parametros = "usuario?correo=" + params.get(0).getValue() + "&clave=" + params.get(1).getValue();
            Estudiante estudiante = restTemplate.getForObject(URL + parametros, Estudiante.class);
            return estudiante;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
