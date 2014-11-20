package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dd.CircularProgressButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.JSONParser;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

public class InicialLoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private FloatingHintEditText email;
    private CircularProgressButton iniciarSesion;
    private FloatingHintEditText password;
    private TextView restaurarPassword;
    private View view;
    private JSONParser jsonParser = new JSONParser();
    private UserTable db;

    public static InicialLoginFragment newInstance() {
        InicialLoginFragment fragment = new InicialLoginFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void afterTextChanged(Editable paramEditable) {}

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_iniciar_sesion:
                if (exiteConexionInternet() != false) {
                    deshabilitarElementos();
                    if (ValidadorEmails.validarEmail(this.email.getText().toString()) != true) {
                        Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                        habilitarElementos();
                    } else {
                        new LoginTaks().execute(email.getText().toString(), password.getText().toString());
                    }
                } else {
                    errorConexionInternet();
                }
                break;
            case R.id.tv_olvidar_password_iniciar_sesion:
                restaurarPassword.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, InicialRestaurarPasswordFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_login, paramViewGroup, false);
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_iniciar_sesion));
        password = ((FloatingHintEditText) view.findViewById(R.id.et_password_iniciar_sesion));
        iniciarSesion = ((CircularProgressButton) view.findViewById(R.id.btn_iniciar_sesion));
        restaurarPassword = ((TextView) view.findViewById(R.id.tv_olvidar_password_iniciar_sesion));
        restaurarPassword.setOnClickListener(this);
        iniciarSesion.setOnClickListener(this);
        email.addTextChangedListener(this);
        password.addTextChangedListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new UserTable(getActivity());
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        iniciarSesion.setProgress(0);
    }

    public boolean exiteConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //State edge = cm.getNetworkInfo(0).getState();
        State wifi = cm.getNetworkInfo(1).getState();
        //NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //if (edge == State.CONNECTED || edge == State.CONNECTING) {
            //return true;
        //}else
        if(wifi == State.CONNECTED || wifi == State.CONNECTING) {
            return true;
        }else{
            return false;
        }
    }

    public void errorIniciarSesion(){
        Vibrator vibrator =(Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast.makeText(getActivity(), R.string.mensaje_error_iniciar_sesion, Toast.LENGTH_SHORT).show();
    }

    public void errorConexionInternet(){
        Vibrator vibrator =(Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast.makeText(getActivity(), R.string.mensaje_error_conexion, Toast.LENGTH_SHORT).show();;
    }

    public void errorServidor(){
        Vibrator vibrator =(Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast.makeText(getActivity(),R.string.mensaje_error_servidor, Toast.LENGTH_SHORT).show();;
    }

    private class LoginTaks extends AsyncTask<String, Void, Integer> {

        private JSONObject jsonObject;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iniciarSesion.setIndeterminateProgressMode(true);
            iniciarSesion.setProgress(50);
        }

        @Override
        protected Integer doInBackground(String... params) {
            SystemClock.sleep(3000);
            int response = -1;
            /** Cargamos los parametros que enviaremos por URL */
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("correo", params[0]));
            parametros.add(new BasicNameValuePair("clave", params[1]));

            /** Mandamos los parametros via GET y esperemos una respuesta del servidor */
            jsonObject = jsonParser.validarUsuario("GET", parametros);
            try {
                if (jsonObject != null) { /** Si la consulta se realizo */
                    Log.i("JSON", Integer.toString(jsonObject.getInt("id")));
                    if (jsonObject.getInt("id") != -1) { /** Si el usuario existe */
                        /** Guardamos sus datos internamente para que no se loguee de nuevo */

                        db.insertData(jsonObject.getJSONObject("usuario").getInt("id"),
                                jsonObject.getJSONObject("usuario").getString("nombre"),
                                jsonObject.getJSONObject("usuario").getString("clave"),
                                null,
                                jsonObject.getInt("id"),
                                jsonObject.getString("cedula"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("apellido"));
                        Log.i("CEDULA", jsonObject.getString("cedula"));
                        response = 100;
                    } else { /** Si el usuario no existe */
                        response = -1;
                    }
                } else { /** Hubo problemas con el servidor o lentitud de la red */
                    response = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            Log.i("RESPONSE", Integer.toString(i));
            iniciarSesion.setProgress(i);
            if (i == 100) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        Log.i("Login", "Felicidades, te logueaste");
                        startActivity(new Intent(getActivity(), VistasPrincipalesActivity.class));
                        getActivity().finish();
                    }
                }).start();

            } else if (i == -1) {
                Log.i("Login", "Error con las credenciales");
                errorIniciarSesion();
                habilitarElementos();
            } else {
                Log.i("Login", "Problemas con conectividad");
                errorServidor();
                habilitarElementos();
            }
        }
    }

    private void habilitarElementos() {
        email.setEnabled(true);
        password.setEnabled(true);
        restaurarPassword.setEnabled(true);
        restaurarPassword.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void deshabilitarElementos() {
        email.setEnabled(false);
        password.setEnabled(false);
        restaurarPassword.setEnabled(false);
        restaurarPassword.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }
}
