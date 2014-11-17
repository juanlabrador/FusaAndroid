package edu.ucla.fusa.android.fragmentos;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter;

public class InicialContactoFundacionFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private FragmentViewPagerAdapter adapter;
    private TypedArray iconos;
    private TabPageIndicator tabPageIndicator;
    private View view;
    private ViewPager viewPager;

    public static InicialContactoFundacionFragment newInstance() {
        InicialContactoFundacionFragment fragment = new InicialContactoFundacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_contacto_fundacion, paramViewGroup, false);
        iconos = getResources().obtainTypedArray(R.array.icons_contacto);
        viewPager = ((ViewPager) view.findViewById(R.id.view_pager_contacto));
        adapter = new FragmentViewPagerAdapter(getFragmentManager(), iconos);
        adapter.addFragment(InicialContactoFundacionDatosFragment.newInstance());
        adapter.addFragment(InicialContactoFundacionMapaFragment.newInstance());
        viewPager.setAdapter(adapter);
        tabPageIndicator = ((TabPageIndicator) view.findViewById(R.id.tab_contacto));
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(this);
        return view;
    }

    public void onPageScrollStateChanged(int paramInt) {}
    
    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}

    public void onPageSelected(int paramInt) {
        switch (paramInt) {
            case 0:
                ((TextView) getActivity().findViewById(R.id.tv_titulo_superior)).setText(R.string.titulo_tab_datos_contacto);
                break;
            case 1:
                ((TextView) getActivity().findViewById(R.id.tv_titulo_superior)).setText(R.string.titulo_tab_ubicacion_contacto);
                break;
        }
    }
}