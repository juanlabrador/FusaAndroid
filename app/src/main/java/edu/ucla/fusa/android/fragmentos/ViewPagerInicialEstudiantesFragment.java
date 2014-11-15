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

public class ViewPagerInicialEstudiantesFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button postulate;

    public static ViewPagerInicialEstudiantesFragment newInstance() {
        ViewPagerInicialEstudiantesFragment fragment = new ViewPagerInicialEstudiantesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public ViewPagerInicialEstudiantesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_viewpager_inicial_estudiantes, container, false);

        postulate = (Button) view.findViewById(R.id.btn_postulate);
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
