package edu.ucla.fusa.android.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.interfaces.Item;
import edu.ucla.fusa.android.modelo.herramientas.HeaderListInstrumentos;
import edu.ucla.fusa.android.modelo.herramientas.ItemListInstrumentos;

public class ListInstrumentosAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ItemListInstrumentos> items;
    private LayoutInflater layout;
    private Fila fila;


    public ListInstrumentosAdapter(Context paramContext, ArrayList<ItemListInstrumentos> paramArrayList) {
        this.context = paramContext;
        this.items = paramArrayList;
        this.layout = LayoutInflater.from(paramContext);
    }

    public static class Fila {
        CheckBox seleccion;
        TextView instrumento;
    }

    public View getView(int position, View view, ViewGroup paramViewGroup) {

        if (view == null) {
            fila = new Fila();
            view = layout.inflate(R.layout.custom_item_list_instrumentos, null);
            fila.seleccion = (CheckBox) view.findViewById(R.id.cb_check_instrumento_donar);
            fila.instrumento = (TextView) view.findViewById(R.id.tv_nombre_instrumento_donar);
            view.setTag(fila);

        } else {
            fila = (Fila) view.getTag();
        }

        final ItemListInstrumentos item = items.get(position);
        fila.instrumento.setText(item.getInstrumento());
        fila.seleccion.setTag(item.getInstrumento());
        fila.seleccion.setChecked(item.getSeleccion());
        fila.seleccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                //Asegura que se modifica la fila
                //para evitar que se pierda el check al reciclar la lista
                //Es imprescindible tagear el item antes de establecer el valor del checkbox
                if (item.getInstrumento().equals(view.getTag().toString())) {
                    item.setSeleccion(isChecked);
                }
            }
        });
        return view;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int paramInt) {
        return items.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }
}