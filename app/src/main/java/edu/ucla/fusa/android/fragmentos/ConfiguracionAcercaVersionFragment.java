package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucla.fusa.android.actividades.R;

/**
 * Created by juanlabrador on 21/10/14.
 */
public class ConfiguracionAcercaVersionFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private View view;

    public ConfiguracionAcercaVersionFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_configuraciones_acerca_version, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("Volver", this).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
