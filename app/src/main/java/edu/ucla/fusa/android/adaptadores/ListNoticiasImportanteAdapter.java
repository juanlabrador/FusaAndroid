package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

public class ListNoticiasImportanteAdapter extends BaseAdapter {

    private int VIEW_TYPE_FOTO = 1;
    private static int VIEW_TYPE_SIN_FOTO = 0;
    private static String TAG = "ListNoticiasAdapter";
    private Context context;
    private ArrayList<ItemListNoticia> arrayItems;
    private ViewHolder mViewHolder;
    private ViewHolderFoto mViewHolderFoto;
    private ItemListNoticia item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private View mView;

    public ListNoticiasImportanteAdapter(Context context, ArrayList<ItemListNoticia> paramArrayList, ListFragment fragment) {
        this.context = context;
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

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayItems.get(position).getHaveFoto() == 0) {
            return VIEW_TYPE_SIN_FOTO;
        } else {
            return VIEW_TYPE_FOTO;
        }
    }

    public static class ViewHolder {
        TextView mFecha;
        TextView mTitulo;
        ExpandableTextView mExpandible;
        int view;

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }
    }

    public static class ViewHolderFoto {
        TextView mFecha;
        ImageView mImagen;
        TextView mTitulo;
        ExpandableTextView mExpandible;
        int view;

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        item = arrayItems.get(position);
        int type = getItemViewType(position);
        if (type == VIEW_TYPE_SIN_FOTO) {
            if (convertView == null) {
                mViewHolder = new ViewHolder();

                Log.i(TAG, "¡Sin foto!");
                convertView = inflater.inflate(R.layout.custom_item_list_noticias_sin_foto, parent, false);
                mViewHolder.mTitulo = (TextView) convertView.findViewById(R.id.tv_titulo_noticia_sin_foto);
                mViewHolder.mFecha = (TextView) convertView.findViewById(R.id.tv_fecha_publicacion_noticia_sin_foto);
                mViewHolder.mExpandible = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view_sin_foto);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            Log.i(TAG, "¡Layout sin foto!");
            item = arrayItems.get(position);
            mViewHolder.mTitulo.setText(item.getTitulo());
            mViewHolder.mFecha.setText(item.getFecha());
            Log.i(TAG, "Tiene foto " + item.getHaveFoto());
            mViewHolder.mExpandible.setText(item.getDescripcion());
            
        } else if (type == VIEW_TYPE_FOTO) {
            if (convertView == null) {
                mViewHolderFoto = new ViewHolderFoto();
                Log.i(TAG, "¡Con foto!");
                convertView = inflater.inflate(R.layout.custom_item_list_noticias, parent, false);
                mView = inflater.inflate(R.layout.custom_item_list_noticias, parent, false);
                mViewHolderFoto.mTitulo = (TextView) convertView.findViewById(R.id.tv_titulo_noticia);
                mViewHolderFoto.mFecha = (TextView) convertView.findViewById(R.id.tv_fecha_publicacion_noticia);
                mViewHolderFoto.mImagen = (ImageView) convertView.findViewById(R.id.iv_foto_noticia);
                mViewHolderFoto.mExpandible = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                convertView.setTag(mViewHolderFoto);

            } else {
                mViewHolderFoto = (ViewHolderFoto) convertView.getTag();
            }
            Log.i(TAG, "¡Layout con foto!");
            item = arrayItems.get(position);
            mViewHolderFoto.mTitulo.setText(item.getTitulo());
            mViewHolderFoto.mFecha.setText(item.getFecha());
            Log.i(TAG, "Tiene foto " + item.getHaveFoto());
            Picasso.with(context)
                    .load(JSONParser.URL_IMAGEN_NOTICIA_IMPORTANTE + item.getId())
                    .into(mViewHolderFoto.mImagen);

            mViewHolderFoto.mExpandible.setText(item.getDescripcion());
        }
        
        return convertView;
    }
}
