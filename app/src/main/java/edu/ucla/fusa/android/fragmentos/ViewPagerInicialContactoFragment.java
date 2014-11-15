package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.ucla.fusa.android.R;


/**
 * Created by juanlabrador on 16/10/14.
 *
 * Clase que administra la información presentada en una de las vistas iniciales del ViewPager
 *
 */

public class ViewPagerInicialContactoFragment extends Fragment implements View.OnClickListener {

    private Button contacto;
    private TextView iniciarSesion;
    private View view;

    public static ViewPagerInicialContactoFragment newInstance() {
        ViewPagerInicialContactoFragment activity = new ViewPagerInicialContactoFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerInicialContactoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_viewpager_inicial_contacto, container, false);
        contacto = (Button) view.findViewById(R.id.btn_contacto);
        iniciarSesion = (TextView) view.findViewById(R.id.tv_iniciar_sesion);

        contacto.setOnClickListener(this);
        iniciarSesion.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_contacto) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, InicialViewPagerContactoFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        } else if (v.getId() == R.id.tv_iniciar_sesion) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, InicialLoginFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
    }

}
