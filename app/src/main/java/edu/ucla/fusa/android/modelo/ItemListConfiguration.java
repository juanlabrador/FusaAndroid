package edu.ucla.fusa.android.modelo;

import edu.ucla.fusa.android.interfaces.Item;

/**
 * Created by juanlabrador on 20/10/14.
 *
 * Clase modelo que administra los elementos de la lista de configuración.
 *
 */
public class ItemListConfiguration implements Item {

    public final String title;
    public int icono;

    public ItemListConfiguration(String title, int icono) {
        this.title = title;
        this.icono = icono;
    }

    public String getTitle() {
        return title;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
