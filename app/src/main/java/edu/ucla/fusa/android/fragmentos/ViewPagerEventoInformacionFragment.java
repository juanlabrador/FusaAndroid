package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ViewPagerFragmentAdapter;

/**
 * Created by juanlabrador on 30/10/14.
 */
public class ViewPagerEventoInformacionFragment extends Fragment {

    private View view;

    public static ViewPagerEventoInformacionFragment newInstance() {
        ViewPagerEventoInformacionFragment activity = new ViewPagerEventoInformacionFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerEventoInformacionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_viewpager_evento_informacion, container, false);

        return view;
    }
}
