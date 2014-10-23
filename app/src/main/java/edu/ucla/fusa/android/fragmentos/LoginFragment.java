package edu.ucla.fusa.android.fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.actividades.VistasPrincipalesActivity;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

/**
 * Created by juanlabrador on 17/10/14.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private Button iniciarSesion;
    private View view;
    private TextView restaurarPassword;
    private FragmentTransaction transaction;

    public static LoginFragment newInstance() {
        LoginFragment activity = new LoginFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_login, container, false);
        email = (EditText) view.findViewById(R.id.etEmail);
        password = (EditText) view.findViewById(R.id.etPassword);
        iniciarSesion = (Button) view.findViewById(R.id.btnLogin);
        restaurarPassword = (TextView) view.findViewById(R.id.tvOlvidarPassword);
        restaurarPassword.setOnClickListener(this);
        iniciarSesion.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuVisibility(true);
        getActivity().getActionBar().show();
        getActivity().getActionBar().setTitle(R.string.titulo_action_bar_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                /**boolean validate = ValidadorEmails.validarEmail(email.getText().toString());
                if (validate != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                }**/
                startActivity(new Intent(getActivity(), VistasPrincipalesActivity.class));
                getActivity().finish();
            break;
            case R.id.tvOlvidarPassword:
                transaction = getFragmentManager().beginTransaction();
                //Reemplazamos el LoginFragment por el RestaurarPasswordFragment
                transaction.replace(android.R.id.content, RestaurarPasswordFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            break;
        }
    }

}
