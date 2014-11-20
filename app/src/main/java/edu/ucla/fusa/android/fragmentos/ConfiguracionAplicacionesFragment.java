package edu.ucla.fusa.android.fragmentos;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListAplicationsAdapter;
import edu.ucla.fusa.android.modelo.herramientas.ItemListAplications;
import java.util.ArrayList;

public class ConfiguracionAplicacionesFragment extends Fragment implements AdapterView.OnItemClickListener {

  private ListAplicationsAdapter adapter;
  private TypedArray aplicationIcons;
  private ImageView checkApp;
  private ImageView iconoApp;
  private ArrayList<ItemListAplications> items;
  private ListView list;
  private TextView tituloApp;
  private String[] titulos;
  private View view;

  public static ConfiguracionAplicacionesFragment newInstance() {
    ConfiguracionAplicacionesFragment fragment = new ConfiguracionAplicacionesFragment();
    fragment.setRetainInstance(true);
    return fragment;
  }

  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    getActivity().getActionBar().setIcon(R.drawable.ic_cuentas_enlazadas_white);
    getActivity().getActionBar().setTitle(R.string.configuracion_aplicaciones_cuentas_enlazadas);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    view = paramLayoutInflater.inflate(R.layout.fragment_configuraciones_aplicaciones, paramViewGroup, false);
    list = ((ListView) view.findViewById(R.id.list_view_aplicaciones));
    aplicationIcons = getResources().obtainTypedArray(R.array.icons_app);
    titulos = getResources().getStringArray(R.array.redes_app);
    items = new ArrayList();
    items.add(new ItemListAplications(
            titulos[0],
            aplicationIcons.getResourceId(0, -1)));
    items.add(new ItemListAplications(
            titulos[1],
            aplicationIcons.getResourceId(1, -1)));
    items.add(new ItemListAplications(
            titulos[2],
            aplicationIcons.getResourceId(2, -1)));
    items.add(new ItemListAplications(
            titulos[3],
            aplicationIcons.getResourceId(3, -1)));
    adapter = new ListAplicationsAdapter(getActivity(), items);
    list.setAdapter(adapter);
    list.setOnItemClickListener(this);
    return view;
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    iconoApp = ((ImageView) paramView.findViewById(R.id.ivIconoApp));
    tituloApp = ((TextView) paramView.findViewById(R.id.tvTituloApp));
    checkApp = ((ImageView) paramView.findViewById(R.id.ivIconoActivacionApp));
    switch (paramInt) {
        default:
            break;
        case 0:
            if (checkApp.getVisibility() == View.INVISIBLE) {
                checkApp.setVisibility(View.VISIBLE);
                iconoApp.setImageResource(R.drawable.ic_facebook_enable);
                tituloApp.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
            }
            checkApp.setVisibility(View.INVISIBLE);
            iconoApp.setImageResource(R.drawable.ic_facebook_disable);
            tituloApp.setTextColor(getResources().getColor(R.color.gris_oscuro));
            break;
        case 1:
            if (checkApp.getVisibility() == View.INVISIBLE) {
                checkApp.setVisibility(View.VISIBLE);
                iconoApp.setImageResource(R.drawable.ic_google_enable);
                tituloApp.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
            }
            checkApp.setVisibility(View.INVISIBLE);
            iconoApp.setImageResource(R.drawable.ic_google_disable);
            tituloApp.setTextColor(getResources().getColor(R.color.gris_oscuro));
            break;
        case 2:
            if (checkApp.getVisibility() == View.INVISIBLE) {
                checkApp.setVisibility(View.VISIBLE);
                iconoApp.setImageResource(R.drawable.ic_instagram_enable);
                tituloApp.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
            }
            checkApp.setVisibility(View.INVISIBLE);
            iconoApp.setImageResource(R.drawable.ic_instagram_disable);
            tituloApp.setTextColor(getResources().getColor(R.color.gris_oscuro));
            break;
        case 3:
            if (checkApp.getVisibility() == View.INVISIBLE) {
                checkApp.setVisibility(View.VISIBLE);
                iconoApp.setImageResource(R.drawable.ic_twitter_enable);
                tituloApp.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
            }
            checkApp.setVisibility(View.INVISIBLE);
            iconoApp.setImageResource(R.drawable.ic_twitter_disable);
            tituloApp.setTextColor(getResources().getColor(R.color.gris_oscuro));
            break;
        }
    }
}
