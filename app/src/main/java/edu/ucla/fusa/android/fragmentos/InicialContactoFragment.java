package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.ucla.fusa.android.R;

public class InicialContactoFragment extends Fragment implements View.OnClickListener {

    public static String TAG = "Contacto";
    private Button contacto;
    private TextView iniciarSesion;
    private View view;

    public static InicialContactoFragment newInstance() {
        InicialContactoFragment fragment = new InicialContactoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_contacto:
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, ContenedorContactoFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.tv_iniciar_sesion:
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, LoginFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public void onCreate(Bundle paramBundle)
    {
    super.onCreate(paramBundle);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_contacto, paramViewGroup, false);
        contacto = ((Button) view.findViewById(R.id.btn_contacto));
        iniciarSesion = ((TextView) view.findViewById(R.id.tv_iniciar_sesion));
        contacto.setOnClickListener(this);
        iniciarSesion.setOnClickListener(this);
        return view;
    }
}