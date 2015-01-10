package edu.ucla.fusa.android.modelo.fundacion;

import java.util.Calendar;

/**
 * Created by juanlabrador on 05/12/14.
 */
public class Noticia {

    private String descripcion;
    private String estatus;
    private Calendar fechapublicacion;
    private long id;
    private byte[] imagen;
    private String nombreImagen;
    private TipoNoticia tipoNoticia;
    private String titulo;

    public Noticia(String descripcion, String estatus, Calendar fechapublicacion, long id, byte[] imagen, String nombreImagen, TipoNoticia tipoNoticia, String titulo) {
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.fechapublicacion = fechapublicacion;
        this.id = id;
        this.imagen = imagen;
        this.nombreImagen = nombreImagen;
        this.tipoNoticia = tipoNoticia;
        this.titulo = titulo;
    }

    public Noticia() {}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Calendar getFechapublicacion() {
        return fechapublicacion;
    }

    public void setFechapublicacion(Calendar fechapublicacion) {
        this.fechapublicacion = fechapublicacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public TipoNoticia getTipoNoticia() {
        return tipoNoticia;
    }

    public void setTipoNoticia(TipoNoticia tipoNoticia) {
        this.tipoNoticia = tipoNoticia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
