package edu.ucla.fusa.android.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasInicialesActivity;

public class CerrarSesionAsyncTaks extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private ProgressDialog dialog;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferencias;

    public CerrarSesionAsyncTaks(Activity paramActivity, SharedPreferences paramSharedPreferences) {
        this.activity = paramActivity;
        this.preferencias = paramSharedPreferences;
    }
    
    protected Void doInBackground(Void[] paramArrayOfVoid) {
        this.editor = this.preferencias.edit();
        this.editor.clear();
        this.editor.commit();
        SystemClock.sleep(2000L);
        return null;
    }

    protected void onPostExecute(Void paramVoid) {
        super.onPostExecute(paramVoid);
        this.dialog.dismiss();
        this.activity.startActivity(new Intent(this.activity, VistasInicialesActivity.class));
        this.activity.finish();
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(this.activity);
        this.dialog.setMessage(this.activity.getResources().getString(R.string.cerrar_sesion));
        this.dialog.setIndeterminate(false);
        this.dialog.setCancelable(false);
        this.dialog.show();
    }
}