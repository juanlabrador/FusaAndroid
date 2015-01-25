package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListNoticiasAdapter extends BaseAdapter {

    private static String TAG = "ListNoticiasAdapter";
    private FragmentActivity activity;
    private ArrayList<ItemListNoticia> arrayItems;
    private ViewHolder mViewHolder;
    private ItemListNoticia item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    public ListNoticiasAdapter(FragmentActivity activity, ArrayList<ItemListNoticia> paramArrayList, ListFragment fragment) {
        this.activity = activity;
        this.arrayItems = paramArrayList;
    }

    public int getCount() {
        return arrayItems.size();
    }

    public Object getItem(int paramInt) {
        return arrayItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }


    public static class ViewHolder {
        TextView mFecha;
        ImageView mImagen;
        TextView mTitulo;
        ExpandableTextView mExpandible;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        item = arrayItems.get(position);
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            if (item.getImagen() == null) {
                Log.i(TAG, "¡Sin foto!");
                convertView = inflater.inflate(R.layout.custom_item_list_noticias_sin_foto, null);
                mViewHolder.mTitulo = (TextView) convertView.findViewById(R.id.tv_titulo_noticia_sin_foto);
                mViewHolder.mFecha = (TextView) convertView.findViewById(R.id.tv_fecha_publicacion_noticia_sin_foto);
                mViewHolder.mImagen = (ImageView) convertView.findViewById(R.id.iv_foto_noticia_sin_foto);
                mViewHolder.mExpandible = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view_sin_foto);
            } else  {
                Log.i(TAG, "¡Con foto!");
                convertView = inflater.inflate(R.layout.custom_item_list_noticias, null);
                mViewHolder.mTitulo = (TextView) convertView.findViewById(R.id.tv_titulo_noticia);
                mViewHolder.mFecha = (TextView) convertView.findViewById(R.id.tv_fecha_publicacion_noticia);
                mViewHolder.mImagen = (ImageView) convertView.findViewById(R.id.iv_foto_noticia);
                mViewHolder.mExpandible = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            }
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        item = arrayItems.get(position);
        mViewHolder.mTitulo.setText(item.getTitulo());
        mViewHolder.mFecha.setText(dateFormat.format(item.getFecha()));
        if (item.getImagen() != null) {
            Picasso.with(activity)
                    .load(JSONParser.URL_IMAGEN + item.getId())
                    .into(mViewHolder.mImagen);
        }
        mViewHolder.mExpandible.setText(item.getDescripcion());
        return convertView;
    }
}
