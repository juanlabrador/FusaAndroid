package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import edu.ucla.fusa.android.R;

public class CalendarioFragment extends Fragment implements CalendarPickerView.OnDateSelectedListener {

    private CalendarPickerView calendario;
    private View view;
    private Calendar nextYear;
    private Calendar today;
    private ArrayList<String> titulos = new ArrayList<String>();
    private ArrayList<Date> fechas = new ArrayList<Date>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public static CalendarioFragment newInstance() {
        CalendarioFragment fragment = new CalendarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setTitle(R.string.calendario_titulo_barra);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_calendar, paramViewGroup, false);
        calendario = (CalendarPickerView) view.findViewById(R.id.calendario);
        calendario.setOnDateSelectedListener(this);
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        today = Calendar.getInstance();

        new EventosTaks().execute();
        return view;
    }

    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.calendario_titulo_barra);
    }

    /*public void onSelectedDayChange(CalendarView paramCalendarView, int paramInt1, int paramInt2, int paramInt3) {
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_container, EventoVistasFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }*/

    @Override
    public void onDateSelected(Date date) {
        String fechaCalendario = sdf.format(date);
        String fechaEvento;
        int contador = 0;
        for (int i = 0; i < fechas.size(); i++) {
            fechaEvento = sdf.format(fechas.get(i));
            if (fechaEvento.equals(fechaCalendario)) {
                contador++;
            }
        }
        if (contador > 1) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            //dialog.setIcon(R.drawable.ic_eventos);
            dialog.setTitle("Seleccione un evento");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.select_dialog_singlechoice);
            for (int i = 0; i < fechas.size(); i++) {
                fechaEvento = sdf.format(fechas.get(i));
                if (fechaEvento.equals(fechaCalendario)) {
                    arrayAdapter.add(titulos.get(i));
                }
            }
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), arrayAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else {
            for (int i = 0; i < fechas.size(); i++) {
                fechaEvento = sdf.format(fechas.get(i));
                if (fechaEvento.equals(fechaCalendario)) {
                    Toast.makeText(getActivity(), titulos.get(i), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDateUnselected(Date date) {

    }

    private class EventosTaks extends AsyncTask<Void, Void, Void> {

        Collection dates;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            calendario.setEmptyView(view.findViewById(R.id.progress_bar));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Calendar diciembre12 = Calendar.getInstance();
            diciembre12.set(2015, 02, 12);
            Calendar diciembre15 = Calendar.getInstance();
            diciembre15.set(2015, 02, 15);
            Calendar diciembre18 = Calendar.getInstance();
            diciembre18.set(2015, 02, 18);
            dates = new ArrayList();
            dates.add(diciembre12.getTime());
            dates.add(diciembre15.getTime());
            dates.add(diciembre18.getTime());
            titulos.clear();
            titulos.add(0, "Aniversario Empresas Polar");
            titulos.add(1, "Caida de Nicolas Maduro");
            titulos.add(0, "Venezuela es libre");
            titulos.add(1, "Adios comunismo");
            fechas.clear();
            fechas.add(0, diciembre12.getTime());
            fechas.add(1, diciembre12.getTime());
            fechas.add(2, diciembre15.getTime());
            fechas.add(3, diciembre18.getTime());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            calendario.init(today.getTime(), nextYear.getTime())
                    .withSelectedDate(today.getTime())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withHighlightedDates(dates);
            calendario.setEmptyView(view.findViewById(R.id.tv_empty_text));
        }
    }
}