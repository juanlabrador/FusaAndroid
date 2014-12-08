package edu.ucla.fusa.android.modelo.herramientas;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.eventos.Noticia;

/**
 * Created by juanlabrador on 17/11/14.
 */

public class JSONParser {

    private static String URL2 = "http://10.0.3.2:8080/miserviciorest/";
    private static String URL = "http://10.0.3.2:8080/fusa.frontend/webservices/rest/";
    private String parametros;
    private RestTemplate restTemplate;

    public JSONParser() {}

    public Estudiante validarUsuario(ArrayList<NameValuePair> params) {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            parametros = "usuario?correo=" + params.get(0).getValue() + "&clave=" + params.get(1).getValue();
            Estudiante estudiante = restTemplate.getForObject(URL2 + parametros, Estudiante.class);
            return estudiante;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Noticia> listadoNoticias() {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Noticia[]> responseEntity = restTemplate.getForEntity(URL + "noticias", Noticia[].class);
            ArrayList<Noticia> noticias = new ArrayList<Noticia>(Arrays.asList(responseEntity.getBody()));
            return noticias;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
