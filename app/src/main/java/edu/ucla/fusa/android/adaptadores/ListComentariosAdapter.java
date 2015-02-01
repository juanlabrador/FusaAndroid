package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.HexagonImageView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListComentario;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

//public class ListComentariosAdapter extends BaseAdapter {

    /*private Activity mActivity;
    private ArrayList<ItemListComentario> mComentarios;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private ItemListComentario mItem;

    public ListComentariosAdapter(Activity activity, ArrayList<ItemListComentario> list) {
        mActivity = activity;
        mComentarios = list;
    }

    public int getCount() {
        return mComentarios.size();
    }

    public Object getItem(int position) {
        return mComentarios.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mLayoutInflater = mActivity.getLayoutInflater();
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.custom_item_list_comentario, null);
            mViewHolder.mFoto = (HexagonImageView) convertView.findViewById(R.id.iv_foto_perfil_comentario);
            mViewHolder.mNombre = (TextView) convertView.findViewById(R.id.tv_usuario_comentario);
            mViewHolder.mPuntuacion = (RatingBar) convertView.findViewById(R.id.rb_puntuacion_comentario);
            mViewHolder.mFecha = (TextView) convertView.findViewById(R.id.tv_fecha_comentario);
            mViewHolder.mComentario = (TextView) convertView.findViewById(R.id.tv_comentario);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            
        }
        mItem = mComentarios.get(position);
        mViewHolder.mNombre.setText(mItem.getNombre());
        mViewHolder.mFoto.setImageResource(mItem.getFoto());
        mViewHolder.mPuntuacion.setRating(mItem.getPuntuacion());
        mViewHolder.mFecha.setText(mDateFormat.format(mItem.getFecha()));
        mViewHolder.mComentario.setText(mItem.getComentario());
        return convertView;
    }

    public static class ViewHolder {
        TextView mComentario;
        TextView mFecha;
        HexagonImageView mFoto;
        TextView mNombre;
        RatingBar mPuntuacion;
    }*/
//}