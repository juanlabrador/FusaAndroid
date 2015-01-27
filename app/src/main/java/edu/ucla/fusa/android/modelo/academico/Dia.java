package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class Dia {

    private int dia_id;
    private String descripcion;
    private String estatus;

    public Dia() {
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getDia_id() {
        return dia_id;
    }

    public void setDia_id(int dia_id) {
        this.dia_id = dia_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
