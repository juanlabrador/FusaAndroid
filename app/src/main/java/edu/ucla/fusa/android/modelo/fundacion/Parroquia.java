package edu.ucla.fusa.android.modelo.fundacion;

/**
 * Created by juanlabrador on 10/01/15.
 */
public class Parroquia {

    private int id;
    private Ciudad ciudad;
    private String descripcion;
    private String estatus;

    public Parroquia() {
    }

    public Parroquia(int id, Ciudad ciudad, String descripcion, String estatus) {
        this.id = id;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.estatus = estatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
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
