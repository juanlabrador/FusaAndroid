package edu.ucla.fusa.android.modelo.herramientas;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.Usuario;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;

/**
 * Created by juanlabrador on 17/11/14.
 */

public class JSONParser {

    private static String URL = "http://10.0.3.2:8080/fusa.frontend/webservices/rest/";
    private String parametros;
    private RestTemplate restTemplate;

    public JSONParser() {}

    public Usuario serviceLogin(ArrayList<NameValuePair> params) {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            parametros = "ServiceLogin/" + params.get(0).getValue() + "/" + params.get(1).getValue();
            return restTemplate.getForObject(URL + parametros, Usuario.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Estudiante serviceEstudiante(ArrayList<NameValuePair> params) {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            parametros = "ServiceEstudiante/" + params.get(0).getValue();
            return restTemplate.getForObject(URL + parametros, Estudiante.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Noticia> serviceLoadingNoticias() {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Noticia[]> responseEntity = restTemplate.getForEntity(URL + "ServiceNoticias", Noticia[].class);
            return new ArrayList<Noticia>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Noticia> serviceRefreshNoticias(ArrayList<NameValuePair> params) {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            parametros = "ServiceNewNoticias/" + params.get(0).getValue();
            ResponseEntity<Noticia[]> responseEntity = restTemplate.getForEntity(URL + parametros, Noticia[].class);
            return new ArrayList<Noticia>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
