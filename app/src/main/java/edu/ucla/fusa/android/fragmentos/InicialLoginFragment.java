package edu.ucla.fusa.android.fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasInicialesActivity;
import edu.ucla.fusa.android.VistasPrincipalesActivity;

/**
 * Created by juanlabrador on 17/10/14.
 *
 * Clase encargada del acceso del usuario.
 */
public class InicialLoginFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private EditText email;
    private EditText password;
    private CircularProgressButton iniciarSesion;
    private View view;
    private TextView restaurarPassword;
    private FragmentTransaction transaction;
    private Bundle arguments;

    public static InicialLoginFragment newInstance() {
        InicialLoginFragment activity = new InicialLoginFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public InicialLoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_inicial_login, container, false);

        email = (EditText) view.findViewById(R.id.etEmail);
        password = (EditText) view.findViewById(R.id.etPassword);
        iniciarSesion = (CircularProgressButton) view.findViewById(R.id.btn_login);
        restaurarPassword = (TextView) view.findViewById(R.id.tvOlvidarPassword);
        restaurarPassword.setOnClickListener(this);
        iniciarSesion.setOnClickListener(this);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);

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
        getActivity().getActionBar().setIcon(android.R.color.transparent);
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
                /** Reemplazamos el LoginFragment por el RestaurarPasswordFragment */
                transaction.replace(R.id.inicial_container, InicialRestaurarPasswordFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                startActivity(new Intent(getActivity(), VistasInicialesActivity.class).putExtra("position", 0));
                getActivity().finish();
                return true;
            }
        }
        return false;
    }
}
