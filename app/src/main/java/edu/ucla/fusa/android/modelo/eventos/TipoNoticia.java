package edu.ucla.fusa.android.modelo.eventos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanlabrador on 05/12/14.
 */
public class TipoNoticia {

    private long id;
    private String descripcion;
    private String estatus;

    public TipoNoticia(long id, String descripcion, String estatus) {
        this.id = id;
        this.descripcion = descripcion;
        this.estatus = estatus;
    }

    public TipoNoticia() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
