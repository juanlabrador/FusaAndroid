package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.dd.CircularProgressButton;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

public class RestaurarPasswordFragment extends Fragment implements View.OnClickListener {

    private FloatingHintEditText email;
    private CircularProgressButton enviar;
    private View view;

    public static RestaurarPasswordFragment newInstance() {
        RestaurarPasswordFragment fragment = new RestaurarPasswordFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View paramView) {
        if (ValidadorEmails.validarEmail(email.getText().toString()) != true) {
            Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_restaurar_password, paramViewGroup, false);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_restaurar_password));
        enviar.setOnClickListener(this);
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_restaurar_password));
        return view;
    }
}