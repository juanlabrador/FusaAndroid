package edu.ucla.fusa.android.actividades;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import edu.ucla.fusa.android.fragmentos.LoginFragment;
import edu.ucla.fusa.android.fragmentos.RegistrarPostulacionesFragment;
import edu.ucla.fusa.android.fragmentos.TipoAccesoFragment;

/**
 * Created by juanlabrador on 17/10/14.
 */
public class VistasAccesoActivity extends FragmentActivity {

    private FragmentTransaction transaction;
    private int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        key = getIntent().getIntExtra("acceso", -1);

        switch (key) {
           case 0:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, LoginFragment.newInstance());
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
           case 1:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, RegistrarPostulacionesFragment.newInstance());
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
           case 2:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, TipoAccesoFragment.newInstance());
                //transaction.addToBackStack(null); //Es el primero, no se agrega.
                transaction.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
