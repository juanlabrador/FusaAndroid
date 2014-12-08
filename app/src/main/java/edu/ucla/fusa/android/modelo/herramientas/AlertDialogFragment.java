package edu.ucla.fusa.android.modelo.herramientas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by juanlabrador on 08/12/14.
 */
public class AlertDialogFragment extends DialogFragment {

     static final String PARAM_TITLE = "AlertDialogFragment.PARAM_TITLE";
    private static final String PARAM_MESSAGE = "AlertDialogFragment.PARAM_MESSAGE";

    public static AlertDialogFragment newInstance(String title, String message) {
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putString(PARAM_MESSAGE, message);
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setArguments(args);
        return alertDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final String paramTitle = args.getString(PARAM_TITLE);
        final String paramMessage = args.getString(PARAM_MESSAGE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(paramTitle);
        builder.setMessage(paramMessage);
        builder.setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }
}