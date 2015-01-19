package edu.ucla.fusa.android.modelo.evento;

/**
 * Created by juanlabrador on 17/01/15.
 */
public class Lugar {
    
    private int id;
    private String descripcion;
    private String direccion;
    private String estatus;

    public Lugar() {
    }

    public Lugar(int id, String descripcion, String direccion, String estatus) {
        this.id = id;
        this.descripcion = descripcion;
        this.direccion = direccion;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
