package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucla.fusa.android.R;

public class InicialEstudiantesFragment extends Fragment
  implements View.OnClickListener
{
  public static String TAG = "Estudiantes";
  private Button postulate;
  private View view;

  public static InicialEstudiantesFragment newInstance() {
    InicialEstudiantesFragment localInicialEstudiantesFragment = new InicialEstudiantesFragment();
    localInicialEstudiantesFragment.setRetainInstance(true);
    return localInicialEstudiantesFragment;
  }

  public void onClick(View paramView) {
    getFragmentManager().beginTransaction()
            .replace(android.R.id.content, EstudianteAspiranteFragment.newInstance())
            .addToBackStack(null)
            .commit();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    this.view = paramLayoutInflater.inflate(R.layout.fragment_inicial_estudiantes, paramViewGroup, false);
    this.postulate = ((Button)this.view.findViewById(R.id.btnPostularse));
    this.postulate.setOnClickListener(this);
    return this.view;
  }
}