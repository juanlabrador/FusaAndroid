package edu.ucla.fusa.android.modelo;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by juanlabrador on 20/10/14.
 *
 * Clase modelo que administra los elementos de la lista de noticias.
 *
 */
public class ItemListComentario {

    public int foto;
    public String nombre;
    public int puntuacion;
    public Date fecha;
    public String comentario;

    public ItemListComentario(int foto, String nombre, int puntuacion, Date fecha, String comentario) {
        this.foto = foto;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
        this.comentario = comentario;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
