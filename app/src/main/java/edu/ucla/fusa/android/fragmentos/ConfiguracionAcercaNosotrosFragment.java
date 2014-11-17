package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import edu.ucla.fusa.android.R;

public class ConfiguracionAcercaNosotrosFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private View view;

  public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    paramDialogInterface.cancel();
  }

  public Dialog onCreateDialog(Bundle paramBundle) {
    super.onCreateDialog(paramBundle);
    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_configuraciones_acerca_nosotros, null);
    return new AlertDialog.Builder(getActivity())
            .setView(view)
            .setPositiveButton("Volver", this)
            .show();
  }
}
