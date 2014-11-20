package edu.ucla.fusa.android.modelo;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by juanlabrador on 17/11/14.
 */

public class JSONParser {

    private static String URL = "http://10.0.3.2:8080/miserviciorest/";
    static JSONObject jObj = null;
    String parametros;
    HttpPost httpPost;
    HttpGet httpGet;
    HttpResponse httpResponse;
    HttpEntity httpEntity;

    public JSONParser() {}

    //Metodo que valida la sesión de un usuario a la aplicación
    public JSONObject validarUsuario(String method, ArrayList<NameValuePair> params){
        try { //Si es via POST
            if(method == "POST"){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(URL);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                httpResponse = httpClient.execute(httpPost);

            }else if(method == "GET") { //Si es via GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                parametros = "usuario?correo=" + params.get(0).getValue() + "&clave=" + params.get(1).getValue();
                httpGet = new HttpGet(URL + parametros);
                httpResponse = httpClient.execute(httpGet);
            }
            if (httpResponse == null) { //Si no hay respuesta del servidor
                return null;
            } else {
                //Obtenemos la respuesta de la consulta
                httpEntity = httpResponse.getEntity();
                String resultado = EntityUtils.toString(httpEntity);
                jObj = new JSONObject(resultado);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.toString();
        }
        return jObj;
    }
}
