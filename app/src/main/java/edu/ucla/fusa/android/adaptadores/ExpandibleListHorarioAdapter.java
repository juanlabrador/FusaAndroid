package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.HorarioPorGrupo;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class ExpandibleListHorarioAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> group; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<HorarioPorGrupo>> contenido;

    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    private Calendar horaInicio = Calendar.getInstance();
    private Calendar horaFin = Calendar.getInstance();

    public ExpandibleListHorarioAdapter(Context context, List<String> group,
                                        HashMap<String, List<HorarioPorGrupo>> contenido) {
        this._context = context;
        this.group = group;
        this.contenido = contenido;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return contenido.get(group.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        HorarioPorGrupo det = (HorarioPorGrupo) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_item_list_schedule, null);
        }

        View barra = convertView.findViewById(R.id.barra_tipo_horario);

        TextView actividad = (TextView) convertView
                .findViewById(R.id.tv_actividad_schedule);

        TextView instructor = (TextView) convertView
                .findViewById(R.id.tv_instructor_schedule);

        TextView horario = (TextView) convertView
                .findViewById(R.id.tv_horario_schedule);

        TextView lugar = (TextView) convertView
                .findViewById(R.id.tv_lugar_schedule);

        if (det.getGrupo().getDescripcion() != null) {
            actividad.setText(det.getGrupo().getDescripcion());
        }
        if (det.getGrupo().getInstructor().getNombre() != null) {
            instructor.setText(det.getGrupo().getInstructor().getNombre());
        }

        /*if (det.getHorario().getHoraInicio() != null) {
            horaInicio = det.getHorario().getHoraInicio();
            horaInicio.set(Calendar.HOUR, 8);
        }
        if (det.getHorario().getHoraFin() != null) {
            horaFin = det.getHorario().getHoraFin();
            horaFin.set(Calendar.HOUR, 9);
        }*/
        if (det.getHorario().getHoraInicio() != null && det.getHorario().getHoraFin() != null) {
            horario.setText(sdf.format(horaInicio.getTime()) + " - " + sdf.format(horaFin.getTime()));
        }
        if (det.getAreaEstudio().getDescripcion() != null) {
            lugar.setText(det.getAreaEstudio().getDescripcion());
        }
        if (det.getGrupo().getTipo() == 1) {
            barra.setBackgroundColor(_context.getResources().getColor(R.color.clase_particular));
        } else if (det.getGrupo().getTipo() == 2) {
            barra.setBackgroundColor(_context.getResources().getColor(R.color.clase_agrupacion));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return contenido.get(group.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.group.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.group.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_item_group_schedule, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tv_group_schedule);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
