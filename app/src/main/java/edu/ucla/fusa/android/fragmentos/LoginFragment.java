package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd.CircularProgressButton;
import com.github.siyamed.shapeimageview.HexagonImageView;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static String TAG = "LoginFragment";
    private GroupContainer mCredenciales;
    private HexagonImageView mAvatar;
    private CircularProgressButton mLogin;
    private TextView mPasswordRestore;
    private TextView mChangeAccount;
    private JSONParser mJSONParser;
    private UserTable mUserTable;
    private Usuario mUsuario;
    private SharedPreferences mPreferencias;
    private Bitmap mBitmap;
    private String mUsername;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogin = (CircularProgressButton) view.findViewById(R.id.btn_iniciar_sesion);
        mLogin.setOnClickListener(this);

        mAvatar = (HexagonImageView) view.findViewById(R.id.avatar_login);
        mAvatar.setImageResource(R.drawable.no_avatar);

        mPasswordRestore = (TextView) view.findViewById(R.id.tv_olvidar_password_iniciar_sesion);
        mPasswordRestore.setOnClickListener(this);

        mChangeAccount = (TextView) view.findViewById(R.id.tv_cambiar_usuario);
        mChangeAccount.setOnClickListener(this);

        mCredenciales = (GroupContainer) view.findViewById(R.id.login_credenciales);
        Log.i(TAG, "¡Paso al restaurar pantalla!");
        mCredenciales.clear();
        mCredenciales.addEditTextLayout(R.string.login_usuario);
        mCredenciales.addEditTextLayout(R.string.login_contraseña);
        mCredenciales.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mCredenciales.getEditTextLayoutAt(1).getEditText().addTextChangedListener(this);
        mCredenciales.getEditTextLayoutAt(1).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        restoreData();
    }

    public void restoreData() {
        mPreferencias = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        if (!mPreferencias.getString("usuario", "").equals("")) {
            Log.i(TAG, "¡Tiene datos en cache!");
            mCredenciales.getEditTextLayoutAt(0).setContent(mPreferencias.getString("usuario", ""));
            //mCredenciales.getEditTextLayoutAt(0).getEditText().setFocusable(false);
            if (!mPreferencias.getString("foto", "").equals("")) {
                mBitmap = convertByteToImage(mPreferencias.getString("foto", ""));

                if (mBitmap != null) {
                    mAvatar.setImageBitmap(mBitmap);
                } else {
                    Log.i(TAG, "¡Avatar nulo!");
                }
            }
            mChangeAccount.setVisibility(View.VISIBLE);
        }
    }

    public void afterTextChanged(Editable paramEditable) {}

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_iniciar_sesion:
                mUsername = mCredenciales.getEditTextLayoutAt(0).getContent();
                if (exiteConexionInternet() != false) {
                    deshabilitarElementos();
                    if (!mUsername.equals("") &&
                            !mCredenciales.getEditTextLayoutAt(1).getContent().equals("")) {
                        new Login().execute(mUsername, mCredenciales.getEditTextLayoutAt(1).getContent());
                    } else {
                        habilitarElementos();
                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .type(SnackbarType.MULTI_LINE)
                                        .text(R.string.mensaje_error_iniciar_sesion));
                    }
                } else {
                    habilitarElementos();
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_conexion));
                }
                break;
            case R.id.tv_olvidar_password_iniciar_sesion:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(android.R.id.content, RestaurarPasswordFragment.newInstance())
                        .commit();
                break;
            case R.id.tv_cambiar_usuario:
                mAvatar.setImageResource(R.drawable.no_avatar);
                mPreferencias.edit().clear().commit();
                mChangeAccount.setVisibility(View.GONE);
                cambiarUsuario();
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
        byte[] bytes = Base64.decode(data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private Bitmap convertByteToImage(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public boolean exiteConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        State edge = cm.getNetworkInfo(0).getState();
        State wifi = cm.getNetworkInfo(1).getState();
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (edge == State.CONNECTED || edge == State.CONNECTING) {
            return true;
        }else
        if(wifi == State.CONNECTED || wifi == State.CONNECTING) {
            return true;
        }else{
            return false;
        }
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
            /** Cargamos los parametros que enviaremos por URL */
            ArrayList<NameValuePair> parametros = new ArrayList<>();
            parametros.add(new BasicNameValuePair("username", params[0]));
            parametros.add(new BasicNameValuePair("password", params[1]));

            /** Mandamos los parametros y esperemos una respuesta del servidor */
            mUsuario = mJSONParser.serviceLogin(parametros);
            if (mUsuario != null) {
                Log.i(TAG, "Username: " + mUsuario.getUsername());
                if (!mUsuario.getUsername().equals("")) { /** Si el usuario existe */
                    /** Guardamos sus datos internamente para que no se loguee de nuevo */
                    mUserTable.insertData(
                            mUsuario.getUsername(),
                            mUsuario.getPassword(),
                            mUsuario.getNombre(),
                            mUsuario.getApellido(),
                            mUsuario.getCorreo(),
                            mUsuario.getFoto(),
                            mUsuario.getTipoUsuario().getId());
                    return 100;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mLogin.setProgress(result);
            switch (result) {
                case 100:
                    if (mUsuario.getTipoUsuario().getId() == 1) {
                        if (mUsuario.getFoto() != null && mUsuario.getFoto().length > 40) {
                            mBitmap = convertByteToImage(mUsuario.getFoto());
                            mAvatar.setImageBitmap(mBitmap);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(3000);
                                Log.i(TAG, "!Login success!");
                                startActivity(new Intent(getActivity(), VistasPrincipalesActivity.class)
                                        .putExtra("NombreUsuario", mUsuario.getUsername()));
                                getActivity().finish();
                            }
                        }).start();
                    } else {
                        Log.i(TAG, "¡No es un estudiante!");
                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .type(SnackbarType.MULTI_LINE)
                                        .text(R.string.mensaje_error_iniciar_sesion));
                        habilitarElementos();
                        getActivity().deleteDatabase(DataBaseHelper.NAME);
                    }
                    break;
                case -1:
                    Log.i(TAG, "Error con las credenciales");
                    habilitarElementos();
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_iniciar_sesion));

                    break;
                case 0:
                    habilitarElementos();
                    Log.i(TAG, "Problemas con conectividad");
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_servidor));
                    break;
            }
        }
    }

    private void habilitarElementos() {
        Log.i(TAG, "¡Habilita elementos!");
        //mCredenciales.getEditTextLayoutAt(1).getEditText().setFocusable(true);
        mPasswordRestore.setEnabled(true);
        mPasswordRestore.setTextColor(getResources().getColor(R.color.azul_oscuro));
        mChangeAccount.setTextColor(getResources().getColor(R.color.azul_oscuro));
        mChangeAccount.setEnabled(true);
    }
    
    private void cambiarUsuario() {
        mCredenciales.getEditTextLayoutAt(0).setContent("");
        mCredenciales.getEditTextLayoutAt(1).setContent("");
        //mCredenciales.getEditTextLayoutAt(0).getEditText().setFocusable(true);
        //mCredenciales.getEditTextLayoutAt(1).getEditText().setFocusable(true);
        mLogin.setProgress(0);
        
    }

    private void deshabilitarElementos() {
        Log.i(TAG, "¡Deshabilita elementos!");
        //mCredenciales.getEditTextLayoutAt(1).getEditText().setFocusable(false);
        mPasswordRestore.setEnabled(false);
        mChangeAccount.setEnabled(false);
    }
}
