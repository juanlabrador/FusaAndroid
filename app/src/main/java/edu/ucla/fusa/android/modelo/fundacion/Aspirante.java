package edu.ucla.fusa.android.modelo.fundacion;

import edu.ucla.fusa.android.modelo.academico.Persona;
import edu.ucla.fusa.android.modelo.instrumentos.TipoInstrumento;

/**
 * Created by juanlabrador on 11/01/15.
 */
public class Aspirante extends Persona {

    private TipoInstrumento tipoInstrumento;
    private String instrumentoPropio;

    public Aspirante() {
    }

    public TipoInstrumento getTipoInstrumento() {
        return tipoInstrumento;
    }

    public void setTipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.tipoInstrumento = tipoInstrumento;
    }

    public String getInstrumentoPropio() {
        return instrumentoPropio;
    }

    public void setInstrumentoPropio(String instrumentoPropio) {
        this.instrumentoPropio = instrumentoPropio;
    }
}
