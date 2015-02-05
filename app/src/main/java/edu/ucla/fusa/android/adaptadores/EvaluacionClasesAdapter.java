package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.EvaluacionPorAgrupacion;
import edu.ucla.fusa.android.modelo.academico.EvaluacionPorClase;

public class EvaluacionClasesAdapter extends BaseAdapter {

    private static String APROBADO = "alcanzado";
    private static String REPROBADO = "no alcanzado";
    private static String TAG = "EvaluacionClasesAdapter";
    private Activity mActivity;
    private List<EvaluacionPorClase> mEvaluaciones;
    private EvaluacionPorClase mEvaluacion;

    public EvaluacionClasesAdapter(Activity activity, List<EvaluacionPorClase> evaluaciones) {
        mEvaluaciones = evaluaciones;
        mActivity = activity;
    }

    public void clear() {
        mEvaluaciones.clear();
    }

    public int getCount() {
        return mEvaluaciones.size();
    }

    public Object getItem(int position) {
        return mEvaluaciones.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder mViewHolder;
        Log.i(TAG, "Cantidad: " + getCount());
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.custom_item_evaluacion_clases, null);
            mViewHolder.mTarea = (TextView) convertView.findViewById(R.id.tarea_asignada_clase);
            mViewHolder.mFecha = (TextView) convertView.findViewById(R.id.fecha_tarea_clase);
            mViewHolder.mCalificacion = (ImageView) convertView.findViewById(R.id.calificacion_clase);
            mViewHolder.mObservaciones = (ExpandableTextView) convertView.findViewById(R.id.observaciones_tarea_clase);
            convertView.setTag(mViewHolder);
          
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mEvaluacion = mEvaluaciones.get(position);
        Log.i(TAG, "ID: " + mEvaluacion.getId());
        mViewHolder.mTarea.setText(mEvaluacion.getTareaAsignada().toUpperCase());
        mViewHolder.mFecha.setText("Evaluado el " + mEvaluacion.getFecha());
        if (mEvaluacion.getEstadoCumplimiento().getDescripcion().equals(APROBADO)) {
            mViewHolder.mCalificacion.setImageResource(R.drawable.ic_aprobado);
        } else if (mEvaluacion.getEstadoCumplimiento().getDescripcion().equals(REPROBADO)) {
            mViewHolder.mCalificacion.setImageResource(R.drawable.ic_reprobado);
        }
        
        mViewHolder.mObservaciones.setText(
                "OBSERVACIONES\n\n" +
                        mEvaluacion.getObservaciones() + "\n" +
                        "Estado: " + mEvaluacion.getEstadoCumplimiento().getDescripcion() + "\n" +
                        "Pieza: " + mEvaluacion.getPieza() + "\n" +
                        "Estudio: " + mEvaluacion.getEstudio() + "\n" +
                        "Contenido: " + mEvaluacion.getObjetivoPorClase().getDescripcion()
                
        );
        return convertView;
    }

    public static class ViewHolder {
        TextView mTarea;
        TextView mFecha;
        ImageView mCalificacion;
        ExpandableTextView mObservaciones;
    }
}