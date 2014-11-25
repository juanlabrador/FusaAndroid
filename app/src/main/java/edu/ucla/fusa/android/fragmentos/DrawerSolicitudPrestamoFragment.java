package edu.ucla.fusa.android.fragmentos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.dd.CircularProgressButton;

import java.util.Calendar;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;

public class DrawerSolicitudPrestamoFragment extends Fragment implements View.OnClickListener {

    private Calendar calendar;
    private int day;
    private FloatingHintEditText desde;
    private CircularProgressButton enviar;
    private FloatingHintEditText hasta;
    private int month;
    private View view;
    private int year;

    public static DrawerSolicitudPrestamoFragment newInstance() {
        DrawerSolicitudPrestamoFragment fragment = new DrawerSolicitudPrestamoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.et_desde:
                new DatePickerDialog(getActivity(), datePickerListenerDesde, this.year, this.month, this.day).show();
                break;
            case R.id.et_hasta:
                new DatePickerDialog(getActivity(), datePickerListenerHasta, this.year, this.month, this.day).show();
                break;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_solicitud_prestamo_blanco);
        getActivity().getActionBar().setTitle(R.string.contenido_prestamo_instrumento_action_bar_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_solicitud_prestamo, paramViewGroup, false);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_enviar_solicitud_prestamo));
        desde = (FloatingHintEditText) view.findViewById(R.id.et_desde);
        hasta = (FloatingHintEditText) view.findViewById(R.id.et_hasta);
        enviar.setOnClickListener(this);
        desde.setOnClickListener(this);
        hasta.setOnClickListener(this);
        calendar = Calendar.getInstance();
        day =  calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        return view;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerDesde = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int year, int month, int day) {
            DrawerSolicitudPrestamoFragment.this.desde.setText(day + "/" + (month + 1) + "/" + year);
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerHasta = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker paramAnonymousDatePicker, int year, int month, int day) {
            DrawerSolicitudPrestamoFragment.this.hasta.setText(day + "/" + (month + 1) + "/" + year);
        }
    };
}
