package edu.ucla.fusa.android.modelo;

/**
 * Created by juanlabrador on 19/10/14.
 *
 * Clase modelo que administra los elementos de la lista de aplicaciones.
 */
public class ItemListAplications {

    private String titulo;
    private int icono;

    public ItemListAplications(String titulo, int icono) {
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
