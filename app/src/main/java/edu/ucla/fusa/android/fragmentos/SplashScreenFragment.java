package edu.ucla.fusa.android.fragmentos;

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

import edu.ucla.fusa.android.R;
import me.relex.circleindicator.CircleIndicator;

public class SplashScreenFragment extends Fragment implements View.OnClickListener {

    private static long SPLASHDELAY = 3000L;
    public static String TAG = "SplashScreen";
    private Animation animacion;
    private ImageView logotipo;
    private boolean status = false;
    private CircleIndicator mCircleIndicator;
    private TextView textoInformacion;
    private TextView tvIniciarSesion;
    private View view;

    public static SplashScreenFragment newInstance() {
        SplashScreenFragment fragment = new SplashScreenFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    private void startAnimationBarraInferior() {
        mCircleIndicator = (CircleIndicator) getActivity().findViewById(R.id.indicador);
        animacion = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_barra_inferior);
        mCircleIndicator.startAnimation(animacion);
    }

    private void startAnimationLogo() {
        logotipo = ((ImageView)getView().findViewById(R.id.logotipo));
        animacion = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_logo);
        logotipo.startAnimation(animacion);
    }

    private void startAnimationText() {
        textoInformacion = ((TextView)getView().findViewById(R.id.textoInformacion));
        animacion = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_text);
        textoInformacion.startAnimation(animacion);
    }

    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        if (status != true) {
            startAnimationLogo();
            startAnimationText();
            startAnimationBarraInferior();
            TimerTask tarea = new TimerTask() {
                public void run() {}
            };
            new Timer().schedule(tarea, SPLASHDELAY);
            status = true;
        }
    }

    public void onClick(View paramView) {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, LoginFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_splash_screen, paramViewGroup, false);
        tvIniciarSesion = ((TextView) view.findViewById(R.id.tv_iniciar_sesion));
        tvIniciarSesion.setOnClickListener(this);
        return view;
    }
}
