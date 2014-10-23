package edu.ucla.fusa.android.fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.actividades.R;

/**
 * Created by juanlabrador on 21/10/14.
 *
 * Clase que administra una vista de comentario acerca de un problema que tenga la aplicación
 * manejando un EditTex y un botón de acción.
 *
 */
public class ConfiguracionInformarProblemaFragment extends Fragment {

    private View view;

    public static ConfiguracionInformarProblemaFragment newInstance() {
        ConfiguracionInformarProblemaFragment activity = new ConfiguracionInformarProblemaFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ConfiguracionInformarProblemaFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.ic_informacion_problema_white);
        getActivity().getActionBar().setTitle(R.string.configuracion_ayuda_problema);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_configuraciones_informar_problema, container, false);

        return view;
    }
}
