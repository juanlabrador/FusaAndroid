package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.actividades.R;

/**
* Created by juanlabrador on 16/10/14.
*
* Clase que administra la información presentada en una de las vistas iniciales del ViewPager
*
*/

public class VistaInicialEventosFragment extends Fragment {

    public static VistaInicialEventosFragment newInstance() {
        VistaInicialEventosFragment activity = new VistaInicialEventosFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public VistaInicialEventosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_vc_eventos, container, false);
    }
}
