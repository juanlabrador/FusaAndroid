package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.juanlabrador.grouplayout.GroupContainer;

import java.util.ArrayList;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.Agrupacion;

public class AgrupacionAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Agrupacion> mAgrupaciones;
    private Agrupacion mAgrupacion;
    private HorarioAgrupacionAdapter mHorarioAdapter;

    public AgrupacionAdapter(Activity activity, ArrayList<Agrupacion> agrupaciones) {
        mActivity = activity;
        mAgrupaciones = agrupaciones;
    }

    public void clear() {
        mAgrupaciones.clear();
    }

    public int getCount() {
        return mAgrupaciones.size();
    }

    public Object getItem(int position) {
        return mAgrupaciones.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.custom_item_list_drawer, null);
            mViewHolder.mAgrupacion = (TextView) convertView.findViewById(R.id.nombre_agrupacion);
            mViewHolder.mIntructor = (TextView) convertView.findViewById(R.id.instructor_agrupacion);
            mViewHolder.mDatosInstructor = (GroupContainer) convertView.findViewById(R.id.grupo_instructor);
            mViewHolder.mListaHorario = (ListView) convertView.findViewById(R.id.lista_horario_agrupacion);
            convertView.setTag(mViewHolder);
            
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mAgrupacion = mAgrupaciones.get(position);

        mViewHolder.mAgrupacion.setText(mAgrupacion.getDescripcion());
        mViewHolder.mIntructor.setText(mAgrupacion.getInstructor().getNombre() + " " + mAgrupacion.getInstructor().getApellido());
        mViewHolder.mDatosInstructor.addSimpleOneButtonLayout(mAgrupacion.getInstructor().getTelefonoMovil(), R.drawable.ic_llamada);
        mViewHolder.mDatosInstructor.addSimpleOneButtonLayout(mAgrupacion.getInstructor().getCorreo(), R.drawable.ic_correo);
        mHorarioAdapter = new HorarioAgrupacionAdapter(mActivity, mAgrupacion.getHorarioArea());
        mViewHolder.mListaHorario.setAdapter(mHorarioAdapter);
        return convertView;
    }

    public static class ViewHolder {
        TextView mAgrupacion;
        TextView mIntructor;
        GroupContainer mDatosInstructor;
        ListView mListaHorario;
    }
}