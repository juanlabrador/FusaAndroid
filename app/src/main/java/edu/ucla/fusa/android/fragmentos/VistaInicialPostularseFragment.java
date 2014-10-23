package edu.ucla.fusa.android.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.ucla.fusa.android.actividades.VistasAccesoActivity;
import edu.ucla.fusa.android.actividades.R;


/**
 * Created by juanlabrador on 16/10/14.
 *
 * Clase que administra la información presentada en una de las vistas iniciales del ViewPager
 *
 */

public class VistaInicialPostularseFragment extends Fragment implements View.OnClickListener {

    private Button postularse;
    private TextView iniciarSesion;
    private View view;

    public static VistaInicialPostularseFragment newInstance() {
        VistaInicialPostularseFragment activity = new VistaInicialPostularseFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public VistaInicialPostularseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_vc_postularse, container, false);
        postularse = (Button) view.findViewById(R.id.btnPostularse);
        iniciarSesion = (TextView) view.findViewById(R.id.tvIniciarSesion);

        postularse.setOnClickListener(this);
        iniciarSesion.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnPostularse) {
            startActivity(new Intent(getActivity(), VistasAccesoActivity.class).putExtra("acceso", 1));
            getActivity().finish();  /** Finalizamos la antigua actividad */
        } else if (v.getId() == R.id.tvIniciarSesion) {
            startActivity(new Intent(getActivity(), VistasAccesoActivity.class).putExtra("acceso", 0));
            getActivity().finish();  /** Finalizamos la antigua actividad */
        }
    }
}
