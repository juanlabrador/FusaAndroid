package edu.ucla.fusa.android.modelo.fundacion;

/**
 * Created by juanlabrador on 05/12/14.
 */
public class Noticia {

    private String descripcion;
    private String estatus;
    private String fechapublicacion;
    private long id;
    private byte[] imagen;
    private String nombreImagen;
    private TipoNoticia tipoNoticia;
    private String titulo;


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

    public String getFechapublicacion() {
        return fechapublicacion;
    }

    public void setFechapublicacion(String fechapublicacion) {
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
