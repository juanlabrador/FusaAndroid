package edu.ucla.fusa.android.modelo.herramientas;

import java.util.Date;

public class ItemListComentario {

    public String comentario;
    public Date fecha;
    public int foto;
    public String nombre;
    public int puntuacion;

    public ItemListComentario(int paramInt1, String paramString1, int paramInt2, Date paramDate, String paramString2) {
        this.foto = paramInt1;
        this.nombre = paramString1;
        this.puntuacion = paramInt2;
        this.fecha = paramDate;
        this.comentario = paramString2;
    }

    public String getComentario() {
        return this.comentario;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public int getFoto() {
        return this.foto;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getPuntuacion() {
        return this.puntuacion;
    }

    public void setComentario(String paramString) {
        this.comentario = paramString;
    }

    public void setFecha(Date paramDate) {
        this.fecha = paramDate;
    }

    public void setFoto(int paramInt) {
        this.foto = paramInt;
    }

    public void setNombre(String paramString) {
        this.nombre = paramString;
    }

    public void setPuntuacion(int paramInt) {
        this.puntuacion = paramInt;
    }
}