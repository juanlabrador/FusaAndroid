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

public class VistaInicialEventosFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button asistir;
    private Button contratar;

    public static VistaInicialEventosFragment newInstance() {
        VistaInicialEventosFragment activity = new VistaInicialEventosFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public VistaInicialEventosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_vista_inicial_eventos, container, false);

        asistir = (Button) view.findViewById(R.id.btnPresentaciones);
        asistir.setOnClickListener(this);

        contratar = (Button) view.findViewById(R.id.btnContrataciones);
        contratar.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContrataciones:
                getFragmentManager().beginTransaction()
                        .replace(R.id.inicial_container, InicialContratacionesFragment.newInstance())
                        .commit();
                break;
        }
    }
}
