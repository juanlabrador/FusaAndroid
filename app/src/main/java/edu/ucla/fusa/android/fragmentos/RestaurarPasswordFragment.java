package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

/**
 * Created by juanlabrador on 18/10/14.
 *
 * Clase encargada de reportar a la administración si el usuario a perdido o no recuerda su contraseña.
 */
public class RestaurarPasswordFragment extends Fragment implements View.OnClickListener {

    private Button enviarSolicitud;
    private EditText email;
    private View view;

    public static RestaurarPasswordFragment newInstance() {
        RestaurarPasswordFragment activity = new RestaurarPasswordFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public RestaurarPasswordFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_restaurar_password, container, false);

        enviarSolicitud = (Button) view.findViewById(R.id.btnRestaurarPassword);
        enviarSolicitud.setOnClickListener(this);

        email = (EditText) view.findViewById(R.id.etEmail);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().show();
        getActivity().getActionBar().setTitle(R.string.titulo_action_bar_restaurar);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnRestaurarPassword) {
            /** Metodo que valida que el correo sea válido */
            boolean validar = ValidadorEmails.validarEmail(email.getText().toString());
            if (validar != true) {
                Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
