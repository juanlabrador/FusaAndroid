package edu.ucla.fusa.android.modelo.fundacion;

import edu.ucla.fusa.android.modelo.academico.Catedra;
import edu.ucla.fusa.android.modelo.academico.Persona;

/**
 * Created by juanlabrador on 12/01/15.
 */
public class InstructorAspirante extends Persona {

    private int id;
    private Catedra catedra;

    public InstructorAspirante() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Catedra getCatedra() {
        return catedra;
    }

    public void setCatedra(Catedra catedra) {
        this.catedra = catedra;
    }
}
