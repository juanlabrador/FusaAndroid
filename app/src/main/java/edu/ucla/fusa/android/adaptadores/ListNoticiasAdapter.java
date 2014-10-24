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
import edu.ucla.fusa.android.modelo.ItemListNoticias;

/**
 * Created by juanlabrador on 23/10/14.
 *
 * Clase adaptadora que se utiliza para personalizar el contenido de la lista de noticias.
 *
 * Contiene la implementación de un titulo, una fecha y una imagen, y un enlace
 * para ver más contenido y compartir la información.
 *
 */
public class ListNoticiasAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemListNoticias> arrayItems;

    public ListNoticiasAdapter(Activity activity, ArrayList<ItemListNoticias> arrayItems) {
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

    /** Creamos una clase de los componentes de la lista */
    public static class Fila {
        TextView titulo;
        TextView fecha;
        ImageView imagen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Fila fila;
        LayoutInflater inflator = activity.getLayoutInflater();

        /** Si no hay contenido en la lista, los crea */
        if (convertView == null) {
            fila = new Fila();

            ItemListNoticias item = arrayItems.get(position);
            convertView = inflator.inflate(R.layout.custom_item_list_noticias, null);

            fila.titulo = (TextView) convertView.findViewById(R.id.tvTituloNoticia);
            fila.titulo.setText(item.getTitulo());

            fila.fecha = (TextView) convertView.findViewById(R.id.tvFechaPublicacion);
            fila.fecha.setText(item.getFecha());

            fila.imagen = (ImageView) convertView.findViewById(R.id.ivFotoNoticia);
            fila.imagen.setImageResource(item.getImagen());

            convertView.setTag(fila);
        } else {
            fila = (Fila) convertView.getTag();
        }

        return convertView;
    }
}
