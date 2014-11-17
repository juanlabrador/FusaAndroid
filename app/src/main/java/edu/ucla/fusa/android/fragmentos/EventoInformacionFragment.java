package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.R;

public class EventoInformacionFragment extends Fragment {

    private View view;

    public static EventoInformacionFragment newInstance() {
        EventoInformacionFragment fragment = new EventoInformacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_datos);
        getActivity().getActionBar().setTitle(R.string.contenido_evento_informacion_tab_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_evento_informacion, paramViewGroup, false);
        return view;
    }

    public void onResume() {
        super.onResume();
    }
}
