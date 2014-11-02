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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 30/10/14.
 */
public class ViewPagerEventoConCalificacionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RatingBar calificacion;
    private TextView comentarioFinal;
    private ImageView editar;
    private RatingBar calificando;
    private AlertDialog dialog;
    private TextView descripcion;
    private EditText comentarioEditado;
    private int votacion;
    private String comentario;
    private Bundle arguments;
    private View dialogInterface;

    public static ViewPagerEventoConCalificacionFragment newInstance(Bundle arguments) {
        ViewPagerEventoConCalificacionFragment activity = new ViewPagerEventoConCalificacionFragment();
        activity.setRetainInstance(true);
        if (arguments != null) {
            activity.setArguments(arguments);
        }
        return activity;
    }

    public ViewPagerEventoConCalificacionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.custom_layout_comentario_con_calificacion, container, false);

        /** Los elementos de la ventana con la calificación realizada */
        calificacion = (RatingBar) view.findViewById(R.id.calificacion_evento);
        calificacion.setRating(getArguments().getInt("puntuacion"));
        votacion = (int) calificacion.getRating();

        comentarioFinal = (TextView) view.findViewById(R.id.tv_comentario_final);
        comentarioFinal.setText(getArguments().getString("comentario"));
        comentario = comentarioFinal.getText().toString();

        editar = (ImageView) view.findViewById(R.id.btn_editar_comentario);
        editar.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        /** Cargamos un layout personalizado al dialogo */
        dialogInterface = getActivity().getLayoutInflater().inflate(R.layout.custom_layout_comentario_intermedio, null);

        /** Los elementos de la vista emergente para editar el comentario */
        descripcion = (TextView) dialogInterface.findViewById(R.id.tv_descripcion_calificando);
        comentarioEditado = (EditText) dialogInterface.findViewById(R.id.et_comentario_final);
        comentarioEditado.setText(comentario);
        calificando = (RatingBar) dialogInterface.findViewById(R.id.calificando_evento);
        calificando.setRating(votacion);
        calificando.setIsIndicator(false);
        switch (votacion) {
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
                .setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arguments = new Bundle();

                        arguments.putInt("puntuacion", (int) calificando.getRating());
                        arguments.putString("comentario", comentarioEditado.getText().toString());

                        getFragmentManager().beginTransaction().
                                replace(R.id.calificacion_container, ViewPagerEventoConCalificacionFragment.newInstance(arguments))
                                .commit();
                    }
                }).show();
    }

}
