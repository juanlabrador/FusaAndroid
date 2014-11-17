package edu.ucla.fusa.android.modelo;

import edu.ucla.fusa.android.interfaces.Item;

public class HeaderListConfiguracion implements Item {

    private int icono;
    private String titulo;

    public HeaderListConfiguracion(String paramString, int paramInt) {
        this.titulo = paramString;
        this.icono = paramInt;
    }

    public int getIcono() {
        return this.icono;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public boolean isSection() {
        return true;
    }

    public void setIcono(int paramInt) {
        this.icono = paramInt;
    }

    public void setTitulo(String paramString) {
        this.titulo = paramString;
    }
}