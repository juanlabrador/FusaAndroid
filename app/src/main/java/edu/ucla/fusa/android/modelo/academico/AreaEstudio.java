package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class AreaEstudio {

    private int id;
    private String descripcion;

    public AreaEstudio(int id, String descripcion) {
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
