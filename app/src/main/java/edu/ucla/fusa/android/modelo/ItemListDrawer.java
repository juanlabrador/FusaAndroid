package edu.ucla.fusa.android.modelo;

/**
 * Created by juanlabrador on 19/10/14.
 */
public class ItemListDrawer {

    private String titulo;
    private int icono;

    public ItemListDrawer(String titulo, int icono) {
        this.titulo = titulo;
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
