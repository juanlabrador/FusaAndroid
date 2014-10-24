package edu.ucla.fusa.android.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;

import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.actividades.VistasInicialesActivity;

/**
 * Created by juanlabrador on 21/10/14.
 *
 * Clase de tareas programadas, que se utiliza al momento de cerrar sesión, pueda limpiar
 * en segundo plano, todas las preferencias del usuario que se habia logueado.
 *
 */
public class CerrarSesionAsyncTaks extends AsyncTask<Void, Void, Void> {

    private ProgressDialog dialog;
    private Activity activity;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;

    public CerrarSesionAsyncTaks(Activity activity, SharedPreferences preferencias) {
        this.activity = activity;
        this.preferencias = preferencias;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setMessage(activity.getResources().getString(R.string.cerrar_sesion));
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        editor = preferencias.edit();
        editor.clear();
        editor.commit();
        SystemClock.sleep(2000);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        activity.startActivity(new Intent(activity, VistasInicialesActivity.class));
        activity.finish();
    }
}
