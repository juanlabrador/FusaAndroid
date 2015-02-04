package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucla.fusa.android.R;

public class InicialNoticiasFragment extends Fragment implements View.OnClickListener {
    
    private Button noticias;
    private View mView;

    public static InicialNoticiasFragment newInstance() {
        InicialNoticiasFragment fragment = new InicialNoticiasFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View paramView) {
        getFragmentManager().beginTransaction()
            .replace(android.R.id.content, NoticiasLightFragment.newInstance())
            .addToBackStack(null)
            .commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        mView = inflater.inflate(R.layout.fragment_inicial_noticias, container, false);
        noticias = (Button) mView.findViewById(R.id.btnNoticias);
        noticias.setOnClickListener(this);
        return mView;
    }
}