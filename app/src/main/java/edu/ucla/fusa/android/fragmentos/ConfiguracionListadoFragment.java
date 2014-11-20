package edu.ucla.fusa.android.fragmentos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.ucla.fusa.android.DB.DataBaseHelper;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasInicialesActivity;
import edu.ucla.fusa.android.adaptadores.ListConfiguracionAdapter;
import edu.ucla.fusa.android.interfaces.Item;
import edu.ucla.fusa.android.modelo.herramientas.HeaderListConfiguracion;
import edu.ucla.fusa.android.modelo.herramientas.ItemListConfiguration;
import java.util.ArrayList;

public class ConfiguracionListadoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ConfiguracionAcercaNosotrosFragment acercaNosotros;
    private ConfiguracionAcercaVersionFragment acercaVersion;
    private ListConfiguracionAdapter adapter;
    private CerrarSesionAsyncTaks asyncTaks;
    private FragmentManager fragmentManager;
    private ArrayList<Item> items = new ArrayList();
    private ListView list;
    private SharedPreferences preferencias;
    private View view;

    public static ConfiguracionListadoFragment newInstance() {
        ConfiguracionListadoFragment fragment = new ConfiguracionListadoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setTitle(R.string.configuracion);
        getActivity().getActionBar().setIcon(R.drawable.ic_configuracion);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.configuracion);
        getActivity().getActionBar().setIcon(R.drawable.ic_configuracion);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_configuraciones, paramViewGroup, false);
        list = ((ListView) view.findViewById(R.id.list_view_configuraciones));
        items.clear();
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_cuenta), 0));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_cambiar_password),
                R.drawable.ic_cambiar_password));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_cerrar_sesion),
                R.drawable.ic_cerrar_sesion));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_postularse),
                R.drawable.ic_estudiantes));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_participar),
                R.drawable.ic_profesores));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_contribuir),
                R.drawable.ic_instrumentos));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_cuenta_contratar),
                R.drawable.ic_eventos));
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_aplicaciones), 0));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_aplicaciones_cuentas_enlazadas),
                R.drawable.ic_cuentas_enlazadas));
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_informacion), 0));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informacion_version),
                R.drawable.ic_informacion_version));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informacion_nosotros),
                R.drawable.ic_acerca_de));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informacion_web),
                R.drawable.ic_informacion_web));
        items.add(new HeaderListConfiguracion(
                getResources().getString(R.string.configuracion_ayuda), 0));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_informar_problema),
                R.drawable.ic_informacion_problema));
        items.add(new ItemListConfiguration(
                getResources().getString(R.string.configuracion_sugerencia),
                R.drawable.ic_sugerencia));
        adapter = new ListConfiguracionAdapter(getActivity(), this.items);
        list.setAdapter(this.adapter);
        list.setOnItemClickListener(this);
        return view;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        switch (paramInt) {
            case 4:
            case 5:
            case 7:
            case 9:
            case 13:
            default:
                break;
            case 1:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionCambiarPasswordFragment.newInstance())
                        .addToBackStack(null).commit();
                break;
            case 2:
                preferencias = getActivity().getSharedPreferences("index", 0);
                new CerrarSesionAsyncTaks().execute();
                break;
            case 3:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, InicialPostulacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 6:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, InicialContratacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 8:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionAplicacionesFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 10:
                fragmentManager = getFragmentManager();
                acercaVersion = new ConfiguracionAcercaVersionFragment();
                acercaVersion.show(fragmentManager, "AcercaVersión");
                break;
            case 11:
                fragmentManager = getFragmentManager();
                acercaNosotros = new ConfiguracionAcercaNosotrosFragment();
                acercaNosotros.show(fragmentManager, "AcercaNosotros");
                break;
            case 12:
                getActivity().startActivity(new Intent("android.intent.action.VIEW")
                        .setData(Uri.parse(getActivity()
                                .getResources()
                                .getString(R.string.pagina_web))));
                break;
            case 14:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionInformarProblemaFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case 15:
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, ConfiguracionSugerenciaFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    private class CerrarSesionAsyncTaks extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private SharedPreferences.Editor editor;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getResources().getString(R.string.cerrar_sesion));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            editor = preferencias.edit();
            editor.clear();
            editor.commit();
            SystemClock.sleep(2000L);
            getActivity().deleteDatabase(DataBaseHelper.NAME);
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            dialog.dismiss();
            startActivity(new Intent(getActivity(), VistasInicialesActivity.class));
            getActivity().finish();
        }
    }
}
