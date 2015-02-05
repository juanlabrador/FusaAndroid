package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter;

/**
 * Created by juanlabrador on 26/01/15.
 */
public class ContenedorHorarioFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static String TAG = "ContenedorHorarioFragment";
    private FragmentViewPagerAdapter mAdapter;
    private PagerSlidingTabStrip mTabs;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private static final String[] mContentTabs = new String[] {"CATEDRAS" , "AGRUPACIÓN"};


    public static ContenedorHorarioFragment newInstance() {
        ContenedorHorarioFragment fragment = new ContenedorHorarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        return inflater.inflate(R.layout.fragment_contenedor_horario, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.mis_clases_action_bar);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_horario);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs_horarios);
        mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), mContentTabs);
        mAdapter.addFragment(HorarioClasesFragment.newInstance());
        mAdapter.addFragment(HorarioAgrupacionFragment.newInstance());
        mViewPager.setAdapter(mAdapter);
        mTabs.setViewPager(mViewPager);
        mTabs.setOnPageChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
