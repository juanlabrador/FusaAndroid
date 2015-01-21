package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.dd.CircularProgressButton;
import com.juanlabrador.GroupLayout;

import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;
import edu.ucla.fusa.android.validadores.ValidadorPasswords;

public class CambiarPasswordFragment extends Fragment implements TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    
    private static String TAG = "CambiarPasswordFragment";
    private GroupLayout mMostrarContraseña;
    private GroupLayout mAntiguaContraseña;
    private GroupLayout mNuevaContraseña;
    private View mBarraDebil;
    private View mBarraFuerte;
    private View mBarraMuyDebil;
    private View mBarraMuyFuerte;
    private CircularProgressButton mBoton;
    private TextView mStatus;
    private ValidadorPasswords mValidador;
    private View mView;
    private Toolbar mToolbar;
    private UserTable mUserTable;
    private Usuario mUsuario;
    private String mActualContraseña;

    public static CambiarPasswordFragment newInstance() {
        CambiarPasswordFragment fragment = new CambiarPasswordFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        mView = inflater.inflate(R.layout.fragment_configuraciones_cambiar_password, container, false);

        mMostrarContraseña = (GroupLayout) mView.findViewById(R.id.mostrar_contraseña);
        mMostrarContraseña.addSwitchLayout(R.string.contraseña_mostrar, getResources().getColor(R.color.azul));
        mMostrarContraseña.getSwitchLayoutAt(0).getSwitch().setOnCheckedChangeListener(this);
        
        mAntiguaContraseña = (GroupLayout) mView.findViewById(R.id.antigua_contraseña);
        mAntiguaContraseña.addValidatorLayout(R.string.contraseña_antigua);
        mAntiguaContraseña.getValidatorLayoutAt(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mAntiguaContraseña.getValidatorLayoutAt(0).getEditText().addTextChangedListener(this);
        
        mNuevaContraseña = (GroupLayout) mView.findViewById(R.id.nueva_contraseña);
        mNuevaContraseña.addEditTextLayout(R.string.contraseña_nueva);
        mNuevaContraseña.addValidatorLayout(R.string.contraseña_repetir );
        mNuevaContraseña.getEditTextLayoutAt(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mNuevaContraseña.getValidatorLayoutAt(1).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mNuevaContraseña.getEditTextLayoutAt(0).getEditText().addTextChangedListener(this);
        mNuevaContraseña.getValidatorLayoutAt(1).getEditText().addTextChangedListener(this);
        
        mStatus = (TextView) mView.findViewById(R.id.tv_status_password);

        mBarraMuyDebil = mView.findViewById(R.id.barra_muy_debil);
        mBarraDebil = mView.findViewById(R.id.barra_debil);
        mBarraFuerte = mView.findViewById(R.id.barra_fuerte);
        mBarraMuyFuerte = mView.findViewById(R.id.barra_muy_fuerte);
        mBoton = (CircularProgressButton) mView.findViewById(R.id.btn_cambiar_password);
        mBoton.setOnClickListener(this);
        
        mBoton.setEnabled(false);
        mBoton.setBackgroundColor(getResources().getColor(R.color.gris_oscuro));
        return mView;
    }

    public void afterTextChanged(Editable editable) {
        if (!mAntiguaContraseña.getValidatorLayoutAt(0).getContent().equals("") &&
                !mNuevaContraseña.getEditTextLayoutAt(0).getContent().equals("") &&
                !mNuevaContraseña.getValidatorLayoutAt(1).getContent().equals("")) {
            mBoton.setBackgroundResource(R.color.azul);
            mBoton.setEnabled(true);
        } else {
            mBoton.setEnabled(false);
            mBoton.setBackgroundColor(getResources().getColor(R.color.gris_oscuro));
        }

        if (mNuevaContraseña.getEditTextLayoutAt(0).getEditText().getText().hashCode() == editable.hashCode()) {
            Log.i(TAG, "¡Esta contando!");
            switch (editable.length()) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    mStatus.setText("");
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 6:
                case 7:
                case 8:
                    mStatus.setText(R.string.contraseña_muy_debil);
                    mStatus.setTextColor(getResources().getColor(R.color.debil));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                    mBarraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 9:
                case 10:
                    mStatus.setText(R.string.contraseña_debil);
                    mStatus.setTextColor(getResources().getColor(R.color.debil));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                    mBarraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 11:
                case 12:
                case 13:
                    mStatus.setText(R.string.contraseña_normal);
                    mStatus.setTextColor(getResources().getColor(R.color.normal));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                    mBarraDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case 14:
                case 15:
                case 16:
                case 17:
                    mStatus.setText(R.string.contraseña_mejor);
                    mStatus.setTextColor(getResources().getColor(R.color.mejor));
                    mBarraMuyDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                    mBarraDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                    mBarraFuerte.setBackgroundColor(getResources().getColor(R.color.mejor));
                    mBarraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                case 18:
                case 19:
                case 20:
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

    public void onClick(View view) {
        if (mAntiguaContraseña.getValidatorLayoutAt(0).getEstado()) {
            if (mNuevaContraseña.getValidatorLayoutAt(1).getEstado()) {
                if (ValidadorPasswords.validarPassword(mNuevaContraseña.getEditTextLayoutAt(0).getContent())) {
                    Log.i(TAG, "¡Contraseña aceptada!");
                } else {
                    Log.i(TAG, "¡La contraseña no cumple con los requisitos minimos!");
                }
            } else {
                Log.i(TAG, "¡Las nuevas contraseñas no concuerdan!");
            }
        } else {
            Log.i(TAG, "¡La contraseña actual no concuerda!");
        }
        
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.contraseña_titulo_barra);
        mUserTable = new UserTable(getActivity());
        mUsuario = mUserTable.searchUser();
    }

    public void onTextChanged(CharSequence charSequence, int i1, int i2, int i3) {
        if (mAntiguaContraseña.getValidatorLayoutAt(0).getEditText().getText().hashCode() == charSequence.hashCode()) {
            if (mAntiguaContraseña.getValidatorLayoutAt(0).getContent().equals(mUsuario.getPassword())) {
                mAntiguaContraseña.getValidatorLayoutAt(0).dataCheck();
            } else {
                mAntiguaContraseña.getValidatorLayoutAt(0).dataError();
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
    
    private class UpdatePassword extends AsyncTask<Usuario, Void, Integer> {

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            return null;
        }
    }
}