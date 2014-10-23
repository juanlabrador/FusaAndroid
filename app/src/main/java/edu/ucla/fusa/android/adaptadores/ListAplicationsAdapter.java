package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.ucla.fusa.android.actividades.R;
import edu.ucla.fusa.android.modelo.ItemListAplications;
import edu.ucla.fusa.android.modelo.ItemListDrawer;

/**
 * Created by juanlabrador on 19/10/14.
 */
public class ListAplicationsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemListAplications> arrayItems;

    public ListAplicationsAdapter(Activity activity, ArrayList<ItemListAplications> arrayItems) {
        this.activity = activity;
        this.arrayItems = arrayItems;
    }

    @Override
    public Object getItem(int position) {
        return arrayItems.get(position);
    }

    @Override
    public int getCount() {
        return arrayItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Fila {
        TextView titulo;
        ImageView icono;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fila fila;
        LayoutInflater inflator = activity.getLayoutInflater();

        if (convertView == null) {
            fila = new Fila();

            ItemListAplications item = arrayItems.get(position);
            convertView = inflator.inflate(R.layout.custom_item_list_aplicaciones, null);

            fila.titulo = (TextView) convertView.findViewById(R.id.tvTituloApp);
            fila.titulo.setText(item.getTitulo());

            fila.icono = (ImageView) convertView.findViewById(R.id.ivIconoApp);
            fila.icono.setImageResource(item.getIcono());

            convertView.setTag(fila);
        } else {
            fila = (Fila) convertView.getTag();
        }

        return convertView;
    }
}
