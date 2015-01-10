package edu.ucla.fusa.android.modelo.fundacion;

/**
 * Created by juanlabrador on 10/01/15.
 */
public class Municipio {

    private int id;
    private String descripcion;
    private Estado estado;
    private String estatus;

    public Municipio() {
    }

    public Municipio(int id, String descripcion, Estado estado, String estatus) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
