package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.ucla.fusa.android.actividades.R;

/**
 * Created by juanlabrador on 18/10/14.
 *
 * Clase que gestion los tipos de acceso a la aplicación, ya sea por login o por postulate.
 */
public class TipoAccesoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button postularse;
    private TextView iniciarSesion;
    private FragmentTransaction transaction;

    public static TipoAccesoFragment newInstance() {
        TipoAccesoFragment activity = new TipoAccesoFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public TipoAccesoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tipo_acceso, container, false);
        postularse = (Button) view.findViewById(R.id.btnPostularse);
        postularse.setOnClickListener(this);

        iniciarSesion = (TextView) view.findViewById(R.id.tvIniciarSesion);
        iniciarSesion.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPostularse:
               transaction = getFragmentManager().beginTransaction();
               transaction.replace(android.R.id.content, RegistrarPostulacionesFragment.newInstance());
               transaction.addToBackStack(null);
               transaction.commit();
            break;

            case R.id.tvIniciarSesion:
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, LoginFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            break;
        }
    }
}
