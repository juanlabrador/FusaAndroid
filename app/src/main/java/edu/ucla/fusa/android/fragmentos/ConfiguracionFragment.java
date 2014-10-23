package edu.ucla.fusa.android.fragmentos;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.ucla.fusa.android.AsyncTask.CerrarSesionAsyncTaks;
import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.adaptadores.ListAplicationsAdapter;
import edu.ucla.fusa.android.adaptadores.ListConfiguracionAdapter;
import edu.ucla.fusa.android.interfaces.Item;
import edu.ucla.fusa.android.modelo.HeaderListConfiguracion;
import edu.ucla.fusa.android.modelo.ItemListConfiguration;

/**
 * Created by juanlabrador on 19/10/14.
 *
 * Clase que administra todos los elementos de la lista de configuración.
 *
 */
public class ConfiguracionFragment extends PreferenceFragment implements AdapterView.OnItemClickListener {

    private View view;
    private ListView list;
    private ArrayList<Item> items = new ArrayList<Item>();
    private ListConfiguracionAdapter adapter;
    private SharedPreferences preferencias;
    private CerrarSesionAsyncTaks asyncTaks;
    private FragmentManager fragmentManager;
    private ConfiguracionAcercaNosotrosFragment acercaNosotros;
    private ConfiguracionAcercaVersionFragment acercaVersion;

    public static ConfiguracionFragment newInstance() {
        ConfiguracionFragment activity = new ConfiguracionFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ConfiguracionFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle(R.string.configuracion);
        getActivity().getActionBar().setIcon(R.drawable.ic_configuracion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_drawer_configuraciones, container, false);
        list = (ListView) view.findViewById(R.id.lvConfiguraciones);

        /** Limpiamos la lista para que no acumule los items */
        items.clear();

        /** Cargamos el listado de configuraciones */
        items.add(new HeaderListConfiguracion(getResources().getString(R.string.configuracion_cuenta), 0));
        items.add(new ItemListConfiguration(getResources().getString(R.string.configuracion_cuenta_cambiar_password),
                R.drawable.ic_cambiar_password));
        items.add(new ItemListConfiguration(getResources().getString(R.string.configuracion_cuenta_cerrar_sesion),
                R.drawable.ic_cerrar_sesion));

        items.add(new HeaderListConfiguracion(getResources().getString(R.string.configuracion_aplicaciones), 0));
        items.add(new ItemListConfiguration(getResources().getString(R.string.configuracion_aplicaciones_cuentas_enlazadas),
                R.drawable.ic_cuentas_enlazadas));

        items.add(new HeaderListConfiguracion(getResources().getString(R.string.configuracion_informacion), 0));
        items.add(new ItemListConfiguration(getResources().getString(R.string.configuracion_informacion_version),
                R.drawable.ic_informacion_version));
        items.add(new ItemListConfiguration(getResources().getString(R.string.configuracion_informacion_nosotros),
                R.drawable.ic_acerca_de));
        items.add(new ItemListConfiguration(getResources().getString(R.string.configuracion_informacion_web),
                R.drawable.ic_informacion_web));

        items.add(new HeaderListConfiguracion(getResources().getString(R.string.configuracion_ayuda), 0));
        items.add(new ItemListConfiguration(getResources().getString(R.string.configuracion_ayuda_problema),
                R.drawable.ic_informacion_problema));


        adapter = new ListConfiguracionAdapter(getActivity(), items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            /** Fragment de cambiar password */
            case 1:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionCambiarPasswordFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            /** Cerramos sesión */
            case 2:
                preferencias = getActivity().getSharedPreferences("index", Context.MODE_PRIVATE);
                asyncTaks = new CerrarSesionAsyncTaks(getActivity(), preferencias);
                asyncTaks.execute();
                break;
            /** Fragment de Aplicaciones */
            case 4:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionAplicacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            /** Fragment de Acerca de la versión */
            case 6:
                fragmentManager = getFragmentManager();
                acercaVersion = new ConfiguracionAcercaVersionFragment();
                acercaVersion.show(fragmentManager, "AcercaVersión");
                break;
            /** Fragment de Acerca de nosotros */
            case 7:
                fragmentManager = getFragmentManager();
                acercaNosotros = new ConfiguracionAcercaNosotrosFragment();
                acercaNosotros.show(fragmentManager, "AcercaNosotros");
                break;
             /** Visitamos la página web de Fundamusical */
            case 8:
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(getActivity().getResources().getString(R.string.pagina_web))));
                break;
            /** Fragment de Informar un problema */
            case 10:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionInformarProblemaFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
