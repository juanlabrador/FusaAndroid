package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.interfaces.Item;
import edu.ucla.fusa.android.modelo.HeaderListConfiguracion;
import edu.ucla.fusa.android.modelo.ItemListConfiguration;
import java.util.ArrayList;

public class ListConfiguracionAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater layout;

    public ListConfiguracionAdapter(Context paramContext, ArrayList<Item> paramArrayList) {
        super(paramContext, 0, paramArrayList);
        this.context = paramContext;
        this.items = paramArrayList;
        this.layout = ((LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        View view = paramView;
        Item item = items.get(paramInt);
        if (item != null) {
            if (item.isSection()) {
                HeaderListConfiguracion header = (HeaderListConfiguracion)item;
                view = layout.inflate(R.layout.custom_item_list_section_configuraciones, null);
                view.setOnClickListener(null);
                view.setOnLongClickListener(null);
                view.setLongClickable(false);
                ((TextView) view.findViewById(R.id.list_item_section_text)).setText(header.getTitulo());
            } else {
                ItemListConfiguration itemList = (ItemListConfiguration) item;
                view = this.layout.inflate(R.layout.custom_item_list_configuraciones, null);
                ImageView icono = (ImageView) view.findViewById(R.id.iv_icono_configuracion);
                TextView titulo = (TextView) view.findViewById(R.id.tv_titulo_configuracion);
                if (icono != null) {
                    icono.setImageResource(itemList.icono);
                }
                if (titulo != null) {
                    titulo.setText(itemList.title);
                }
            }
        }
        return view;
    }
}