package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 30/10/14.
 */
public class ViewPagerEventoSinCalificacionFragment extends Fragment implements RatingBar.OnRatingBarChangeListener, DialogInterface.OnClickListener {

    private View view;
    private View dialogInterface;
    private RatingBar sinCalificacion;
    private RatingBar calificando;
    private AlertDialog dialog;
    private TextView descripcion;
    private EditText comentarioFinal;
    private int votacion;
    private String comentario;
    private Bundle arguments;

    public static ViewPagerEventoSinCalificacionFragment newInstance() {
        ViewPagerEventoSinCalificacionFragment activity = new ViewPagerEventoSinCalificacionFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ViewPagerEventoSinCalificacionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.custom_layout_comentario_sin_calificacion, container, false);


        sinCalificacion = (RatingBar) view.findViewById(R.id.calificar_evento);
        sinCalificacion.setOnRatingBarChangeListener(this);
        return view;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        /** Cargamos un layout personalizado al dialogo */
        dialogInterface = getActivity().getLayoutInflater().inflate(R.layout.custom_layout_comentario_intermedio, null);
        descripcion = (TextView) dialogInterface.findViewById(R.id.tv_descripcion_calificando);
        comentarioFinal = (EditText) dialogInterface.findViewById(R.id.et_comentario_final);
        calificando = (RatingBar) dialogInterface.findViewById(R.id.calificando_evento);
        calificando.setRating(Math.round(rating));
        calificando.setIsIndicator(false);
        switch (Math.round(rating)) {
            case 1:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_1));
                //descripcion.setTextColor(getResources().getColor(R.color.nivel_1));
                break;
            case 2:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_2));
                //descripcion.setTextColor(getResources().getColor(R.color.nivel_2));
                break;
            case 3:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_3));
                //descripcion.setTextColor(getResources().getColor(R.color.nivel_3));
                break;
            case 4:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_4));
                //descripcion.setTextColor(getResources().getColor(R.color.nivel_4));
                break;
            case 5:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_5));
                //descripcion.setTextColor(getResources().getColor(R.color.nivel_5));
                break;
        }

        calificando.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int)rating) {
                    case 1:
                        descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_1));
                        //descripcion.setTextColor(getResources().getColor(R.color.nivel_1));
                        break;
                    case 2:
                        descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_2));
                        //descripcion.setTextColor(getResources().getColor(R.color.nivel_2));
                        break;
                    case 3:
                        descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_3));
                        //descripcion.setTextColor(getResources().getColor(R.color.nivel_3));
                        break;
                    case 4:
                        descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_4));
                        //descripcion.setTextColor(getResources().getColor(R.color.nivel_4));
                        break;
                    case 5:
                        descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_5));
                        //descripcion.setTextColor(getResources().getColor(R.color.nivel_5));
                        break;
                }
            }
        });

        dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogInterface)
                .setPositiveButton("Enviar", this).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        arguments = new Bundle();
        arguments.putInt("puntuacion", (int) calificando.getRating());
        arguments.putString("comentario", comentarioFinal.getText().toString());
        getFragmentManager().beginTransaction().
                replace(R.id.calificacion_container, ViewPagerEventoConCalificacionFragment.newInstance(arguments))
                .commit();
    }
}
