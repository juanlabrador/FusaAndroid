package edu.ucla.fusa.android.actividades;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import edu.ucla.fusa.android.fragmentos.LoginFragment;
import edu.ucla.fusa.android.fragmentos.RegistrarPostulacionesFragment;
import edu.ucla.fusa.android.fragmentos.TipoAccesoFragment;

/**
 * Created by juanlabrador on 17/10/14.
 *
 * Esta clase se encarga de gestionar las vistas luego de las pantallas iniciales,
 * es decir, una vez visto la información acerca de Fundamusical, la próxima vez que inicie
 * la aplicación, se dirigira a una vista sencilla que tendra las vista de Login y Postulate.
 *
 */
public class VistasAccesoActivity extends FragmentActivity {

    private FragmentTransaction transaction;
    private int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         *
         * Al momento de llamar un fragment desde la vista de SplahsScreen o Postulate
         * puedo instanciar un fragment diferente, por tal motivo, por medio de unos parametros
         * identificamos a cual esta haciendo el llamado.
         *
         * */
        key = getIntent().getIntExtra("acceso", -1);

        switch (key) {
           case 0: /** Instanciamos el fragment de Login */
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, LoginFragment.newInstance());
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
           case 1: /** Instanciamos el fragment de Registrar Postulaciones */
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, RegistrarPostulacionesFragment.newInstance());
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
           case 2: /** Instanciamos el fragment de Tipo de acceso */
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
