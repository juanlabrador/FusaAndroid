package edu.ucla.fusa.android.fragmentos;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.seguridad.TipoUsuario;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import edu.ucla.fusa.android.validadores.ValidadorPasswords;

public class CambiarPasswordFragment extends Fragment implements TextWatcher, CompoundButton.OnCheckedChangeListener, Toolbar.OnMenuItemClickListener {
    
    private static String TAG = "CambiarPasswordFragment";
    private GroupContainer mMostrarContraseña;
    private GroupContainer mAntiguaContraseña;
    private GroupContainer mNuevaContraseña;
    private View mBarraDebil;
    private View mBarraFuerte;
    private View mBarraMuyDebil;
    private View mBarraMuyFuerte;
    private TextView mStatus;
    private Toolbar mToolbar;
    private UserTable mUserTable;
    private Usuario mUsuario;
    private JSONParser mJSONParser;
    private NotificationManager mManager;
    private VistasPrincipalesActivity mActivity;

    public static CambiarPasswordFragment newInstance() {
        CambiarPasswordFragment fragment = new CambiarPasswordFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        return inflater.inflate(R.layout.fragment_drawer_cambiar_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMostrarContraseña = (GroupContainer) view.findViewById(R.id.mostrar_contraseña);
        mMostrarContraseña.addSwitchLayout(R.string.contraseña_mostrar, getResources().getColor(R.color.azul));
        mMostrarContraseña.getSwitchLayoutAt(0).getSwitch().setOnCheckedChangeListener(this);

        mAntiguaContraseña = (GroupContainer) view.findViewById(R.id.antigua_contraseña);
        mAntiguaContraseña.addValidatorLayout(R.string.contraseña_antigua);
        mAntiguaContraseña.getValidatorLayoutAt(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mAntiguaContraseña.getValidatorLayoutAt(0).getEditText().addTextChangedListener(this);
        mAntiguaContraseña.getValidatorLayoutAt(0).setMaxLength(20);

        mNuevaContraseña = (GroupContainer) view.findViewById(R.id.nueva_contraseña);
        mNuevaContraseña.addEditTextLayout(R.string.contraseña_nueva);
        mNuevaContraseña.addValidatorLayout(R.string.contraseña_repetir );
        mNuevaContraseña.getEditTextLayoutAt(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mNuevaContraseña.getValidatorLayoutAt(1).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mNuevaContraseña.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mNuevaContraseña.getValidatorLayoutAt(1).getEditText().addTextChangedListener(this);
        mNuevaContraseña.getEditTextLayoutAt(0).setMaxLength(20);
        mNuevaContraseña.getValidatorLayoutAt(1).setMaxLength(20);

        mStatus = (TextView) view.findViewById(R.id.tv_status_password);

        mBarraMuyDebil = view.findViewById(R.id.barra_muy_debil);
        mBarraDebil = view.findViewById(R.id.barra_debil);
        mBarraFuerte = view.findViewById(R.id.barra_fuerte);
        mBarraMuyFuerte = view.findViewById(R.id.barra_muy_fuerte);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToolbar.getMenu().clear();
    }

    public void afterTextChanged(Editable editable) {
        if (!mAntiguaContraseña.getValidatorLayoutAt(0).getContent().equals("") &&
                !mNuevaContraseña.getEditTextLayoutAt(0).getContent().equals("") &&
                !mNuevaContraseña.getValidatorLayoutAt(1).getContent().equals("")) {
            mToolbar.getMenu().findItem(R.id.action_enviar).setEnabled(true);
        } else {
            mToolbar.getMenu().findItem(R.id.action_enviar).setEnabled(false);
        }

        if (mNuevaContraseña.getEditTextLayoutAt(0).getEditText().getText().hashCode() == editable.hashCode()) {
            Log.i(TAG, "¡Esta contando!");
            switch (editable.length()) {
                case 0:
                case 1:
                case 2:
                    mStatus.setText("");
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 3:
                case 4:
                    mStatus.setText(R.string.contraseña_muy_debil);
                    mStatus.setTextColor(getResources().getColor(R.color.debil));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                    mBarraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 5:
                case 6:
                case 7:
                    mStatus.setText(R.string.contraseña_debil);
                    mStatus.setTextColor(getResources().getColor(R.color.debil));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                    mBarraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 8:
                case 9:
                case 10:
                    mStatus.setText(R.string.contraseña_normal);
                    mStatus.setTextColor(getResources().getColor(R.color.normal));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                    mBarraDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 11:
                case 12:
                case 13:
                    mStatus.setText(R.string.contraseña_mejor);
                    mStatus.setTextColor(getResources().getColor(R.color.mejor));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                    mBarraDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(R.color.mejor));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 14:
                case 15:
                    mStatus.setText(R.string.contraseña_muy_fuerte);
                    mStatus.setTextColor(getResources().getColor(R.color.fuerte));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.fuerte));
                    mBarraDebil.setBackgroundColor(getResources().getColor(R.color.fuerte));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(R.color.fuerte));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(R.color.fuerte));
                    break;
            }
        }
    }

    public void beforeTextChanged(CharSequence charSequence, int i1, int i2, int i3) {}

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.contraseña_titulo_barra);
        mToolbar.inflateMenu(R.menu.action_enviar);
        mToolbar.setOnMenuItemClickListener(this);
        mUserTable = new UserTable(getActivity());
        mUsuario = mUserTable.searchUser();
        mJSONParser = new JSONParser();
        
        mActivity = (VistasPrincipalesActivity) getActivity();
    }

    public void onTextChanged(CharSequence charSequence, int i1, int i2, int i3) {
        if (mAntiguaContraseña.getValidatorLayoutAt(0).getEditText().getText().hashCode() == charSequence.hashCode()) {
            if (mAntiguaContraseña.getValidatorLayoutAt(0).getContent().equals(mUsuario.getPassword())) {
                mAntiguaContraseña.getValidatorLayoutAt(0).dataCheck();
            } else {
                mAntiguaContraseña.getValidatorLayoutAt(0).dataError();
            }
        } else if (mNuevaContraseña.getEditTextLayoutAt(0).getEditText().getText().hashCode() == charSequence.hashCode()) {
            if (mNuevaContraseña.getEditTextLayoutAt(0).getContent().equals(mNuevaContraseña.getValidatorLayoutAt(1).getContent())) {
                mNuevaContraseña.getValidatorLayoutAt(1).dataCheck();
            } else {
                mNuevaContraseña.getValidatorLayoutAt(1).dataError();
            }
        } else if (mNuevaContraseña.getValidatorLayoutAt(1).getEditText().getText().hashCode() == charSequence.hashCode()) {
            if (mNuevaContraseña.getEditTextLayoutAt(0).getContent().equals(mNuevaContraseña.getValidatorLayoutAt(1).getContent())) {
                mNuevaContraseña.getValidatorLayoutAt(1).dataCheck();
            } else {
                mNuevaContraseña.getValidatorLayoutAt(1).dataError();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            mAntiguaContraseña.getValidatorLayoutAt(0).getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
            mNuevaContraseña.getEditTextLayoutAt(0).getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
            mNuevaContraseña.getValidatorLayoutAt(1).getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mAntiguaContraseña.getValidatorLayoutAt(0).getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mNuevaContraseña.getEditTextLayoutAt(0).getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mNuevaContraseña.getValidatorLayoutAt(1).getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_enviar:
                if (mAntiguaContraseña.getValidatorLayoutAt(0).isCheck()) {
                    if (mNuevaContraseña.getValidatorLayoutAt(1).isCheck()) {
                        if (ValidadorPasswords.validarPassword(mNuevaContraseña.getEditTextLayoutAt(0).getContent())) {
                            Log.i(TAG, "¡Contraseña aceptada!");
                            mUsuario.setPassword(mNuevaContraseña.getEditTextLayoutAt(0).getContent());
                            new UpdatePassword().execute(mUsuario);
                        } else {
                            Log.i(TAG, "¡La contraseña no cumple con los requisitos minimos!");
                            SnackbarManager.show(
                                    Snackbar.with(getActivity())
                                            .type(SnackbarType.MULTI_LINE)
                                            .text(R.string.mensaje_error_contraseña_requisitos));
                        }
                    } else {
                        Log.i(TAG, "¡Las nuevas contraseñas no concuerdan!");
                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .text(R.string.mensaje_error_repetir_contraseña));
                    }
                } else {
                    Log.i(TAG, "¡La contraseña actual no concuerda!");
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text(R.string.mensaje_error_contraseña_actual));
                }
                break;
        }
        return true;
    }

    private class UpdatePassword extends AsyncTask<Usuario, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(R.layout.custom_progress_bar);
            sendNotificacion();
        }

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            return mJSONParser.updateUsuario(usuarios[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Cambio de contraseña exitoso!");
                    mActivity.actualizarPassword();
                    mUserTable.updatePassword(mUsuario.getUsername(), mUsuario.getPassword());
                    mManager.cancel(1);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.contraseña_exito));
                    getFragmentManager().popBackStack();
                    break;
                case -1:
                    Log.i(TAG, "¡Error al cambiar contraseña!");
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    mManager.cancel(1);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_enviar));
                    break;
                case 0:
                    mToolbar.getMenu().findItem(R.id.action_enviar).setActionView(null);
                    mManager.cancel(1);
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.mensaje_error_excepcion));
                    break;
            }
        }
    }

    private void sendNotificacion() {

        NotificationCompat.Builder mNotificacion = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.contraseña_enviar))
                .setTicker(getString(R.string.contraseña_enviar))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotificacion.setAutoCancel(true);
        mManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(1, mNotificacion.build());

    }
}