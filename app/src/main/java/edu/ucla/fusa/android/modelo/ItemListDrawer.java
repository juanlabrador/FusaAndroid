package edu.ucla.fusa.android.modelo;

public class ItemListDrawer {

    private int icono;
    private String titulo;

    public ItemListDrawer(String paramString, int paramInt) {
        this.titulo = paramString;
        this.icono = paramInt;
    }

    public int getIcono() {
        return this.icono;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setIcono(int paramInt) {
        this.icono = paramInt;
    }

    public void setTitulo(String paramString) {
        this.titulo = paramString;
    }
}