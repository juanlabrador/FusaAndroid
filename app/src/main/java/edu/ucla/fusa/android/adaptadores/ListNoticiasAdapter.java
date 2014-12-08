package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListNoticiasAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList<ItemListNoticia> arrayItems;
    private Fila fila;
    private ItemListNoticia item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");

    public ListNoticiasAdapter(Activity paramActivity, ArrayList<ItemListNoticia> paramArrayList) {
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

    public static class Fila {
        ImageView boton;
        TextView fecha;
        ImageView imagen;
        TextView titulo;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (paramView == null) {
          fila = new Fila();
          paramView = layoutInflater.inflate(R.layout.custom_item_list_noticias, null);
          fila.titulo = ((TextView) paramView.findViewById(R.id.tv_titulo_noticia));
          fila.fecha = ((TextView) paramView.findViewById(R.id.tv_fecha_publicacion_noticia));
          fila.imagen = ((ImageView) paramView.findViewById(R.id.iv_foto_noticia));
          fila.imagen.setOnClickListener(this);
          fila.boton = ((ImageView) paramView.findViewById(R.id.btn_compartir_noticia));
          fila.boton.setOnClickListener(this);
          paramView.setTag(fila);
        } else {
            fila = ((Fila) paramView.getTag());
            item = arrayItems.get(paramInt);
            fila.titulo.setText(item.getTitulo());
            fila.fecha.setText(dateFormat.format(item.getFecha()));
            if (item.getImagen() != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bmp = BitmapFactory.decodeByteArray(item.getImagen(), 0, item.getImagen().length, options);
                fila.imagen.setImageBitmap(bmp);
            } else {
                fila.imagen.setVisibility(View.GONE);
            }
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

}
