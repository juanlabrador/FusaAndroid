package edu.ucla.fusa.android.modelo.herramientas;

public class ItemListOpcionesMultimedia {

    public int icono;
    public final String title;

    public ItemListOpcionesMultimedia(String paramString, int paramInt) {
        this.title = paramString;
        this.icono = paramInt;
    }

    public int getIcono() {
        return this.icono;
    }

    public String getTitle() {
        return this.title;
    }

    public void setIcono(int paramInt) {
        this.icono = paramInt;
    }
}