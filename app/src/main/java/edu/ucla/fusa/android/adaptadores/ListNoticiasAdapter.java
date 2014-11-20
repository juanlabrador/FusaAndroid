package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import java.util.ArrayList;

public class ListNoticiasAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList<ItemListNoticia> arrayItems;
    private Fila fila;
    private ListFragment fragment;
    private ItemListNoticia item;

    public ListNoticiasAdapter(Activity paramActivity, ListFragment paramListFragment, ArrayList<ItemListNoticia> paramArrayList) {
        this.activity = paramActivity;
        this.arrayItems = paramArrayList;
        this.fragment = paramListFragment;
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
        if (paramView == null) {
          this.fila = new Fila();
          paramView = layoutInflater.inflate(R.layout.custom_item_list_noticias, null);
          this.fila.titulo = ((TextView) paramView.findViewById(R.id.tv_titulo_noticia));
          this.fila.fecha = ((TextView) paramView.findViewById(R.id.tv_fecha_publicacion_noticia));
          this.fila.imagen = ((ImageView) paramView.findViewById(R.id.iv_foto_noticia));
          this.fila.imagen.setOnClickListener(this);
          this.fila.boton = ((ImageView) paramView.findViewById(R.id.btn_compartir_noticia));
          this.fila.boton.setOnClickListener(this);
          paramView.setTag(this.fila);
        } else {
            this.fila = ((Fila) paramView.getTag());
            this.item = this.arrayItems.get(paramInt);
            this.fila.titulo.setText(this.item.getTitulo());
            this.fila.fecha.setText(this.item.getFecha());
            this.fila.imagen.setImageResource(this.item.getImagen());
        }
        return paramView;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_compartir_noticia:
                Toast.makeText(this.activity, "En construcción...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static class Fila {
        ImageView boton;
        TextView fecha;
        ImageView imagen;
        TextView titulo;
    }
}
