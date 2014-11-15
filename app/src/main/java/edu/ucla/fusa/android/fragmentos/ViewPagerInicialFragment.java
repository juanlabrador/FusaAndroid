package edu.ucla.fusa.android.fragmentos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;

/**
 * Created by juanlabrador on 16/10/14.
 *
 * Esta clase administra todas las vista deslizables iniciales, donde se muestra información
 * de la Fundación, y manejamos dos opciones de iniciación, logueandose o postulandose con
 * Fundamusical.
 *
 */
public class ViewPagerInicialFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerFragmentAdapter adapter;
    private TabPageIndicator tabPageIndicator;
    private View view;
    private int[] icons = new int[] {
            R.drawable.ic_cerrar_sesion,
            R.drawable.ic_estudiantes,
            R.drawable.ic_profesores,
            R.drawable.ic_instrumentos,
            R.drawable.ic_eventos,
            R.drawable.ic_contacto
    };

    public static ViewPagerInicialFragment newInstance(Bundle arguments) {
        ViewPagerInicialFragment activity = new ViewPagerInicialFragment();
        activity.setRetainInstance(true);
        if (arguments != null) {
            activity.setArguments(arguments);
        }
        return activity;
    }

    public ViewPagerInicialFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);




        viewPager.setCurrentItem(getArguments().getInt("position"));
        return view;
    }

}
