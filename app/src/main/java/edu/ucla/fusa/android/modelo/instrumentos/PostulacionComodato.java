package edu.ucla.fusa.android.modelo.instrumentos;

import java.util.Calendar;

import edu.ucla.fusa.android.modelo.academico.Estudiante;

/**
 * Created by juanlabrador on 27/11/14.
 */
public class PostulacionComodato {

    private int id;
    private Calendar fechaEmision;
    private Calendar fechaVencimiento;
    private Estudiante estudiante;
    private Instrumento instrumento;

    public PostulacionComodato(int id, Calendar fechaEmision, Calendar fechaVencimiento, Estudiante estudiante, Instrumento instrumento) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estudiante = estudiante;
        this.instrumento = instrumento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Calendar fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Calendar getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Calendar fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }
}
