package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter;


public class ContenedorContactoFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static String TAG = "ContenedorContactoFragment";
    private FragmentViewPagerAdapter mAdapter;
    private PagerSlidingTabStrip mTabs;
    private Toolbar mToolbar;
    private View mView;
    private ViewPager mViewPager;
    private static final String[] mContentTabs = new String[] {"Información", "Ubicación", "fff"};
    private DrawerArrowDrawable mDrawerArrow;

    public static ContenedorContactoFragment newInstance() {
        ContenedorContactoFragment fragment = new ContenedorContactoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        
        mView = inflater.inflate(R.layout.fragment_inicial_contenedor_contacto, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager_contacto);
        
        mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), mContentTabs);
        mAdapter.addFragment(DatosFundacionFragment.newInstance());
        mAdapter.addFragment(MapaFundacionFragment.newInstance());

        mViewPager.setAdapter(mAdapter);

        mTabs = (PagerSlidingTabStrip) mView.findViewById(R.id.tabs);
        mTabs.setViewPager(mViewPager);
        mTabs.setOnPageChangeListener(this);
       
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDrawerArrow = new DrawerArrowDrawable(getActivity()) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerArrow.setProgress(1f);
        
        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.contacto_titulo_barra);
        mToolbar.setNavigationIcon(mDrawerArrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
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