package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucla.fusa.android.R;

public class InicialProfesoresFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button participa;
    public static String TAG = "Profesores";

    public static InicialProfesoresFragment newInstance() {
        InicialProfesoresFragment fragment = new InicialProfesoresFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_profesores, paramViewGroup, false);

        participa = (Button) view.findViewById(R.id.btnParticipa);
        participa.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(android.R.id.content, InstructorAspiranteFragment.newInstance())
                .commit();
    }
}
