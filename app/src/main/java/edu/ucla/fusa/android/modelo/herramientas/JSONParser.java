package edu.ucla.fusa.android.modelo.herramientas;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.ucla.fusa.android.modelo.academico.Agrupacion;
import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.EstudiantePorAgrupacion;
import edu.ucla.fusa.android.modelo.academico.EvaluacionPorAgrupacion;
import edu.ucla.fusa.android.modelo.academico.EvaluacionPorClase;
import edu.ucla.fusa.android.modelo.evento.Evento;
import edu.ucla.fusa.android.modelo.instrumentos.Prestamo;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;
import edu.ucla.fusa.android.modelo.instrumentos.TipoPrestamo;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import edu.ucla.fusa.android.modelo.fundacion.Aspirante;
import edu.ucla.fusa.android.modelo.fundacion.InstructorAspirante;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;

/**
 * Created by juanlabrador on 17/11/14.
 */

public class JSONParser {

    public static String URL_IMAGEN = "http://10.0.3.2:8080/fusa.frontend/webservices/rest/downloads/";
    private static String TAG = "JSONParser";
    private static String URL = "http://10.0.3.2:8080/fusa.frontend/webservices/rest/";
    private String mParameters;
    private RestTemplate mRestTemplate;
    private SimpleClientHttpRequestFactory mRequestFactory ;
    
    public JSONParser() {
        mRequestFactory = new SimpleClientHttpRequestFactory();
        mRequestFactory.setConnectTimeout(2000);
        mRequestFactory.setReadTimeout(3000);
    }

    // Usuario

    public Usuario serviceLogin(ArrayList<NameValuePair> params) {
        try {
            Log.i(TAG, "¡Entra en serviceLogin!");
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "ServiceLogin/" + params.get(0).getValue() + "/" + params.get(1).getValue();
            return mRestTemplate.getForObject(URL + mParameters, Usuario.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer updateUsuario(Usuario usuario) {
        try {
            // Create a new RestTemplate instance
            mRestTemplate = new RestTemplate(mRequestFactory);

            System.setProperty("http.keepAlive", "false");
            // Add the Jackson and String message converters
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            Gson gson = new Gson();
            String json = gson.toJson(usuario);
            Log.i(TAG, json);
            // Make the HTTP POST request, marshaling the request to JSON, and the response to a Integer
            return mRestTemplate.postForObject(URL + "usuario/update", usuario, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Estudiante

    public Estudiante serviceEstudiante(String username) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "ServiceEstudiante/" + username;
            return mRestTemplate.getForObject(URL + mParameters, Estudiante.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EstudiantePorAgrupacion serviceEstudiantePorAgrupacion(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "EstudiantePorAgrupacion/" + id;
            return mRestTemplate.getForObject(URL + mParameters, EstudiantePorAgrupacion.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Agrupacion serviceAgrupacionEstudiante(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "HorarioEstudiantePorAgrupacion/" + id;
            return mRestTemplate.getForObject(URL + mParameters, Agrupacion.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<ClaseParticular> serviceClaseEstudiante(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<ClaseParticular[]> responseEntity = mRestTemplate.getForEntity(URL + "HorarioEstudiantePorClaseParticular/" + id, ClaseParticular[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateEstudiante(Estudiante estudiante) {
        // Create a new RestTemplate instance
        mRestTemplate = new RestTemplate(mRequestFactory);

        System.setProperty("http.keepAlive", "false");
        // Add the Jackson and String message converters
        mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        mRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP POST request, marshaling the request to JSON, and the response to a Integer
        return mRestTemplate.postForObject(URL + "estudiante/update", estudiante, Integer.class);
    }

    public List<EvaluacionPorAgrupacion> serviceEvaluacionPorAgrupacion(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<EvaluacionPorAgrupacion[]> responseEntity = mRestTemplate.getForEntity(URL + "EvaluacionEstudiantePorAgrupacion/" + id, EvaluacionPorAgrupacion[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EvaluacionPorClase> serviceEvaluacionPorClases(int id_clase, int id_estudiante) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<EvaluacionPorClase[]> responseEntity = mRestTemplate.getForEntity(URL + "EvalEstudianteClaseParticular/" + id_clase + "/" + id_estudiante, EvaluacionPorClase[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Noticias

    public ArrayList<Noticia> serviceLoadingNoticias() {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Noticia[]> responseEntity = mRestTemplate.getForEntity(URL + "ServiceNoticias", Noticia[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Noticia> serviceRefreshNoticias(long id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "ServiceNewNoticias/" + id;
            ResponseEntity<Noticia[]> responseEntity = mRestTemplate.getForEntity(URL + mParameters, Noticia[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Catedras

    public ArrayList<Catedra> serviceLoadingCatedras() {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Catedra[]> responseEntity = mRestTemplate.getForEntity(URL + "ServiceCatedras", Catedra[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Postulación de un estudiante

    public int uploadAspirante(Aspirante aspirante) {
        try {
            // Create a new RestTemplate instance
            mRestTemplate = new RestTemplate(mRequestFactory);

            System.setProperty("http.keepAlive", "false");
            // Add the Jackson and String message converters
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            Gson gson = new Gson();
            String json = gson.toJson(aspirante);
            Log.i(TAG, json);
            // Make the HTTP POST request, marshaling the request to JSON, and the response to a Integer
            return mRestTemplate.postForObject(URL + "aspirante/guardar", aspirante, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    // Solicitud de un prestamo

    public int uploadSolicitudPrestamo(SolicitudPrestamo solicitud) {
        try {
            // Create a new RestTemplate instance
            mRestTemplate = new RestTemplate(mRequestFactory);

            System.setProperty("http.keepAlive", "false");
            // Add the Jackson and String message converters
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            Gson gson = new Gson();
            String json = gson.toJson(solicitud);

            Log.i(TAG, json);
            // Make the HTTP POST request, marshaling the request to JSON, and the response to a Integer
            return mRestTemplate.postForObject(URL + "SolicitudPrestamo/upload", solicitud, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public SolicitudPrestamo serviceSolicitudPrestamoPorEstudiante(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "buscar_solicitud_por_estudiante/" + id;
            return mRestTemplate.getForObject(URL + mParameters, SolicitudPrestamo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SolicitudPrestamo serviceSolicitudPrestamo(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "buscar_solicitud/" + id;
            return mRestTemplate.getForObject(URL + mParameters, SolicitudPrestamo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tipo de prestamo

    public ArrayList<TipoPrestamo> serviceLoadingTipoPrestamo() {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<TipoPrestamo[]> responseEntity = mRestTemplate.getForEntity(URL + "TipoPrestamos", TipoPrestamo[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tipo de instrumento

    public ArrayList<TipoInstrumento> serviceLoadingTipoInstrumento() {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<TipoInstrumento[]> responseEntity = mRestTemplate.getForEntity(URL + "TipoInstrumentos", TipoInstrumento[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Eventos
    
    public ArrayList<Evento> serviceLoadingEventos() {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Evento[]> responseEntity = mRestTemplate.getForEntity(URL + "eventos", Evento[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Evento> serviceLoadingNuevosEventos(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "eventos_new/" + id;
            ResponseEntity<Evento[]> responseEntity = mRestTemplate.getForEntity(URL + mParameters, Evento[].class);
            return new ArrayList<>(Arrays.asList(responseEntity.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Prestamo

    public Prestamo servicePrestamo(int id) {
        try {
            mRestTemplate = new RestTemplate(mRequestFactory);
            mRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            mParameters = "buscar_prestamo/" + id;
            return mRestTemplate.getForObject(URL + mParameters, Prestamo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
