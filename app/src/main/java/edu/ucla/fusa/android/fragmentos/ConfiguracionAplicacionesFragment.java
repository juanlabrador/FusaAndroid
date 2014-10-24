package edu.ucla.fusa.android.fragmentos;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.adaptadores.ListAplicationsAdapter;
import edu.ucla.fusa.android.modelo.ItemListAplications;

/**
 * Created by juanlabrador on 20/10/14.
 *
 * Clase que administra la lista de aplicaciones que se enlazaran a la aplicación.
 *
 */
public class ConfiguracionAplicacionesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;
    private ListView list;
    private TypedArray aplicationIcons;
    private String[] titulos;
    private ArrayList<ItemListAplications> items;
    private ListAplicationsAdapter adapter;
    private ImageView checkApp;
    private ImageView iconoApp;
    private TextView tituloApp;

    public static ConfiguracionAplicacionesFragment newInstance() {
        ConfiguracionAplicacionesFragment activity = new ConfiguracionAplicacionesFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public ConfiguracionAplicacionesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_configuraciones_aplicaciones, container, false);
        list = (ListView) view.findViewById(R.id.lvAplicaciones);
        aplicationIcons = getResources().obtainTypedArray(R.array.icons_app);
        titulos = getResources().getStringArray(R.array.redes_app);

        /** Cargamos los items a la lista */
        items = new ArrayList<ItemListAplications>();

        items.add(new ItemListAplications(titulos[0], aplicationIcons.getResourceId(0, -1)));
        items.add(new ItemListAplications(titulos[1], aplicationIcons.getResourceId(1, -1)));
        items.add(new ItemListAplications(titulos[2], aplicationIcons.getResourceId(2, -1)));
        items.add(new ItemListAplications(titulos[3], aplicationIcons.getResourceId(3, -1)));

        adapter = new ListAplicationsAdapter(getActivity(), items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setIcon(R.drawable.ic_cuentas_enlazadas_white);
        getActivity().getActionBar().setTitle(R.string.configuracion_aplicaciones_cuentas_enlazadas);
    }

    /**
     * Evento de la clase onItemClickListener que acciona
     * un elemento de la lista al presionar en él
     *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        iconoApp = (ImageView) view.findViewById(R.id.ivIconoApp);
        tituloApp = (TextView) view.findViewById(R.id.tvTituloApp);
        checkApp = (ImageView) view.findViewById(R.id.ivIconoActivacionApp);
        switch (position) {
            case 0:
                if (checkApp.getVisibility() == View.INVISIBLE) {
                    checkApp.setVisibility(View.VISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_facebook_enable);
                    tituloApp.setTextColor(getResources().getColor(R.color.azul_llamativo));

                    /* Código para desactivar cuenta de Facebook */
                } else {
                    checkApp.setVisibility(View.INVISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_facebook_disable);
                    tituloApp.setTextColor(getResources().getColor(android.R.color.darker_gray));

                    /* Código para desactivar cuenta de Facebook */
                }
                break;
            case 1:
                if (checkApp.getVisibility() == View.INVISIBLE) {
                    checkApp.setVisibility(View.VISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_google_enable);
                    tituloApp.setTextColor(getResources().getColor(R.color.azul_llamativo));

                    /* Código para desactivar cuenta de Facebook */
                } else {
                    checkApp.setVisibility(View.INVISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_google_disable);
                    tituloApp.setTextColor(getResources().getColor(android.R.color.darker_gray));

                    /* Código para desactivar cuenta de Facebook */
                }
                break;
            case 2:
                if (checkApp.getVisibility() == View.INVISIBLE) {
                    checkApp.setVisibility(View.VISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_instagram_enable);
                    tituloApp.setTextColor(getResources().getColor(R.color.azul_llamativo));

                    /* Código para desactivar cuenta de Facebook */
                } else {
                    checkApp.setVisibility(View.INVISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_instagram_disable);
                    tituloApp.setTextColor(getResources().getColor(android.R.color.darker_gray));

                    /* Código para desactivar cuenta de Facebook */
                }
                break;
            case 3:
                if (checkApp.getVisibility() == View.INVISIBLE) {
                    checkApp.setVisibility(View.VISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_twitter_enable);
                    tituloApp.setTextColor(getResources().getColor(R.color.azul_llamativo));

                    /* Código para desactivar cuenta de Facebook */
                } else {
                    checkApp.setVisibility(View.INVISIBLE);
                    iconoApp.setImageResource(R.drawable.ic_twitter_disable);
                    tituloApp.setTextColor(getResources().getColor(android.R.color.darker_gray));

                    /* Código para desactivar cuenta de Facebook */
                }
            break;
        }
    }
}
