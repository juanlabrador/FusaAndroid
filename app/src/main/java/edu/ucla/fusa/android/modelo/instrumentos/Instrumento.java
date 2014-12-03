package edu.ucla.fusa.android.modelo.instrumentos;

/**
 * Created by juanlabrador on 27/11/14.
 */
public class Instrumento {

    private int id;
    private String descripcion;

    public Instrumento(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
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
}
