package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 03/02/15.
 */
public class ObjetivoPorAgrupacion {

    private String descripcion;
    private ContenidoProgramatico contenidoProgramatico;

    public ObjetivoPorAgrupacion() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ContenidoProgramatico getContenidoProgramatico() {
        return contenidoProgramatico;
    }

    public void setContenidoProgramatico(ContenidoProgramatico contenidoProgramatico) {
        this.contenidoProgramatico = contenidoProgramatico;
    }
}
