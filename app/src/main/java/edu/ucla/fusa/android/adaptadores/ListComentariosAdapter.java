package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.ItemListComentario;
import edu.ucla.fusa.android.modelo.ItemListNoticia;

/**
 * Created by juanlabrador on 23/10/14.
 *
 * Clase adaptadora que se utiliza para personalizar el contenido de la lista de noticias.
 *
 * Contiene la implementación de un titulo, una fecha y una imagen, y un enlace
 * para ver más contenido y compartir la información.
 *
 */
public class ListComentariosAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemListComentario> arrayItems;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Date date;

    public ListComentariosAdapter(Activity activity, ArrayList<ItemListComentario> arrayItems) {
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
        CircleImageView foto;
        TextView nombre;
        RatingBar puntuacion;
        TextView fecha;
        TextView comentario;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Fila fila;
        LayoutInflater inflator = activity.getLayoutInflater();

        /** Si no hay contenido en la lista, los crea */
        if (convertView == null) {
            fila = new Fila();

            ItemListComentario item = arrayItems.get(position);
            convertView = inflator.inflate(R.layout.custom_item_list_comentario, null);

            fila.foto = (CircleImageView) convertView.findViewById(R.id.iv_foto_perfil_comentario);
            fila.foto.setImageResource(0);

            fila.nombre = (TextView) convertView.findViewById(R.id.tv_usuario_comentario);
            fila.nombre.setText(item.getNombre());

            fila.puntuacion = (RatingBar) convertView.findViewById(R.id.rb_puntuacion_comentario);
            fila.puntuacion.setRating(item.getPuntuacion());

            fila.fecha = (TextView) convertView.findViewById(R.id.tv_fecha_comentario);
            fila.fecha.setText(format.format(item.getFecha()));

            fila.comentario = (TextView) convertView.findViewById(R.id.tv_comentario);
            fila.comentario.setText(item.getComentario());

            convertView.setTag(fila);
        } else {
            fila = (Fila) convertView.getTag();
        }

        return convertView;
    }
}
