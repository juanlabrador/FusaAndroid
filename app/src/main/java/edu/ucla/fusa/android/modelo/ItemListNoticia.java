package edu.ucla.fusa.android.modelo;

import edu.ucla.fusa.android.interfaces.Item;

/**
 * Created by juanlabrador on 20/10/14.
 *
 * Clase modelo que administra los elementos de la lista de noticias.
 *
 */
public class ItemListNoticia {

    public String titulo;
    public String fecha;
    public int imagen;

    public ItemListNoticia(String titulo, String fecha, int imagen) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
