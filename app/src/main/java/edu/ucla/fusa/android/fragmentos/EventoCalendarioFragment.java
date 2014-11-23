package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.ucla.fusa.android.R;

public class EventoCalendarioFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private CalendarPickerView calendario;
    private View view;

    public static EventoCalendarioFragment newInstance() {
        EventoCalendarioFragment fragment = new EventoCalendarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_calendario_blanco);
        getActivity().getActionBar().setTitle(R.string.contenido_calendario_action_bar_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_calendar, paramViewGroup, false);
        calendario = ((CalendarPickerView)this.view.findViewById(R.id.calendario));
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        Calendar diciembre12 = Calendar.getInstance();
        diciembre12.set(2014, 11, 12);
        Calendar diciembre15 = Calendar.getInstance();
        diciembre15.set(2014, 11, 15);
        Calendar diciembre18 = Calendar.getInstance();
        diciembre18.set(2014, 11, 18);
        Collection dates = new ArrayList();
        dates.add(diciembre12.getTime());
        dates.add(diciembre15.getTime());
        dates.add(diciembre18.getTime());
        calendario.init(today, nextYear.getTime())
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withHighlightedDates(dates);
        calendario.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Toast.makeText(getActivity(), "Dia: "+ calendario.getSelectedDate(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setIcon(R.drawable.ic_calendario_blanco);
        getActivity().getActionBar().setTitle(R.string.contenido_calendario_action_bar_titulo);
    }

    public void onSelectedDayChange(CalendarView paramCalendarView, int paramInt1, int paramInt2, int paramInt3) {
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_container, EventoVistasFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}