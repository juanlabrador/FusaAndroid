package edu.ucla.fusa.android.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.herramientas.ItemListComentario;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListComentariosAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemListComentario> arrayItems;
    private Date date;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private ItemListComentario item;

    public ListComentariosAdapter(Activity paramActivity, ArrayList<ItemListComentario> paramArrayList) {
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
        LayoutInflater layoutInflater = this.activity.getLayoutInflater();
        Fila fila;
        if (paramView == null) {
            fila = new Fila();
            paramView = layoutInflater.inflate(R.layout.custom_item_list_comentario, null);
            fila.foto = ((CircleImageView)paramView.findViewById(R.id.iv_foto_perfil_comentario));
            fila.nombre = ((TextView)paramView.findViewById(R.id.tv_usuario_comentario));
            fila.puntuacion = ((RatingBar)paramView.findViewById(R.id.rb_puntuacion_comentario));
            fila.fecha = ((TextView)paramView.findViewById(R.id.tv_fecha_comentario));
            fila.comentario = ((TextView)paramView.findViewById(R.id.tv_comentario));
            paramView.setTag(fila);
        } else {
            fila = (Fila) paramView.getTag();
            item = arrayItems.get(paramInt);
            fila.nombre.setText(this.item.getNombre());
            fila.foto.setImageResource(this.item.getFoto());
            fila.puntuacion.setRating(this.item.getPuntuacion());
            fila.fecha.setText(this.format.format(this.item.getFecha()));
            fila.comentario.setText(this.item.getComentario());
        }
        return paramView;
    }

    public static class Fila {
        TextView comentario;
        TextView fecha;
        CircleImageView foto;
        TextView nombre;
        RatingBar puntuacion;
    }
}