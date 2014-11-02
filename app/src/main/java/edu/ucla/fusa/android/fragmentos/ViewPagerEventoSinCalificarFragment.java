package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 30/10/14.
 */
public class ViewPagerEventoSinCalificarFragment extends Fragment {

    private View view;

    public static ViewPagerEventoSinCalificarFragment newInstance() {
        ViewPagerEventoSinCalificarFragment activity = new ViewPagerEventoSinCalificarFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerEventoSinCalificarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.custom_layout_inicial_comentario, container, false);

        return view;
    }
}
