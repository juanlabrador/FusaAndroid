package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucla.fusa.android.R;

public class InicialEventosFragment extends Fragment implements View.OnClickListener {

    public static String TAG = "InicialEventosFragment";
    private Button contratar;
    private View view;

    public static InicialEventosFragment newInstance() {
        InicialEventosFragment fragment = new InicialEventosFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_contratar:
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, SolicitanteExternoFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_eventos, paramViewGroup, false);
        contratar = ((Button) view.findViewById(R.id.btn_contratar));
        contratar.setOnClickListener(this);
        return view;
    }
}
