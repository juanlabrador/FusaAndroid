package edu.ucla.fusa.android.modelo.instrumentos;

/**
 * Created by juanlabrador on 12/01/15.
 */
public class TipoPrestamo {

    private int id;
    private String descripcion;
    private String estatus;

    public TipoPrestamo() {
    }

    public TipoPrestamo(int id, String descripcion, String estatus) {
        this.id = id;
        this.descripcion = descripcion;
        this.estatus = estatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
