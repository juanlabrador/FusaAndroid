package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RatingBar;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListComentario;
import java.util.ArrayList;
import java.util.Calendar;

public class EventoComentariosTodosFragment extends ListFragment implements AbsListView.OnScrollListener {

    //private ListComentariosAdapter adapter;
    private Calendar calendar;
    private View footer;
    private View header;
    private ArrayList<ItemListComentario> items = new ArrayList();
    private RatingBar ratingBar;

    public static EventoComentariosTodosFragment newInstance() {
        EventoComentariosTodosFragment fragment = new EventoComentariosTodosFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onActivityCreated(Bundle paramBundle) {
    super.onActivityCreated(paramBundle);
        header = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_header_comentarios_list_view, null, false);
        ratingBar = ((RatingBar) header.findViewById(R.id.rb_puntuacion_promedio));
        ratingBar.setRating(4.2F);
        footer = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_footer_comentarios_list_view, null, false);
        new CargarComentariosTask().execute();
        getListView().removeFooterView(this.footer);
        getListView().setOnScrollListener(this);
    }

    public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
        //getActivity().getActionBar().setIcon(R.drawable.ic_todos_comentarios_blanco);
        //getActivity().getActionBar().setTitle(R.string.contenido_titulo_tab_eventos_comentarios);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().getActionBar().setHomeButtonEnabled(false);
        calendar = Calendar.getInstance();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        return paramLayoutInflater.inflate(R.layout.fragment_evento_comentarios_todos, paramViewGroup, false);
    }

    public void onResume() {
    super.onResume();
    }

    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {}

    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
        int i = getListView().getCount();
        if ((paramInt == 0) && (getListView().getLastVisiblePosition() >= i - 1)) {
            getListView().addFooterView(this.footer);
            new PullMoreDataTask().execute();
        }
    }

    private class CargarComentariosTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... paramArrayOfVoid) {
            getListView().addHeaderView(EventoComentariosTodosFragment.this.header);
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Silvana Dorantes", 5,
                    calendar.getTime(),
                    "Excelente concierto, bravo por esos niños."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Miguel Nesterovsky", 4,
                    calendar.getTime(),
                    "Me gusto venir."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Jorge Aponte", 3,
                    calendar.getTime(),
                    "Bueno, hubo una que otra desafinación, pero excelente esfuerzo."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Luis \"Negro\" Castillo", 5,
                    calendar.getTime(),
                    "Quisiera ser niño otra vez."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Xioang Sanguino", 5,
                    calendar.getTime(),
                    "La orquesta infantil, dios mio, que bellos niños, con sus intrumentos."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Ana Ure", 5,
                    calendar.getTime(),
                    "Por estas iniciativas, es que vale la pena ayudar a la sociedad."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Winder Ojeda", 5,
                    calendar.getTime(),
                    "Pronto veré a mi hijo en un evento como este."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Jaime Daza", 1,
                    calendar.getTime(),
                    "No se para que vine, no me gusta estar aquí."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Alexis \"Pipo\" Colina", 3,
                    calendar.getTime(),
                    "Este tipo de música me desagrada, vine fue por mi novia."));
            items.add(new ItemListComentario(R.drawable.no_avatar,
                    "Angélica Morales", 5,
                    calendar.getTime(),
                    "Excelente TODO. Los niños, las canciones, quede fascinada."));
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            //adapter = new ListComentariosAdapter(getActivity(), items);
           // setListAdapter(adapter);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
        }
    }

    private class PullMoreDataTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000L);
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            getListView().removeFooterView(footer);
        }
    }
}