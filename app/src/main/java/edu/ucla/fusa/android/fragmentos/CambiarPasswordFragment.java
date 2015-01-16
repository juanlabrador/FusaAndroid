package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd.CircularProgressButton;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.validadores.ValidadorPasswords;

public class CambiarPasswordFragment extends Fragment implements TextWatcher, View.OnClickListener {

    private FloatingHintEditText antiguoPassword;
    private View barraDebil;
    private View barraFuerte;
    private View barraMuyDebil;
    private View barraMuyFuerte;
    private CircularProgressButton cambiarPassword;
    private FloatingHintEditText nuevoPassword;
    private FloatingHintEditText repitaNuevoPassword;
    private TextView status;
    private ValidadorPasswords validador;
    private View view;

    public static CambiarPasswordFragment newInstance() {
        CambiarPasswordFragment fragment = new CambiarPasswordFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void afterTextChanged(Editable paramEditable) {
        if (((this.antiguoPassword.getText().toString().trim().equals("")) && (this.nuevoPassword.getText().toString().trim().equals("")) && (this.repitaNuevoPassword.getText().toString().trim().equals("")))
                || ((!this.antiguoPassword.getText().toString().trim().equals("")) && (this.nuevoPassword.getText().toString().trim().equals("")) && (this.repitaNuevoPassword.getText().toString().trim().equals("")))
                || ((this.antiguoPassword.getText().toString().trim().equals("")) && (!this.nuevoPassword.getText().toString().trim().equals("")) && (this.repitaNuevoPassword.getText().toString().trim().equals("")))
                || ((this.antiguoPassword.getText().toString().trim().equals("")) && (this.nuevoPassword.getText().toString().trim().equals("")) && (!this.repitaNuevoPassword.getText().toString().trim().equals("")))
                || ((!this.antiguoPassword.getText().toString().trim().equals("")) && (!this.nuevoPassword.getText().toString().trim().equals("")) && (this.repitaNuevoPassword.getText().toString().trim().equals("")))
                || ((!this.antiguoPassword.getText().toString().trim().equals("")) && (this.nuevoPassword.getText().toString().trim().equals("")) && (!this.repitaNuevoPassword.getText().toString().trim().equals("")))
                || ((this.antiguoPassword.getText().toString().trim().equals("")) && (!this.nuevoPassword.getText().toString().trim().equals("")) && (!this.repitaNuevoPassword.getText().toString().trim().equals("")))
                || (!this.nuevoPassword.getText().toString().trim().equals(this.repitaNuevoPassword.getText().toString().trim()))) {
          cambiarPassword.setEnabled(false);
          cambiarPassword.setBackgroundColor(getResources().getColor(R.color.gris_oscuro));
          return;
        }
        cambiarPassword.setBackgroundResource(R.color.azul);
        cambiarPassword.setEnabled(true);
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

    public void onClick(View paramView) {}

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_cambiar_password_white);
        getActivity().getActionBar().setTitle(R.string.configuracion_cuenta_cambiar_password);
    }

    @Nullable
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_configuraciones_cambiar_password, paramViewGroup, false);
        antiguoPassword = ((FloatingHintEditText) view.findViewById(R.id.et_antiguo_password));
        nuevoPassword = ((FloatingHintEditText) view.findViewById(R.id.et_nuevo_password));
        repitaNuevoPassword = ((FloatingHintEditText) view.findViewById(R.id.et_repita_nuevo_password));
        status = ((TextView) view.findViewById(R.id.tv_status_password));
        antiguoPassword.addTextChangedListener(this);
        nuevoPassword.addTextChangedListener(this);
        repitaNuevoPassword.addTextChangedListener(this);
        barraMuyDebil = view.findViewById(R.id.barra_muy_debil);
        barraDebil = view.findViewById(R.id.barra_debil);
        barraFuerte = view.findViewById(R.id.barra_fuerte);
        barraMuyFuerte = view.findViewById(R.id.barra_muy_fuerte);
        cambiarPassword = ((CircularProgressButton) view.findViewById(R.id.btn_cambiar_password));
        cambiarPassword.setOnClickListener(this);
        return view;
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        if (nuevoPassword.getText().hashCode() == paramCharSequence.hashCode()) {
            switch (paramCharSequence.length()) {
                default:
                  status.setText(R.string.nivel_dificulad_contraseña_muy_fuerte);
                  status.setTextColor(getResources().getColor(R.color.fuerte));
                  barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.fuerte));
                  barraDebil.setBackgroundColor(getResources().getColor(R.color.fuerte));
                  barraFuerte.setBackgroundColor(getResources().getColor(R.color.fuerte));
                  barraMuyFuerte.setBackgroundColor(getResources().getColor(R.color.fuerte));
                  break;
                  case 0:
                      status.setText("");
                      barraMuyDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      break;
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                      status.setText(R.string.nivel_dificulad_contraseña_muy_debil);
                      status.setTextColor(getResources().getColor(R.color.debil));
                      barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                      barraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      break;
                  case 5:
                  case 6:
                      status.setText(R.string.nivel_dificulad_contraseña_debil);
                      status.setTextColor(getResources().getColor(R.color.debil));
                      barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.debil));
                      barraDebil.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      break;
                  case 7:
                  case 8:
                      status.setText(R.string.nivel_dificulad_contraseña_normal);
                      status.setTextColor(getResources().getColor(R.color.normal));
                      barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                      barraDebil.setBackgroundColor(getResources().getColor(R.color.normal));
                      barraFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                      break;
                  case 9:
                  case 10:
                  case 11:
                      status.setText(R.string.nivel_dificulad_contraseña_fuerte);
                      status.setTextColor(getResources().getColor(R.color.mejor));
                      barraMuyDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                      barraDebil.setBackgroundColor(getResources().getColor(R.color.mejor));
                      barraFuerte.setBackgroundColor(getResources().getColor(R.color.mejor));
                      barraMuyFuerte.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
        }
    }
}