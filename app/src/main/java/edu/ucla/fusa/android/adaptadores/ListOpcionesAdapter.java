package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListOpcionesMultimedia;
import java.util.ArrayList;

public class ListOpcionesAdapter extends ArrayAdapter<ItemListOpcionesMultimedia>
{
  private Context context;
  private ArrayList<ItemListOpcionesMultimedia> items;
  private LayoutInflater layout;

  public ListOpcionesAdapter(Context context, ArrayList<ItemListOpcionesMultimedia> arrayList) {
    super(context, 0, arrayList);
    this.context = context;
    this.items = arrayList;
    this.layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
  }

  public View getView(int position, View view, ViewGroup viewGroup) {
    ItemListOpcionesMultimedia item = items.get(position);
    if (item != null) {
      view = layout.inflate(R.layout.custom_item_list_opciones_multimedia, null);
      ImageView icono = (ImageView) view.findViewById(R.id.iv_icono_multimedia);
      TextView descripcion = (TextView) view.findViewById(R.id.tv_opcion_multimedia);
      if (icono != null)
        icono.setImageResource(item.icono);
      if (descripcion != null)
        descripcion.setText(item.title);
    }
    return view;
  }
}