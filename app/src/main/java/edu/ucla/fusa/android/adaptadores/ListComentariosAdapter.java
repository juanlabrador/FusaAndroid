package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.evento.CalificarEvento;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListComentariosAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<CalificarEvento> mCalificaciones;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private CalificarEvento mCalificacion;
    private int mComentario;

    public ListComentariosAdapter(Activity activity, ArrayList<CalificarEvento> calificaciones, int comentario) {
        mActivity = activity;
        mCalificaciones = calificaciones;
        mComentario = comentario;
    }

    public int getCount() {
        return mCalificaciones.size();
    }

    public Object getItem(int position) {
        return mCalificaciones.get(position);
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
            mViewHolder.mNombre = (TextView) convertView.findViewById(R.id.tv_nombre_item_comentario);
            mViewHolder.mPuntuacion = (RatingBar) convertView.findViewById(R.id.calificacion_evento);
            mViewHolder.mFecha = (TextView) convertView.findViewById(R.id.tv_fecha_item_comentario);
            mViewHolder.mComentario = (TextView) convertView.findViewById(R.id.comentario_item_evento);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            
        }
        mCalificacion = mCalificaciones.get(position);
        if (mCalificacion.getId() != mComentario) {
            mViewHolder.mNombre.setText(mCalificacion.getUsuario().getNombre() + " " + mCalificacion.getUsuario().getApellido());
            mViewHolder.mPuntuacion.setRating(mCalificacion.getPonderacion());
            mViewHolder.mFecha.setText(mDateFormat.format(new Date(mCalificacion.getFechaPublicacion())));
            mViewHolder.mComentario.setText(mCalificacion.getComentario());
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView mComentario;
        TextView mFecha;
        TextView mNombre;
        RatingBar mPuntuacion;
    }
}