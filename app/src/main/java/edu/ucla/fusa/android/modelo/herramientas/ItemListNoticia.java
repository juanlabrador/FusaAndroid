package edu.ucla.fusa.android.modelo.herramientas;

import java.util.Date;

public class ItemListNoticia {

    public String descripcion;
    public Date fecha;
    public long id;
    public byte[] imagen;
    public String titulo;
    public int haveFoto;

    public ItemListNoticia() {}

    public ItemListNoticia(long id, String titulo, Date fecha, byte[] imagen, String descripcion, int haveFoto) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.id = id;
        this.imagen = imagen;
        this.titulo = titulo;
        this.haveFoto = haveFoto;
    }

    public int getHaveFoto() {
        return haveFoto;
    }

    public void setHaveFoto(int haveFoto) {
        this.haveFoto = haveFoto;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public long getId() {
        return this.id;
    }

    public byte[] getImagen() {
        return this.imagen;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setId(long paramInt) {
        this.id = paramInt;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}