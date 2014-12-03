package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListComentariosAdapter;
import edu.ucla.fusa.android.modelo.herramientas.ItemListComentario;
import java.util.ArrayList;
import java.util.Calendar;

public class EventoComentariosFragment extends Fragment implements View.OnClickListener {

    private ListComentariosAdapter adapter;
    private Calendar calendar;
    private ImageView comentarios;
    private View header;
    private ArrayList<ItemListComentario> items = new ArrayList();
    private ListView list;
    private RatingBar ratingBar;
    private View view;

    public static EventoComentariosFragment newInstance() {
        EventoComentariosFragment fragment = new EventoComentariosFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View paramView) {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, EventoComentariosTodosFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_comentarios_blanco);
        getActivity().getActionBar().setTitle(R.string.contenido_evento_comentarios_tab_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_evento_comentarios, paramViewGroup, false);
        calendar = Calendar.getInstance();
        list = ((ListView) view.findViewById(R.id.list_comentarios_evento));
        header = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_header_comentarios_iniciales, null, false);
        ratingBar = ((RatingBar) header.findViewById(R.id.rb_puntuacion_promedio));
        ratingBar.setRating(4.2F);
        comentarios = ((ImageView) header.findViewById(R.id.btn_ver_todos_comentarios));
        comentarios.setOnClickListener(this);
        getFragmentManager().beginTransaction()
                .replace(R.id.calificacion_container, EventoSinCalificacionFragment.newInstance())
                .commit();
        new CargarComentariosTask().execute();
        return this.view;
    }

    public void onResume() {
        super.onResume();
    }

    private class CargarComentariosTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            list.addHeaderView(header);
            items.add(new ItemListComentario(R.drawable.comentario1,
                    "Silvana Dorantes", 5,
                    calendar.getTime(),
                    "Excelente concierto, bravo por esos niños."));
            items.add(new ItemListComentario(R.drawable.comentario3,
                    "Jorge Aponte", 3,
                    calendar.getTime(),
                    "Bueno, hubo una que otra desafinación, pero excelente esfuerzo."));
            items.add(new ItemListComentario(R.drawable.comentario5,
                    "Xioang Sanguino", 5, calendar.getTime(),
                    "La orquesta infantil, dios mio, que bellos niños, con sus intrumentos."));
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            adapter = new ListComentariosAdapter(getActivity(), items);
            list.setAdapter(adapter);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
        }
    }
}
