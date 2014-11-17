package edu.ucla.fusa.android.fragmentos;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import edu.ucla.fusa.android.R;

public class ConfiguracionAcercaVersionFragment extends DialogFragment implements DialogInterface.OnClickListener {

  private View view;

  public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    paramDialogInterface.cancel();
  }

  public Dialog onCreateDialog(Bundle paramBundle) {
    super.onCreateDialog(paramBundle);
    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_configuraciones_acerca_version, null);
    return new AlertDialog.Builder(getActivity())
            .setView(view)
            .setNegativeButton("Volver", this)
            .show();
  }
}
