package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.ItemListDrawer;
import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemListDrawer> arrayItems;

    public NavigationAdapter(Activity paramActivity, ArrayList<ItemListDrawer> paramArrayList) {
        this.activity = paramActivity;
        this.arrayItems = paramArrayList;
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
        LayoutInflater layoutInflater = this.activity.getLayoutInflater();
        Fila fila;
        if (paramView == null) {
            fila = new Fila();
            ItemListDrawer item = this.arrayItems.get(paramInt);
            View view = layoutInflater.inflate(R.layout.custom_item_list_drawer, null);
            fila.titulo = ((TextView) view.findViewById(R.id.tv_funcionalidad_drawer));
            fila.titulo.setText(item.getTitulo());
            fila.icono = ((ImageView) view.findViewById(R.id.iv_icono_drawer));
            fila.icono.setImageResource(item.getIcono());
            view.setTag(fila);
            return view;
        } else {
            fila = (Fila) paramView.getTag();
        }
        return paramView;
    }

    public static class Fila {
        ImageView icono;
        TextView titulo;
    }
}