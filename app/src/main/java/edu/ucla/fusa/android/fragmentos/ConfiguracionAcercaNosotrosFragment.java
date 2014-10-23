package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import edu.ucla.fusa.android.actividades.R;

/**
 * Created by juanlabrador on 22/10/14.
 */
public class ConfiguracionAcercaNosotrosFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private View view;
    private AlertDialog.Builder dialog;


    public ConfiguracionAcercaNosotrosFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_configuraciones_acerca_nosotros, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Volver", this).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
