package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.R;

public class InicialInstrumentosFragment extends Fragment {

    public static String TAG = "Instrumentos";

    public static InicialInstrumentosFragment newInstance() {
        InicialInstrumentosFragment fragment = new InicialInstrumentosFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        return paramLayoutInflater.inflate(R.layout.fragment_inicial_instrumentos, paramViewGroup, false);
    }
}