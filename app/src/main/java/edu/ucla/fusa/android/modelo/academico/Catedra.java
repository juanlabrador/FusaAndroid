package edu.ucla.fusa.android.modelo.academico;

import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;

/**
 * Created by juanlabrador on 11/01/15.
 */
public class Catedra {

    private int id;
    private String descripcion;
    private TipoInstrumento tipoInstrumento;

    public Catedra() {
    }

    public TipoInstrumento getTipoInstrumento() {
        return tipoInstrumento;
    }

    public void setTipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.tipoInstrumento = tipoInstrumento;
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
