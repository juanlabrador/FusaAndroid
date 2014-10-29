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

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 21/10/14.
 *
 * Clase encargada de manejar la información referente a la versión de la aplicación.
 *
 * Es un dialogo que se crea de forma emergente.
 *
 */
public class ConfiguracionAcercaVersionFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private View view;

    public ConfiguracionAcercaVersionFragment() {}

    /** Metodo de la clase FragmentDialog que instancia un dialogo */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        /** Cargamos un layout personalizado al dialogo */
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_configuraciones_acerca_version, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("Volver", this).show();
    }

    /** Metodo de la clase DialogInterface que al presionar el botón cierra el dialogo */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
