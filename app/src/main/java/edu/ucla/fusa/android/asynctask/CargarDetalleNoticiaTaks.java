package edu.ucla.fusa.android.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ucla.fusa.android.R;

public class CargarDetalleNoticiaTaks extends AsyncTask<Void, Void, Void> {

    private Bundle arguments;
    private TextView descripcion;
    private TextView fecha;
    private ImageView imagen;
    private TextView titulo;
    private View view;

    public CargarDetalleNoticiaTaks(Bundle paramBundle, View paramView) {
        this.arguments = paramBundle;
        this.view = paramView;
    }

    protected Void doInBackground(Void[] paramArrayOfVoid) {
        return null;
    }

    protected void onPostExecute(Void paramVoid) {
        super.onPostExecute(paramVoid);
        this.titulo.setText(this.arguments.getString("titulo_noticia", ""));
        this.fecha.setText(this.arguments.getString("fecha_noticia", ""));
        this.imagen.setImageResource(this.arguments.getInt("imagen_noticia", -1));
        this.descripcion.setText(this.arguments.getString("descripcion_noticia", ""));
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.titulo = ((TextView)this.view.findViewById(R.id.tv_titulo_noticia_detalle));
        this.fecha = ((TextView)this.view.findViewById(R.id.tv_fecha_publicacion_noticia_detalle));
        this.imagen = ((ImageView)this.view.findViewById(R.id.iv_foto_noticia_detalle));
        this.descripcion = ((TextView)this.view.findViewById(R.id.tv_descripcion_noticia_detalle));
    }
}
