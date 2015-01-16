package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucla.fusa.android.R;

public class InicialInstrumentosFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button donar;

    public static String TAG = "Instrumentos";

    public static InicialInstrumentosFragment newInstance() {
        InicialInstrumentosFragment fragment = new InicialInstrumentosFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_instrumentos, paramViewGroup, false);

        donar = (Button) view.findViewById(R.id.btn_contribuye);
        donar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(android.R.id.content, DonarInstrumentoFragment.newInstance())
                .commit();
    }
}