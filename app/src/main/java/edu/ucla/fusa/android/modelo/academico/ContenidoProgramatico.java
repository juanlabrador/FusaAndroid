package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 03/02/15.
 */
public class ContenidoProgramatico {
    
    private String descripcion;
    private String titulo;
    private TipoContenido tipoContenido;

    public ContenidoProgramatico() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoContenido getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(TipoContenido tipoContenido) {
        this.tipoContenido = tipoContenido;
    }
}
