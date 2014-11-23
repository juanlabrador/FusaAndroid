package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import edu.ucla.fusa.android.R;

public class EventoCalendarioFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private CalendarView calendario;
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
        calendario = ((CalendarView)this.view.findViewById(R.id.calendar_view_calendario));
        calendario.setOnDateChangeListener(this);
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