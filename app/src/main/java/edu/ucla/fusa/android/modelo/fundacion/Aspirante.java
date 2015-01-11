package edu.ucla.fusa.android.modelo.fundacion;

import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.academico.Persona;

/**
 * Created by juanlabrador on 11/01/15.
 */
public class Aspirante extends Persona {

    private Catedra catedra;
    private String instrumentoPropio;

    public Aspirante() {
    }

    public Catedra getCatedra() {
        return catedra;
    }

    public void setCatedra(Catedra catedra) {
        this.catedra = catedra;
    }

    public String getInstrumentoPropio() {
        return instrumentoPropio;
    }

    public void setInstrumentoPropio(String instrumentoPropio) {
        this.instrumentoPropio = instrumentoPropio;
    }
}
