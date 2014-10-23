package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.actividades.R;

public class VistaInicialInstrumentosFragment extends Fragment {

    public static VistaInicialInstrumentosFragment newInstance() {
        VistaInicialInstrumentosFragment activity = new VistaInicialInstrumentosFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public VistaInicialInstrumentosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_vc_instrumentos, container, false);
    }
}
