package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dd.CircularProgressButton;
import com.github.siyamed.shapeimageview.HexagonImageView;
import com.juanlabrador.GroupLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;

import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.herramientas.Base64;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;

public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static String TAG = "LoginFragment";
    private GroupLayout mCredenciales;
    private HexagonImageView mAvatar;
    private CircularProgressButton mLogin;
    private FloatingHintEditText mPassword;
    private TextView mPasswordRestore;
    private View mView;
    private JSONParser mJSONParser;
    private UserTable mUserTable;
    private Usuario mUsuario;
    private SharedPreferences mPreferencias;
    private Bitmap mBitmap;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        mView = inflater.inflate(R.layout.fragment_inicial_login, container, false);

        mLogin = (CircularProgressButton) mView.findViewById(R.id.btn_iniciar_sesion);
        
        mCredenciales = (GroupLayout) mView.findViewById(R.id.login_credenciales);
        mCredenciales.addEditTextLayout(R.string.login_usuario);
        mCredenciales.addEditTextLayout(R.string.login_contraseña);
        mCredenciales.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mCredenciales.getEditTextLayoutAt(1).getEditText().addTextChangedListener(this);
        mCredenciales.getEditTextLayoutAt(1).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        mAvatar = (HexagonImageView) mView.findViewById(R.id.avatar_login);
        mAvatar.setImageResource(R.drawable.no_avatar);
        
        mPasswordRestore = (TextView) mView.findViewById(R.id.tv_olvidar_password_iniciar_sesion);
        mPasswordRestore.setOnClickListener(this);

        mLogin.setOnClickListener(this);

        mPreferencias = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        if (!mPreferencias.getString("usuario", "").equals("") && 
                !mPreferencias.getString("foto", "").equals("")) {
            mCredenciales.getEditTextLayoutAt(0).setContent(mPreferencias.getString("usuario", ""));
            mBitmap = convertByteToImage(mPreferencias.getString("foto", ""));
            if (mBitmap != null) {
                mAvatar.setImageBitmap(convertByteToImage(mPreferencias.getString("foto", "")));
            } else {
                Log.i(TAG, "¡Avatar nulo");
            }
        }
        
        return mView;
    }

    public void afterTextChanged(Editable paramEditable) {}

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_iniciar_sesion:
                if (exiteConexionInternet() != false) {
                    deshabilitarElementos();
                    if (!mCredenciales.getEditTextLayoutAt(0).getContent().equals("") && 
                            !mCredenciales.getEditTextLayoutAt(1).getContent().equals("")) {
                        new Login().execute(mCredenciales.getEditTextLayoutAt(0).getContent(), mCredenciales.getEditTextLayoutAt(1).getContent());
                    } else {
                        habilitarElementos();
                        errorIniciarSesion();
                    }
                } else {
                    errorConexionInternet();
                }
                break;
            case R.id.tv_olvidar_password_iniciar_sesion:
                mPasswordRestore.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, RestaurarPasswordFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJSONParser = new JSONParser();
        mUserTable = new UserTable(getActivity());
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        mLogin.setProgress(0);
    }

    private Bitmap convertByteToImage(String data) {
        try {
            return BitmapFactory.decodeByteArray(Base64.decode(data), 0, Base64.decode(data).length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    private class Login extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLogin.setProgress(0);
            mLogin.setIndeterminateProgressMode(true);
            mLogin.setProgress(50);
        }

        @Override
        protected Integer doInBackground(String... params) {
            SystemClock.sleep(3000);
            int response = -1;
            /** Cargamos los parametros que enviaremos por URL */
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("username", params[0]));
            parametros.add(new BasicNameValuePair("password", params[1]));

            /** Mandamos los parametros y esperemos una respuesta del servidor */
            mUsuario = mJSONParser.serviceLogin(parametros);
            if (mUsuario != null) {
                Log.i(TAG, "Username: " + mUsuario.getUsername());
                if (mUsuario.getId() != -1) { /** Si el usuario existe */
                    /** Guardamos sus datos internamente para que no se loguee de nuevo */
                    mUserTable.insertData(
                            mUsuario.getUsername(),
                            mUsuario.getPassword(),
                            mUsuario.getFoto(),
                            mUsuario.getTipoUsuario().getId());
                    response = 100;
                } else { /** Si el usuario no existe */
                    response = -1;
                }
            } else { /** Hubo problemas con el servidor o lentitud de la red */
                response = 0;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mLogin.setProgress(result);
            switch (result) {
                case 100:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(2000);
                            if (mUsuario.getTipoUsuario().getId() == 1) {
                                Log.i(TAG, "!Login success!");
                                startActivity(new Intent(getActivity(), VistasPrincipalesActivity.class)
                                        .putExtra("NombreUsuario", mUsuario.getUsername()));
                                getActivity().finish();
                            } else {
                                Log.i(TAG, "¡No es un estudiante!");
                                errorIniciarSesion();
                                habilitarElementos();
                                getActivity().deleteDatabase(DataBaseHelper.NAME);
                            }
                        }
                    }).start();
                    break;
                case -1:
                    Log.i(TAG, "Error con las credenciales");
                    errorIniciarSesion();
                    habilitarElementos();
                    break;
                case 0:
                    Log.i(TAG, "Problemas con conectividad");
                    errorServidor();
                    habilitarElementos();
                    break;
            }
        }
    }

    private void habilitarElementos() {
        mCredenciales.getEditTextLayoutAt(0).getEditText().setFocusable(true);
        mCredenciales.getEditTextLayoutAt(1).getEditText().setFocusable(true);
        mCredenciales.getEditTextLayoutAt(0).getEditText().setTextIsSelectable(true);
        mCredenciales.getEditTextLayoutAt(0).getEditText().setFocusable(true);
        mCredenciales.getEditTextLayoutAt(0).getEditText().setFocusableInTouchMode(true);
        mCredenciales.getEditTextLayoutAt(1).getEditText().setTextIsSelectable(true);
        mCredenciales.getEditTextLayoutAt(1).getEditText().setFocusable(true);
        mCredenciales.getEditTextLayoutAt(1).getEditText().setFocusableInTouchMode(true);
        mPasswordRestore.setEnabled(true);
        mPasswordRestore.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void deshabilitarElementos() {
        mCredenciales.getEditTextLayoutAt(0).getEditText().setFocusable(false);
        mCredenciales.getEditTextLayoutAt(1).getEditText().setFocusable(false);
        mPasswordRestore.setEnabled(false);
        mPasswordRestore.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }
}
