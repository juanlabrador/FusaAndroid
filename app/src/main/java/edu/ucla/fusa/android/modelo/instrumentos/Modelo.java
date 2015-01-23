package edu.ucla.fusa.android.modelo.instrumentos;

/**
 * Created by juanlabrador on 22/01/15.
 */
public class Modelo {
    
    private int id;
    private String descripcion;
    private String estatus;
    private Marca marca;

    public Modelo() {
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

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}
