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
* Clase que administra la información presentada en una de las vistas iniciales del ViewPager,
 * aqui mandamos a llamar las vistas de realizar una contratacion o asistir a un concierto.
*
*/

public class ViewPagerInicialEventosFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button contratar;

    public static ViewPagerInicialEventosFragment newInstance() {
        ViewPagerInicialEventosFragment activity = new ViewPagerInicialEventosFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerInicialEventosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_viewpager_inicial_eventos, container, false);

        contratar = (Button) view.findViewById(R.id.btn_contrataciones);
        contratar.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, InicialContratacionesFragment.newInstance())
                .commit();

    }
}
