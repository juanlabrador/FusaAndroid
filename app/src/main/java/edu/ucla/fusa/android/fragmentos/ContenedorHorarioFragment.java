package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

import edu.ucla.fusa.android.DB.AgrupacionTable;
import edu.ucla.fusa.android.DB.ClaseParticularTable;
import edu.ucla.fusa.android.DB.HorarioAreaTable;
import edu.ucla.fusa.android.DB.HorarioTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter;
import edu.ucla.fusa.android.modelo.academico.Agrupacion;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;
import edu.ucla.fusa.android.modelo.academico.Dia;
import edu.ucla.fusa.android.modelo.academico.Horario;
import edu.ucla.fusa.android.modelo.academico.HorarioArea;
import edu.ucla.fusa.android.modelo.academico.Instructor;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by juanlabrador on 26/01/15.
 */
public class ContenedorHorarioFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static String TAG = "ContenedorHorarioFragment";
    private FragmentViewPagerAdapter mAdapter;
    private PagerSlidingTabStrip mTabs;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private static final String[] mContentTabs = new String[] {"LUN", "MA", "MIE", "JU", "VI", "SAB"};
    private AgrupacionTable mAgrupacionTable;
    private ClaseParticularTable mClaseParticularTable;
    private CircularProgressBar mProgressBar;
    private List<ClaseParticular> mClasesParticulares;
    private Agrupacion mAgrupacion;
    private Bundle mDia;

    public static ContenedorHorarioFragment newInstance() {
        ContenedorHorarioFragment fragment = new ContenedorHorarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgrupacionTable = new AgrupacionTable(getActivity());
        mClaseParticularTable = new ClaseParticularTable(getActivity());
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
        mToolbar.setVisibility(View.GONE);
        mProgressBar = (CircularProgressBar) view.findViewById(R.id.pb_cargando_horario);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_horario);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs_dias);
        mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), mContentTabs);

        new LoadingHorario().execute();
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
    
    private class LoadingHorario extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mAgrupacion = mAgrupacionTable.searchAgrupacion();
            mClasesParticulares = mClaseParticularTable.searchClases();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            mAdapter.addFragment(new HorarioClasesFragment(mAgrupacion, mClasesParticulares, 1));
            mAdapter.addFragment(new HorarioClasesFragment(mAgrupacion, mClasesParticulares, 2));
            mAdapter.addFragment(new HorarioClasesFragment(mAgrupacion, mClasesParticulares, 3));
            mAdapter.addFragment(new HorarioClasesFragment(mAgrupacion, mClasesParticulares, 4));
            mAdapter.addFragment(new HorarioClasesFragment(mAgrupacion, mClasesParticulares, 5));
            mAdapter.addFragment(new HorarioClasesFragment(mAgrupacion, mClasesParticulares, 6));
            mViewPager.setAdapter(mAdapter);
            mTabs.setViewPager(mViewPager);
            mTabs.setOnPageChangeListener(ContenedorHorarioFragment.this);
            mProgressBar.setVisibility(View.GONE);
            mToolbar.setVisibility(View.VISIBLE);
            mTabs.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
        }
    }
}
