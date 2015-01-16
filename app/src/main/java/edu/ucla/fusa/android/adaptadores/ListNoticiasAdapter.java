package edu.ucla.fusa.android.adaptadores;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListNoticiasAdapter extends BaseAdapter {

    private static String TAG = "ListNoticiasAdapter";
    private FragmentActivity activity;
    private ArrayList<ItemListNoticia> arrayItems;
    private ViewHolder mViewHolder;
    private ItemListNoticia item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private static String URL = "http://10.0.3.2:8080/fusa.frontend/webservices/rest/downloads/";

    public ListNoticiasAdapter(FragmentActivity activity, ArrayList<ItemListNoticia> paramArrayList, ListFragment fragment) {
        this.activity = activity;
        this.arrayItems = paramArrayList;
    }

    public int getCount() {
        return this.arrayItems.size();
    }

    public Object getItem(int paramInt) {
        return this.arrayItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }


    public static class ViewHolder {
        TextView descripcion;
        TextView fecha;
        ImageView imagen;
        TextView titulo;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.custom_item_list_noticias, null);
            mViewHolder.titulo = (TextView) view.findViewById(R.id.tv_titulo_noticia);
            mViewHolder.fecha = (TextView) view.findViewById(R.id.tv_fecha_publicacion_noticia);
            mViewHolder.imagen = (ImageView) view.findViewById(R.id.iv_foto_noticia);
            mViewHolder.descripcion = (TextView) view.findViewById(R.id.tv_descripcion_noticia);

            view.setTag(mViewHolder);
            
            item = arrayItems.get(position);
            mViewHolder.titulo.setText(item.getTitulo());
            mViewHolder.fecha.setText(dateFormat.format(item.getFecha()));
            Picasso.with(activity)
                    .load(URL + item.getId())
                    .into(mViewHolder.imagen);
            mViewHolder.descripcion.setText(item.getDescripcion());
        } else {
            mViewHolder = (ViewHolder) view.getTag();
            item = arrayItems.get(position);
            mViewHolder.titulo.setText(item.getTitulo());
            mViewHolder.fecha.setText(dateFormat.format(item.getFecha()));
            Picasso.with(activity)
                    .load(URL + item.getId())
                    .into(mViewHolder.imagen);
            mViewHolder.descripcion.setText(item.getDescripcion());
        }
        return view;
    }
}
