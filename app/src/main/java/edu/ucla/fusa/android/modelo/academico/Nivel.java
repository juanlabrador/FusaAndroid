package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 05/02/15.
 */
public class Nivel {
    
    private int id;
    private String descripcion;
    private String edadFinal;
    private String edadInicial;
    private String estatus;

    public Nivel() {
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

    public String getEdadFinal() {
        return edadFinal;
    }

    public void setEdadFinal(String edadFinal) {
        this.edadFinal = edadFinal;
    }

    public String getEdadInicial() {
        return edadInicial;
    }

    public void setEdadInicial(String edadInicial) {
        this.edadInicial = edadInicial;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
