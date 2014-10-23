package edu.ucla.fusa.android.adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.interfaces.Item;
import edu.ucla.fusa.android.modelo.HeaderListConfiguracion;
import edu.ucla.fusa.android.modelo.ItemListConfiguration;

/**
 * Created by juanlabrador on 20/10/14.
 *
 * Clase adaptadora que se utiliza para personalizar la lista de configuración.
 *
 * Contiene un elemento de cabecera, y sus componentes asociados, un icono y un texto.
 *
 */
public class ListConfiguracionAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater layout;

    public ListConfiguracionAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if (i.isSection()) {
                HeaderListConfiguracion header = (HeaderListConfiguracion) i;
                view = layout.inflate(R.layout.custom_item_list_section_configuraciones, null);

                view.setOnClickListener(null);
                view.setOnLongClickListener(null);
                view.setLongClickable(false);

                final TextView  sectionView = (TextView) view.findViewById(R.id.list_item_section_text);
                sectionView.setText(header.getTitulo());

            } else {
                ItemListConfiguration iList = (ItemListConfiguration) i;
                view = layout.inflate(R.layout.custom_item_list_configuraciones, null);

                final ImageView icono = (ImageView) view.findViewById(R.id.ivIconoContentConfiguracion);
                final TextView titulo = (TextView) view.findViewById(R.id.tvTituloContentConfiguracion);

                if(icono != null) {
                    icono.setImageResource(iList.icono);
                }
                if (titulo != null) {
                    titulo.setText(iList.title);
                }
            }
        }

        return view;
    }
}
