package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class Dia {

    private int id;
    private String descripcion;

    public Dia(int id, String descripcion) {
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
