package edu.ucla.fusa.android.fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.actividades.R;

/**
 * Created by juanlabrador on 23/10/14.
 */
public class PerfilFragment extends Fragment {

    private View view;

    public static PerfilFragment newInstance() {
        PerfilFragment activity = new PerfilFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public PerfilFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_drawer_perfil, container, false);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.ic_perfil);
        getActivity().getActionBar().setTitle(R.string.contenido_perfil_action_bar_titulo);
    }
}
