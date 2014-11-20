package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ucla.fusa.android.modelo.herramientas.ItemListOpcionesMultimedia;
import java.util.ArrayList;

public class ListOpcionesAdapter extends ArrayAdapter<ItemListOpcionesMultimedia>
{
  private Context context;
  private ArrayList<ItemListOpcionesMultimedia> items;
  private LayoutInflater layout;

  public ListOpcionesAdapter(Context paramContext, ArrayList<ItemListOpcionesMultimedia> paramArrayList)
  {
    super(paramContext, 0, paramArrayList);
    this.context = paramContext;
    this.items = paramArrayList;
    this.layout = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    ItemListOpcionesMultimedia localItemListOpcionesMultimedia = (ItemListOpcionesMultimedia)this.items.get(paramInt);
    if (localItemListOpcionesMultimedia != null)
    {
      localView = this.layout.inflate(2130903054, null);
      ImageView localImageView = (ImageView)localView.findViewById(2131296323);
      TextView localTextView = (TextView)localView.findViewById(2131296324);
      if (localImageView != null)
        localImageView.setImageResource(localItemListOpcionesMultimedia.icono);
      if (localTextView != null)
        localTextView.setText(localItemListOpcionesMultimedia.title);
    }
    return localView;
  }
}

/* Location:           /home/juanlabrador/Escritorio/apk/dex2jar-0.0.9.15/classes_dex2jar.jar
 * Qualified Name:     edu.ucla.fusa.android.adaptadores.ListOpcionesAdapter
 * JD-Core Version:    0.6.2
 */