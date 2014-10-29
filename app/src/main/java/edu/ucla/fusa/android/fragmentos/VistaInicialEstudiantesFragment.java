package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 16/10/14.
 *
 * Clase que administra la información presentada en una de las vistas iniciales del ViewPager
 *
 */

public class VistaInicialEstudiantesFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button postulate;

    public static VistaInicialEstudiantesFragment newInstance() {
        VistaInicialEstudiantesFragment activity = new VistaInicialEstudiantesFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public VistaInicialEstudiantesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_vista_inicial_estudiantes, container, false);

        postulate = (Button) view.findViewById(R.id.btnPostularse);
        postulate.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        getFragmentManager().beginTransaction()
                .replace(R.id.inicial_container, InicialPostulacionesFragment.newInstance())
                .commit();
    }


}
