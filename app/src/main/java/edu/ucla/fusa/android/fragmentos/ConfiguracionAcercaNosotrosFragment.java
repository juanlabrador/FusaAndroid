package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import edu.ucla.fusa.android.R;

/**
 * Created by juanlabrador on 22/10/14.
 *
 * Clase encargada de manejar la información referente del equipo de trabajo.
 *
 * Es un dialogo emergente que se instancia.
 *
 */
public class ConfiguracionAcercaNosotrosFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private View view;
    private AlertDialog.Builder dialog;


    public ConfiguracionAcercaNosotrosFragment() {}

    /**
     *
     * Como la clase hereda de FragmentDialog, este metodo es parte de la clase
     * para la creación de un dialogo.
     *
     * */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        /** Cargamos un layout personalizado al dialogo */
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_configuraciones_acerca_nosotros, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Volver", this).show();
    }

    /** Evento de la clase DialogInterface cuando presiona el botón se cierra el dialogo */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
