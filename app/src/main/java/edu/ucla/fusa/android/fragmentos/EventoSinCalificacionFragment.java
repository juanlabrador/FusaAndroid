package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.FloatingHintEditText;

public class EventoSinCalificacionFragment extends Fragment implements RatingBar.OnRatingBarChangeListener, DialogInterface.OnClickListener {
    private Bundle arguments;
    private RatingBar calificando;
    private String comentario;
    private FloatingHintEditText comentarioFinal;
    private TextView descripcion;
    private AlertDialog dialog;
    private View dialogInterface;
    private RatingBar sinCalificacion;
    private View view;
    private int votacion;

    public static EventoSinCalificacionFragment newInstance() {
        EventoSinCalificacionFragment fragment = new EventoSinCalificacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
        arguments = new Bundle();
        arguments.putInt("puntuacion", (int) calificando.getRating());
        arguments.putString("comentario", comentarioFinal.getText().toString());
        getFragmentManager().beginTransaction()
                .replace(R.id.calificacion_container, EventoConCalificacionFragment.newInstance(arguments))
                .commit();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.custom_layout_comentario_sin_calificacion, paramViewGroup, false);
        sinCalificacion = ((RatingBar) view.findViewById(R.id.calificar_evento));
        sinCalificacion.setOnRatingBarChangeListener(this);
        return view;
    }

    public void onRatingChanged(RatingBar paramRatingBar, float paramFloat, boolean paramBoolean) {

        dialogInterface = getActivity().getLayoutInflater().inflate(R.layout.custom_layout_comentario_intermedio, null);
        descripcion = ((TextView) dialogInterface.findViewById(R.id.tv_descripcion_calificando));
        comentarioFinal = ((FloatingHintEditText) dialogInterface.findViewById(R.id.et_comentario_final));
        calificando = ((RatingBar) dialogInterface.findViewById(R.id.calificando_evento));
        calificando.setRating(Math.round(paramFloat));
        calificando.setIsIndicator(false);
        switch (Math.round(paramFloat)) {
            case 1:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_1));
                break;
            case 2:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_2));
                break;
            case 3:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_3));
                break;
            case 4:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_4));
                break;
            case 5:
                descripcion.setText(getResources().getString(R.string.nivel_calificacion_evento_5));
                break;
        }

        calificando.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar paramAnonymousRatingBar, float paramAnonymousFloat, boolean paramAnonymousBoolean) {
                switch ((int)paramAnonymousFloat) {
                    case 1:
                        descripcion.setText(EventoSinCalificacionFragment.this.getResources().getString(R.string.nivel_calificacion_evento_1));
                        return;
                    case 2:
                        descripcion.setText(EventoSinCalificacionFragment.this.getResources().getString(R.string.nivel_calificacion_evento_2));
                        return;
                    case 3:
                        descripcion.setText(EventoSinCalificacionFragment.this.getResources().getString(R.string.nivel_calificacion_evento_3));
                        return;
                    case 4:
                        descripcion.setText(EventoSinCalificacionFragment.this.getResources().getString(R.string.nivel_calificacion_evento_4));
                        return;
                    case 5:
                        descripcion.setText(EventoSinCalificacionFragment.this.getResources().getString(R.string.nivel_calificacion_evento_5));
                        break;
                }

            }
        });
    dialog = new AlertDialog.Builder(getActivity()).setView(dialogInterface).setPositiveButton("Enviar", this).show();
    }
}