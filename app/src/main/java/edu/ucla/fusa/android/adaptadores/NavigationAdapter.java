package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListDrawer;
import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemListDrawer> arrayItems;

    public NavigationAdapter(Activity paramActivity, ArrayList<ItemListDrawer> paramArrayList) {
        this.activity = paramActivity;
        this.arrayItems = paramArrayList;
    }

    public void clear() {
        arrayItems.clear();
    }

    public int getCount() {
        return this.arrayItems.size();
    }

    public Object getItem(int paramInt) {
        return this.arrayItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        Fila fila;
        View view;
        if (paramView == null) {
            fila = new Fila();
            ItemListDrawer item = arrayItems.get(paramInt);
            if (item.getIcono() != -1) {
                view = layoutInflater.inflate(R.layout.custom_item_list_drawer, null);
                fila.titulo = ((TextView) view.findViewById(R.id.tv_funcionalidad_drawer));
                fila.titulo.setText(item.getTitulo());
                fila.icono = ((ImageView) view.findViewById(R.id.iv_icono_drawer));
                fila.icono.setImageResource(item.getIcono());
                view.setTag(fila);
            } else { //Es una sección
                view = layoutInflater.inflate(R.layout.custom_separator_list_view, null);
            }
        } else {
            view = paramView;
        }
        return view;
    }

    public static class Fila {
        ImageView icono;
        TextView titulo;
    }
}