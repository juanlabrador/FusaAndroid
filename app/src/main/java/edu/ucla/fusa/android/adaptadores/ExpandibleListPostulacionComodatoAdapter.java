package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.HorarioPorGrupo;
import edu.ucla.fusa.android.modelo.herramientas.ItemListEstudiantesComodato;
import edu.ucla.fusa.android.modelo.instrumentos.PostulacionComodato;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class ExpandibleListPostulacionComodatoAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> group; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ItemListEstudiantesComodato>> contenido;

    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    private Calendar horaEmision = Calendar.getInstance();
    private Calendar horaVencimiento = Calendar.getInstance();

    public ExpandibleListPostulacionComodatoAdapter(Context context, List<String> group,
                                                    HashMap<String, List<ItemListEstudiantesComodato>> contenido) {
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
        ItemListEstudiantesComodato det = (ItemListEstudiantesComodato) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_item_list_comodato, null);
        }

        CircleImageView foto = (CircleImageView) convertView.findViewById(R.id.iv_foto_perfil_estudiante_comodato);

        TextView nombre = (TextView) convertView
                .findViewById(R.id.tv_nombre_estudiante_comodato);

        TextView edad = (TextView) convertView
                .findViewById(R.id.tv_edad_estudiante_comodato);

        TextView instrumento = (TextView) convertView
                .findViewById(R.id.tv_instrumento_estudiante_comodato);

        View nivel1 = convertView.findViewById(R.id.barraNivel1);
        View nivel2 = convertView.findViewById(R.id.barraNivel2);
        View nivel3 = convertView.findViewById(R.id.barraNivel3);
        View nivel4 = convertView.findViewById(R.id.barraNivel4);
        View nivel5 = convertView.findViewById(R.id.barraNivel5);


        if (det.getFoto() != -1) {
            foto.setImageResource(det.getFoto());
        }
        if (det.getNombre() != null) {
            nombre.setText(det.getNombre());
        }
        if (det.getEdad() != 0) {
            edad.setText(String.valueOf(det.getEdad()));
        }
        if (det.getInstrumento() != null) {
            instrumento.setText(det.getInstrumento());
        }

        switch (det.getPuntuacion()) {
            case 0:
                nivel1.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel2.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel3.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel4.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel5.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                break;
            case 1:
                nivel1.setBackgroundColor(_context.getResources().getColor(R.color.nivel_1));
                nivel2.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel3.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel4.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel5.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                break;
            case 2:
                nivel1.setBackgroundColor(_context.getResources().getColor(R.color.nivel_2));
                nivel2.setBackgroundColor(_context.getResources().getColor(R.color.nivel_2));
                nivel3.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel4.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel5.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                break;
            case 3:
                nivel1.setBackgroundColor(_context.getResources().getColor(R.color.nivel_3));
                nivel2.setBackgroundColor(_context.getResources().getColor(R.color.nivel_3));
                nivel3.setBackgroundColor(_context.getResources().getColor(R.color.nivel_3));
                nivel4.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                nivel5.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                break;
            case 4:
                nivel1.setBackgroundColor(_context.getResources().getColor(R.color.nivel_4));
                nivel2.setBackgroundColor(_context.getResources().getColor(R.color.nivel_4));
                nivel3.setBackgroundColor(_context.getResources().getColor(R.color.nivel_4));
                nivel4.setBackgroundColor(_context.getResources().getColor(R.color.nivel_4));
                nivel5.setBackgroundColor(_context.getResources().getColor(android.R.color.darker_gray));
                break;
            case 5:
                nivel1.setBackgroundColor(_context.getResources().getColor(R.color.nivel_5));
                nivel2.setBackgroundColor(_context.getResources().getColor(R.color.nivel_5));
                nivel3.setBackgroundColor(_context.getResources().getColor(R.color.nivel_5));
                nivel4.setBackgroundColor(_context.getResources().getColor(R.color.nivel_5));
                nivel5.setBackgroundColor(_context.getResources().getColor(R.color.nivel_5));
                break;
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
            convertView = infalInflater.inflate(R.layout.custom_item_group_estudiantes_comodato, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tv_group_estudiantes_comodato);
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
