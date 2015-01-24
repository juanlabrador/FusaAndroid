package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.DB.UserTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasInicialesActivity;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;

/**
 * Created by juanlabrador on 23/01/15.
 */
public class LogoutFragment extends Fragment {

    private static String TAG = "LogoutFragment";
    private UserTable mUserTable;
    private Usuario mUsuario;
    private SharedPreferences mPreferencias;
    private SharedPreferences.Editor mEditor;
    
    public static LogoutFragment newInstance() {
        LogoutFragment fragment = new LogoutFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserTable = new UserTable(getActivity());
        new Logout().execute();
    }

    private class Logout extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            mUsuario = mUserTable.searchUser();
        }

        protected Void doInBackground(Void[] params) {
            SystemClock.sleep(3000);
            mPreferencias = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
            mEditor = mPreferencias.edit();
            mEditor.clear();
            mEditor.putString("usuario", mUsuario.getUsername());
            if (mUsuario.getFoto() != null) {
                mEditor.putString("foto", Base64.encodeToString(mUsuario.getFoto(), Base64.DEFAULT));
            } else {
                mEditor.putString("foto", "");
            }
            mEditor.commit();
            Log.i(TAG, "¡Cerrando sesión...!");
            DataBaseHelper.getInstance(getActivity()).close();
            getActivity().deleteDatabase(DataBaseHelper.NAME);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            startActivity(new Intent(getActivity(), VistasInicialesActivity.class));
            getActivity().finish();
        }
    }
    
}
