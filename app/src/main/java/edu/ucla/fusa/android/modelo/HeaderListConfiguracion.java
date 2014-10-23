package edu.ucla.fusa.android.modelo;

import edu.ucla.fusa.android.interfaces.Item;

/**
 * Created by juanlabrador on 19/10/14.
 *
 * Clase modelo que maneja los elementos de cabecera de la lista
 */
public class HeaderListConfiguracion implements Item {

    private String titulo;
    private int icono;

    public HeaderListConfiguracion(String titulo, int icono) {
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

    @Override
    public boolean isSection() {
        return true;
    }
}
