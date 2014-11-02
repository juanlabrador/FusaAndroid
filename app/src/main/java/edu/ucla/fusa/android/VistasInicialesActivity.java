package edu.ucla.fusa.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import edu.ucla.fusa.android.fragmentos.ViewPagerInicialFragment;

/**
 * Created by juanlabrador on 16/10/14.
 *
 * Esta clase administra todas las vista deslizables iniciales, donde se muestra información
 * de la Fundación, y manejamos dos opciones de iniciación, logueandose o postulandose con
 * Fundamusical.
 *
 */
public class VistasInicialesActivity extends FragmentActivity {

    private Bundle arguments;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        getActionBar().hide();

        position = getIntent().getIntExtra("position", 0);
        arguments = new Bundle();
        arguments.putInt("position", position);
        getSupportFragmentManager().beginTransaction()
                    .replace(R.id.inicial_container, ViewPagerInicialFragment.newInstance(arguments))
                    .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
