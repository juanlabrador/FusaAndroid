package edu.ucla.fusa.android.modelo.herramientas;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import edu.ucla.fusa.android.modelo.fundacion.Aspirante;
import edu.ucla.fusa.android.modelo.fundacion.InstructorAspirante;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;

/**
 * Created by juanlabrador on 17/11/14.
 */

public class JSONParser {

    private static String TAG = "JsonParser";
    private static String URL = "http://10.0.3.2:8080/fusa.frontend/webservices/rest/";
    private String parametros;
    private RestTemplate restTemplate;

    public JSONParser() {}

    // Usuario

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

    // Estudiante

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

    // Noticias

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

    // Catedras

    public ArrayList<Catedra> serviceLoadingCatedras() {
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Catedra[]> responseEntity = restTemplate.getForEntity(URL + "ServiceCatedras", Catedra[].class);
            return new ArrayList<Catedra>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Postulación de un estudiante

    public int uploadAspirante(Aspirante aspirante) throws Exception {
        // Create a new RestTemplate instance
        restTemplate = new RestTemplate(true);

        System.setProperty("http.keepAlive", "false");
        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP POST request, marshaling the request to JSON, and the response to a Integer
        return restTemplate.postForObject(URL + "aspirante/upload", aspirante, Integer.class);
    }

    // Postulación de un instructor

    public int uploadInstructorAspirante(InstructorAspirante aspirante) throws Exception {
        // Create a new RestTemplate instance
        restTemplate = new RestTemplate(true);

        System.setProperty("http.keepAlive", "false");
        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP POST request, marshaling the request to JSON, and the response to a Integer
        return restTemplate.postForObject(URL + "instructorAspirante/upload", aspirante, Integer.class);
    }

}
