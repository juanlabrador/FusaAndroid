package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.ucla.fusa.android.R;

public class EventoParticipantesFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private ListView list;
    private View view;

    public static EventoParticipantesFragment newInstance() {
        EventoParticipantesFragment fragment = new EventoParticipantesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(android.R.color.transparent);
        getActivity().getActionBar().setTitle(R.string.contenido_evento_participantes_tab_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_evento_participantes, paramViewGroup, false);
        list = ((ListView) view.findViewById(R.id.lista_participantes_eventos));
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.participantes_evento));
        list.setAdapter(this.adapter);
        return view;
    }

    public void onResume()
    {
    super.onResume();
    }
}
