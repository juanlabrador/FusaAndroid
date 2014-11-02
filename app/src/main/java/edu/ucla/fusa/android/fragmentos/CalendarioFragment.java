package edu.ucla.fusa.android.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasEventoActivity;

/**
 * Created by juanlabrador on 19/10/14.
 *
 * Clase encargada de gestionar el calendario de información de interes, como clases, eventos, etc.
 *
 */
public class CalendarioFragment extends Fragment {

    private View view;
    private CalendarView calendario;

    /** Creamos el metodo que instancia el fragmento, basandose en el patrón de Singlenton */
    public static CalendarioFragment newInstance() {
        CalendarioFragment activity = new CalendarioFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    /** Constructor vacio requisito necesario para el fragment */
    public CalendarioFragment() {}

    /** Metodo de la clase Fragment, para inflar la vista en el activity */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_drawer_calendar, container, false);

        calendario = (CalendarView) view.findViewById(R.id.cvCalendario);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Toast.makeText(getActivity(), dayOfMonth +"/"+month+"/"+ year, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), VistasEventoActivity.class));
            }
        });

        return view;
    }

    /** Metodo de la clase Fragment, para acondicionar los elementos de interfaz */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.ic_calendario);
        getActivity().getActionBar().setTitle(R.string.contenido_calendario_action_bar_titulo);
    }
}
