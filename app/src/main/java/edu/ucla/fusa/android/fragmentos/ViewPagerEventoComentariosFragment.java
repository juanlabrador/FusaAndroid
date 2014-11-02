package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListComentariosAdapter;
import edu.ucla.fusa.android.modelo.ItemListComentario;

/**
 * Created by juanlabrador on 30/10/14.
 */
public class ViewPagerEventoComentariosFragment extends Fragment {

    private View view;
    private ListView list;
    private ArrayList<ItemListComentario> items;
    private ListComentariosAdapter adapter;
    private Calendar calendar;

    public static ViewPagerEventoComentariosFragment newInstance() {
        ViewPagerEventoComentariosFragment activity = new ViewPagerEventoComentariosFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerEventoComentariosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_viewpager_evento_comentarios, container, false);

        calendar = Calendar.getInstance();

        list = (ListView) view.findViewById(R.id.list_comentarios_evento);


        items = new ArrayList<ItemListComentario>();
        items.clear();
        items.add(new ItemListComentario(R.drawable.comentario1,
                "Silvana Dorantes",
                5,
                calendar.getTime(),
                "Excelente concierto, bravo por esos niños."));
        items.add(new ItemListComentario(R.drawable.comentario3,
                "Jorge Aponte",
                3,
                calendar.getTime(),
                "Bueno, hubo una que otra desafinación, pero excelente esfuerzo."));
        items.add(new ItemListComentario(R.drawable.comentario5,
                "Xioang Sanguino",
                5,
                calendar.getTime(),
                "La orquesta infantil, dios mio, que bellos niños, con sus intrumentos."));

        adapter = new ListComentariosAdapter(getActivity(), items);
        list.setAdapter(adapter);

        getFragmentManager().beginTransaction().
                replace(R.id.calificacion_container, ViewPagerEventoConCalificacionFragment.newInstance())
                .commit();

        return view;
    }
}
