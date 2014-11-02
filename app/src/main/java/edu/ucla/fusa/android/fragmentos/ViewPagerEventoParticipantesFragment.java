package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 19/10/14.
 *
 * Clase que administra todos los elementos de la lista de configuración.
 *
 */
public class ViewPagerEventoParticipantesFragment extends Fragment {

    private View view;
    private ListView list;
    private ArrayAdapter<String> adapter;

    public static ViewPagerEventoParticipantesFragment newInstance() {
        ViewPagerEventoParticipantesFragment activity = new ViewPagerEventoParticipantesFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerEventoParticipantesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_viewpager_evento_participantes, container, false);
        list = (ListView) view.findViewById(R.id.lista_participantes_eventos);


        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray((R.array.participantes_evento)));

        list.setAdapter(adapter);

        return view;
    }
}
