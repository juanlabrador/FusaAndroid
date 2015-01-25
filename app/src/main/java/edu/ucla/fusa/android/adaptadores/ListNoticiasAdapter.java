package edu.ucla.fusa.android.adaptadores;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
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
        return this.arrayItems.size();
    }

    public Object getItem(int paramInt) {
        return this.arrayItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }


    public static class ViewHolder {
        TextView mFecha;
        ImageView mImagen;
        TextView mTitulo;
        ExpandableTextView mExpandible;
        View mLinea1;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.custom_item_list_noticias, null);
            mViewHolder.mTitulo = (TextView) view.findViewById(R.id.tv_titulo_noticia);
            mViewHolder.mFecha = (TextView) view.findViewById(R.id.tv_fecha_publicacion_noticia);
            mViewHolder.mImagen = (ImageView) view.findViewById(R.id.iv_foto_noticia);
            mViewHolder.mExpandible = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
            mViewHolder.mLinea1 = view.findViewById(R.id.linea_divisor_1);
            view.setTag(mViewHolder);
            
            item = arrayItems.get(position);
            mViewHolder.mTitulo.setText(item.getTitulo());
            mViewHolder.mFecha.setText(dateFormat.format(item.getFecha()));
            Picasso.with(activity)
                    .load(JSONParser.URL_IMAGEN + item.getId())
                    .into(mViewHolder.mImagen);
            mViewHolder.mExpandible.setText(item.getDescripcion());
            mViewHolder.mLinea1.setBackgroundColor(Color.LTGRAY);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
            item = arrayItems.get(position);
            mViewHolder.mTitulo.setText(item.getTitulo());
            mViewHolder.mFecha.setText(dateFormat.format(item.getFecha()));
            Picasso.with(activity)
                    .load(JSONParser.URL_IMAGEN  + item.getId())
                    .into(mViewHolder.mImagen);
            mViewHolder.mExpandible.setText(item.getDescripcion());
            mViewHolder.mLinea1.setBackgroundColor(Color.LTGRAY);
        }
        return view;
    }
}
