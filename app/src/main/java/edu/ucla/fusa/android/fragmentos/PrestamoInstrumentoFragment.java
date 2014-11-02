package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 24/10/14.
 *
 * Clase encargada de manejar los datos referentes al prestamos asociado a un estudiante.
 */
public class PrestamoInstrumentoFragment extends Fragment {

    private View view;
    private TextView codigoInstrumento;
    private TextView tipoInstrumento;
    private TextView fechaVencimiento;
    private TextView cuentaRegresiva;

    private static int SECONDS_IN_A_DAY = 24 * 60 * 60;
    private Calendar thatDay;
    private Calendar today;

    public static PrestamoInstrumentoFragment newInstance() {
        PrestamoInstrumentoFragment activity = new PrestamoInstrumentoFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_prestamo_instrumento, container, false);

        cuentaRegresiva = (TextView) view.findViewById(R.id.tvCuentaRegresivaPrestamo);
        thatDay = Calendar.getInstance();
        thatDay.setTime(new Date(0));
        thatDay.set(Calendar.DAY_OF_MONTH, 31);
        thatDay.set(Calendar.MONTH, 9);
        thatDay.set(Calendar.YEAR, 2014);

        today = Calendar.getInstance();
        long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();
        long diffSec = diff / 1000;

        long days = diffSec / SECONDS_IN_A_DAY;
        long secondsDay = diffSec % SECONDS_IN_A_DAY;
        long seconds = secondsDay % 60;
        long minutes = (secondsDay / 60) % 60;
        long hours = (secondsDay / 3600);

        cuentaRegresiva.setText(days + " dias, " + hours + " horas, " +
                        minutes + " minutos y " + seconds + " segundos");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.ic_prestamos_instrumento);
        getActivity().getActionBar().setTitle(R.string.contenido_prestamo_instrumento_action_bar_titulo);
    }
}
