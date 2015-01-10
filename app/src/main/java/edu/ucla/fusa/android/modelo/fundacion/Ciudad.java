package edu.ucla.fusa.android.modelo.fundacion;

/**
 * Created by juanlabrador on 10/01/15.
 */
public class Ciudad {

    private int id;
    private String descripcion;
    private Municipio municipio;
    private String estatus;

    public Ciudad() {
    }

    public Ciudad(int id, String descripcion, Municipio municipio, String estatus) {
        this.id = id;
        this.descripcion = descripcion;
        this.municipio = municipio;
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

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
