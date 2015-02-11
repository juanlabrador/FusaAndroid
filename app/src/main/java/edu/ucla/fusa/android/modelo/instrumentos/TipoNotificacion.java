package edu.ucla.fusa.android.modelo.instrumentos;

/**
 * Created by juanlabrador on 10/02/15.
 */
public class TipoNotificacion {
    
    private int id;
    private String descripcion;
    private String estatus;

    public TipoNotificacion() {
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
