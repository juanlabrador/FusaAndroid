package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 05/02/15.
 */
public class ObjetivoPorClase {
    
    private int id;
    private ContenidoProgramatico contenidoProgramatico;
    private String descripcion;
    private String estatus;
    private EstadoCumplimiento estadoCumplimiento;

    public ObjetivoPorClase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContenidoProgramatico getContenidoProgramatico() {
        return contenidoProgramatico;
    }

    public void setContenidoProgramatico(ContenidoProgramatico contenidoProgramatico) {
        this.contenidoProgramatico = contenidoProgramatico;
    }

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

    public EstadoCumplimiento getEstadoCumplimiento() {
        return estadoCumplimiento;
    }

    public void setEstadoCumplimiento(EstadoCumplimiento estadoCumplimiento) {
        this.estadoCumplimiento = estadoCumplimiento;
    }
}
