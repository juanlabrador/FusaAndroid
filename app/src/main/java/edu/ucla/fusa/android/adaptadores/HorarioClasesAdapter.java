package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juanlabrador.grouplayout.GroupContainer;

import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.HorarioArea;

public class HorarioClasesAdapter extends BaseAdapter {

    private static String TAG = "HorarioAgrupacionAdapter";
    private Activity mActivity;
    private List<HorarioArea> mHorarios;
    private HorarioArea mHorarioArea;

    public HorarioClasesAdapter(Activity activity, List<HorarioArea> horarios) {
        mHorarios = horarios;
        mActivity = activity;
    }

    public void clear() {
        mHorarios.clear();
    }

    public int getCount() {
        return mHorarios.size();
    }

    public Object getItem(int position) {
        return mHorarios.get(position);
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
            convertView = mInflater.inflate(R.layout.custom_item_horario_clases, null);
            mViewHolder.mHorario = (GroupContainer) convertView.findViewById(R.id.hora_clases);
            convertView.setTag(mViewHolder);
          
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mHorarioArea = mHorarios.get(position);
        mViewHolder.mHorario.clear();
        Log.i(TAG, "ID: " + mHorarioArea.getHorario().getId());
        mViewHolder.mHorario.addSimpleMultiTextLayout(mHorarioArea.getHorario().getDia().getDescripcion().toUpperCase() + " - " + mHorarioArea.getAreaEstudio().getDescripcion().toUpperCase());
        mViewHolder.mHorario.addTextLayout(R.string.mis_clases_horario, mHorarioArea.getHorario().getHoraInicio() + ":00" + " - " + mHorarioArea.getHorario().getHoraFin() + ":00");
        return convertView;
    }

    public static class ViewHolder {
        GroupContainer mHorario;
    }
}