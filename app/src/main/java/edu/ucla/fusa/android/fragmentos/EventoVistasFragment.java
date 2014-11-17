package edu.ucla.fusa.android.fragmentos;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.viewpagerindicator.TabPageIndicator;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.FragmentViewPagerAdapter;

public class EventoVistasFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private FragmentViewPagerAdapter adapter;
    private TypedArray iconos;
    private TabPageIndicator tabPageIndicator;
    private View view;
    private ViewPager viewPager;

    public static EventoVistasFragment newInstance() {
        EventoVistasFragment fragment = new EventoVistasFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_evento, paramViewGroup, false);
        iconos = getResources().obtainTypedArray(R.array.icons_eventos);
        viewPager = ((ViewPager) view.findViewById(R.id.view_pager_eventos));
        adapter = new FragmentViewPagerAdapter(getFragmentManager(), iconos);
        adapter.addFragment(EventoInformacionFragment.newInstance());
        adapter.addFragment(EventoMapaFragment.newInstance());
        adapter.addFragment(EventoParticipantesFragment.newInstance());
        adapter.addFragment(EventoComentariosFragment.newInstance());
        viewPager.setAdapter(adapter);
        tabPageIndicator = ((TabPageIndicator)this.view.findViewById(R.id.tab_eventos));
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(this);
        getActivity().getActionBar().setTitle(R.string.contenido_evento_informacion_tab_titulo);
        getActivity().getActionBar().setIcon(android.R.color.transparent);
        return view;
    }

    public void onPageScrollStateChanged(int paramInt) {}

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}

    public void onPageSelected(int paramInt) {
        switch (paramInt) {
        case 0:
            getActivity().getActionBar().setTitle(R.string.contenido_evento_informacion_tab_titulo);
              return;
        case 1:
            getActivity().getActionBar().setTitle(R.string.contenido_evento_ubicacion_tab_titulo);
            return;
        case 2:
            getActivity().getActionBar().setTitle(R.string.contenido_evento_participantes_tab_titulo);
            return;
        case 3:
            getActivity().getActionBar().setTitle(R.string.contenido_evento_comentarios_tab_titulo);
        }
    }

    public void onResume() {
    super.onResume();
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
    }
}
