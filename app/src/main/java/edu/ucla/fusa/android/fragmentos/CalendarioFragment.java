package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import me.drakeet.materialdialog.MaterialDialog;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ucla.fusa.android.DB.EventoTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.evento.Evento;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class CalendarioFragment extends Fragment implements CalendarPickerView.OnDateSelectedListener {

    private static String TAG = "CalendarioFragment";
    private CalendarPickerView mCalendario;
    private View mView;
    private Calendar mProximoAño;
    private Calendar mDiaActual;
    private ArrayList<Evento> mEventos;
    private ArrayList<Date> mFechas;
    private ArrayList<Integer> mIds;
    private SimpleDateFormat mDateFormat;
    private EventoTable mEventoTable;
    private Toolbar mToolbar;
    private CircularProgressBar mLoading;
    private ArrayAdapter<String> mAdapter;
    private ListView mList;

    public static CalendarioFragment newInstance() {
        CalendarioFragment fragment = new CalendarioFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mEventoTable = new EventoTable(getActivity());
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.calendario_titulo_barra);
        mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mFechas = new ArrayList<>();
        mIds = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        mView = paramLayoutInflater.inflate(R.layout.fragment_drawer_calendar, paramViewGroup, false);
        mCalendario = (CalendarPickerView) mView.findViewById(R.id.calendario);
        mCalendario.setOnDateSelectedListener(this);
        mProximoAño = Calendar.getInstance();
        mProximoAño.add(Calendar.YEAR, 1);
        mDiaActual = Calendar.getInstance();
        mLoading = (CircularProgressBar) mView.findViewById(R.id.pb_cargando_calendario);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new LoadingEventos().execute();
    }

    @Override
    public void onDateSelected(Date date) {
        String mFechaCalendario = mDateFormat.format(date);
        String mFechaEvento;
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        final MaterialDialog mDialog = new MaterialDialog(getActivity());

        for (int i = 0; i < mFechas.size(); i++) {
            mFechaEvento = mDateFormat.format(mFechas.get(i));
            if (mFechaEvento.equals(mFechaCalendario)) {
                mAdapter.add(mEventos.get(i).getNombre());
                mIds.add(mEventos.get(i).getId());
            }
        }
        if (mAdapter.getCount() > 1) {
            Log.i(TAG, "¡Hay más de un evento!");
            mList = new ListView(getActivity());
            mList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8 * scale + 0.5f);
            mList.setPadding(0, dpAsPixels, 0, dpAsPixels);
            mList.setDividerHeight(0);
            mList.setAdapter(mAdapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    getFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, EventoFragment.newInstance(mIds.get(i)))
                            .commit();
                    mDialog.dismiss();
                    Log.i(TAG, "¡Selecciono el evento " + i + " de la lista");
                }
            });
            
            mDialog.setTitle("Seleccione un evento")
                   .setContentView(mList)
                    .setNegativeButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                        }
                    }).show();

        } else if (mAdapter.getCount() == 1){
            Log.i(TAG, "¡Hay solo un evento!");
            getFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame_container, EventoFragment.newInstance(mIds.get(0)))
                    .commit();
        } else {
            Log.i(TAG, "¡No hay eventos en ese dia!");
        }
    }

    @Override
    public void onDateUnselected(Date date) {

    }
    
    private class LoadingEventos extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            mEventos = mEventoTable.searchEventos();
            if (mEventos != null) {
                Log.i(TAG, "¡Hay eventos!");
                for(Evento mEvento : mEventos) {
                    mFechas.add(mEvento.getFecha());
                }
                return 100;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    mCalendario.init(mDiaActual.getTime(), mProximoAño.getTime())
                            .withSelectedDate(mDiaActual.getTime())
                            .inMode(CalendarPickerView.SelectionMode.SINGLE)
                            .withHighlightedDates(mFechas);
                    break;
                case -1:
                    Log.i(TAG, "No hay eventos!");
                    mCalendario.init(mDiaActual.getTime(), mProximoAño.getTime())
                            .withSelectedDate(mDiaActual.getTime());
                    break;
            }
            mLoading.setVisibility(View.GONE);
            mCalendario.setVisibility(View.VISIBLE);
        }
    }
}