package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.asynctask.CargarDetalleNoticiaTaks;

public class DrawerNoticiasDetalleFragment extends Fragment implements View.OnClickListener {

    private Bundle arguments;
    private ImageView compartir;
    private TextView descripcion;
    private TextView fecha;
    private ImageView imagen;
    private TextView titulo;
    private View view;

    public static DrawerNoticiasDetalleFragment newInstance(Bundle paramBundle) {
        DrawerNoticiasDetalleFragment fragment = new DrawerNoticiasDetalleFragment();
        fragment.setRetainInstance(true);
        if (paramBundle != null)
            fragment.setArguments(paramBundle);
        return fragment;
    }

    public void onClick(View paramView) {}

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_noticia_detalle);
        getActivity().getActionBar().setTitle(R.string.contendio_noticia_detalle_action_bar_titulo);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_noticia_detalle, paramViewGroup, false);
        compartir = ((ImageView) view.findViewById(R.id.btn_compartir_noticia));
        compartir.setOnClickListener(this);
        new CargarDetalleNoticiaTaks(getArguments(), view).execute();
        return view;
    }

    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setIcon(R.drawable.ic_noticia_detalle);
        getActivity().getActionBar().setTitle(R.string.contendio_noticia_detalle_action_bar_titulo);
    }
}