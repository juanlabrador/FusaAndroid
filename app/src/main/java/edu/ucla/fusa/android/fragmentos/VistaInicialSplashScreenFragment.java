package edu.ucla.fusa.android.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.ucla.fusa.android.actividades.VistasAccesoActivity;
import edu.ucla.fusa.android.actividades.R;

/**
 * Created by juanlabrador on 16/10/14.
 */
public class VistaInicialSplashScreenFragment extends Fragment implements View.OnClickListener{

    private static long SPLASHDELAY = 3000;
    private ImageView logotipo;
    private TextView textoInformacion;
    private Animation animacion;
    private TextView tvIniciarSesion;
    private View view;

    private boolean status = false;

    //Instanciamos el fragment
    public static VistaInicialSplashScreenFragment newInstance() {
        VistaInicialSplashScreenFragment activity = new VistaInicialSplashScreenFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public VistaInicialSplashScreenFragment() {}

    //Metodo que infla la vista
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_vc_splashscreen, container, false);

        tvIniciarSesion = (TextView) view.findViewById(R.id.tvIniciarSesion);
        tvIniciarSesion.setOnClickListener(this);

        return view;
    }

    //Al estar creada la vista
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(status == false) {
            startAnimationLogo();
            startAnimationText();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                }
            };

            Timer timer = new Timer();
            timer.schedule(task, SPLASHDELAY);
            status = true;
        }

    }

    /*
    *
    * Metodos para la ejecución de las animaciones para el logo y el texto
    *
    *
    * */
    private void startAnimationLogo() {
        logotipo = (ImageView) getView().findViewById(R.id.logotipo);
        animacion = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_logo);
        logotipo.startAnimation(animacion);
    }

    private void startAnimationText() {
        textoInformacion = (TextView) getView().findViewById(R.id.textoInformacion);
        animacion = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_text);
        textoInformacion.startAnimation(animacion);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvIniciarSesion) {
            //Instanciamos una nueva actividad
            startActivity(new Intent(getActivity(), VistasAccesoActivity.class).putExtra("acceso", 0));
            getActivity().finish();  //Finalizamos la antigua actividad
        }
    }
}
