package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.instrumentos.Notificacion;

public class ListNotificacionesAdapter extends BaseAdapter {

    private static String TAG = "ListNotificacionesAdapter";
    private Context context;
    private ArrayList<Notificacion> mNotificaciones;
    private Notificacion mNotificacion;
    private PrettyTime mTime;

    public ListNotificacionesAdapter(Context context, ArrayList<Notificacion> notificaciones) {
        this.context = context;
        this.mNotificaciones = notificaciones;
        mTime = new PrettyTime();
    }

    public int getCount() {
        return mNotificaciones.size();
    }

    public Object getItem(int position) {
        return mNotificaciones.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView mFecha;
        TextView mTitulo;
        TextView mDescripcion;
        ImageView mImagen;
        RelativeLayout mContenedor;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.custom_item_notificaciones, parent, false);
            mViewHolder.mContenedor = (RelativeLayout) convertView.findViewById(R.id.contenedor_notificacion);
            mViewHolder.mTitulo = (TextView) convertView.findViewById(R.id.titulo_notificacion);
            mViewHolder.mFecha = (TextView) convertView.findViewById(R.id.fecha_notificacion);
            mViewHolder.mImagen = (ImageView) convertView.findViewById(R.id.imagen_notificacion);
            mViewHolder.mDescripcion = (TextView) convertView.findViewById(R.id.descripcion_notificacion);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mNotificacion = mNotificaciones.get(position);
        mViewHolder.mTitulo.setText(mNotificacion.getTitulo());
        mViewHolder.mFecha.setText(mTime.format(new Date(mNotificacion.getFecha())));
        mViewHolder.mDescripcion.setText(mNotificacion.getDescripcion());
        switch (mNotificacion.getTipoNotificacion().getId()) {
            case 1:
                mViewHolder.mImagen.setImageResource(R.drawable.notificacion_prestamo_aprobado);
                break;
            case 2:
                mViewHolder.mImagen.setImageResource(R.drawable.notificacion_prestamo_vencido);
                break;
            case 3:
                mViewHolder.mImagen.setImageResource(R.drawable.notificacion_solicitud_prestamo_aprobado);
                break;
            case 4:
                mViewHolder.mImagen.setImageResource(R.drawable.notificacion_solicitud_rechazado);
                break;
            case 5:
                mViewHolder.mImagen.setImageResource(R.drawable.notificacion_evento);
                break;
        }
        if (!mNotificacion.isMensajeLeido()) {
            mViewHolder.mContenedor.setBackgroundColor(context.getResources().getColor(R.color.azul_muy_transparente));
        }
        return convertView;
    }
}
