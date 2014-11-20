package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;

public class EventoConCalificacionFragment extends Fragment implements View.OnClickListener {

    private Bundle arguments;
    private RatingBar calificacion;
    private RatingBar calificando;
    private String comentario;
    private FloatingHintEditText comentarioEditado;
    private TextView comentarioFinal;
    private TextView descripcion;
    private AlertDialog dialog;
    private View dialogInterface;
    private ImageView editar;
    private View view;
    private int votacion;

    public static EventoConCalificacionFragment newInstance(Bundle paramBundle) {
        EventoConCalificacionFragment fragment = new EventoConCalificacionFragment();
        fragment.setRetainInstance(true);
        if (paramBundle != null)
            fragment.setArguments(paramBundle);
        return fragment;
    }

    public void onClick(View paramView) {
        dialogInterface = getActivity().getLayoutInflater().inflate(R.layout.custom_layout_comentario_intermedio, null);
        descripcion = ((TextView) dialogInterface.findViewById(R.id.tv_descripcion_calificando));
        comentarioEditado = ((FloatingHintEditText) dialogInterface.findViewById(R.id.et_comentario_final));
        comentarioEditado.setText(comentario);
        calificando = ((RatingBar) dialogInterface.findViewById(R.id.calificando_evento));
        calificando.setRating(votacion);
        calificando.setIsIndicator(false);
        switch (votacion) {
            default:
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
                    default:
                        break;
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
            }
        });
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogInterface).setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
            }
        }).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                  arguments = new Bundle();
                  arguments.putInt("puntuacion", (int) calificando.getRating());
                  arguments.putString("comentario", comentarioEditado.getText().toString());
                  getFragmentManager().beginTransaction()
                          .replace(R.id.calificacion_container, EventoConCalificacionFragment.newInstance(arguments))
                          .commit();
            }
        }).show();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.custom_layout_comentario_con_calificacion, paramViewGroup, false);
        calificacion = ((RatingBar) view.findViewById(R.id.calificacion_evento));
        calificacion.setRating(getArguments().getInt("puntuacion"));
        votacion = ((int) calificacion.getRating());
        comentarioFinal = ((TextView) view.findViewById(R.id.tv_comentario_final));
        comentarioFinal.setText(getArguments().getString("comentario"));
        comentario =  comentarioFinal.getText().toString();
        editar = ((ImageView) view.findViewById(R.id.btn_editar_comentario));
        editar.setOnClickListener(this);
        return view;
    }
}