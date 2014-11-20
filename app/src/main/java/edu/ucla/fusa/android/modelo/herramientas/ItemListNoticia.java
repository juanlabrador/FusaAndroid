package edu.ucla.fusa.android.modelo.herramientas;

public class ItemListNoticia {
    public String descripcion;
    public String fecha;
    public int id;
    public int imagen;
    public String titulo;

    public ItemListNoticia(int paramInt1, String paramString1, String paramString2, int paramInt2, String paramString3) {
        this.id = paramInt1;
        this.titulo = paramString1;
        this.fecha = paramString2;
        this.imagen = paramInt2;
        this.descripcion = paramString3;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public String getFecha() {
        return this.fecha;
    }

    public int getId() {
        return this.id;
    }

    public int getImagen() {
        return this.imagen;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setDescripcion(String paramString) {
        this.descripcion = paramString;
    }

    public void setFecha(String paramString) {
        this.fecha = paramString;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }

    public void setImagen(int paramInt) {
        this.imagen = paramInt;
    }

    public void setTitulo(String paramString) {
        this.titulo = paramString;
    }
}