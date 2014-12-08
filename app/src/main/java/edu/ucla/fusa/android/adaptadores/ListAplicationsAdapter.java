package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListAplications;
import java.util.ArrayList;

public class ListAplicationsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemListAplications> arrayItems;

    public ListAplicationsAdapter(Activity paramActivity, ArrayList<ItemListAplications> paramArrayList) {
        this.activity = paramActivity;
        this.arrayItems = paramArrayList;
    }

    public int getCount() {
        return arrayItems.size();
    }

    public Object getItem(int paramInt) {
        return arrayItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        Fila fila;

        if (paramView == null) {
            fila = new Fila();
            View view = layoutInflater.inflate(R.layout.custom_item_list_aplicaciones, null);
            fila.titulo = (TextView) view.findViewById(R.id.tvTituloApp);
            fila.icono = (ImageView) view.findViewById(R.id.ivIconoApp);
            fila.check = (ImageView) view.findViewById(R.id.ivIconoActivacionApp);

            Log.i("NUMERO", String.valueOf(paramInt));
            ItemListAplications item = arrayItems.get(paramInt);
            fila.titulo.setText(item.getApp());
            fila.icono.setImageResource(item.getIcon());
            if (item.isConnected()) {
                fila.check.setVisibility(View.VISIBLE);
                fila.titulo.setTextColor(activity.getResources().getColor(android.R.color.holo_blue_light));
            } else {
                fila.check.setVisibility(View.INVISIBLE);
                fila.titulo.setTextColor(activity.getResources().getColor(R.color.gris_oscuro));
            }

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
        ImageView check;
    }
}