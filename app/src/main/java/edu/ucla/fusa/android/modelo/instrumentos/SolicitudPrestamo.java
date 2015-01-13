package edu.ucla.fusa.android.modelo.instrumentos;

import java.util.Date;

import edu.ucla.fusa.android.modelo.academico.Estudiante;

/**
 * Created by juanlabrador on 12/01/15.
 */
public class SolicitudPrestamo {

    private int id;
    private Estudiante estudiante;
    private TipoInstrumento tipoInstrumento;
    private TipoPrestamo tipoPrestamo;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private String estatus;

    public SolicitudPrestamo() {
    }

    public SolicitudPrestamo(int id, Estudiante estudiante, TipoInstrumento tipoInstrumento, TipoPrestamo tipoPrestamo, Date fechaEmision, Date fechaVencimiento, String estatus) {
        this.id = id;
        this.estudiante = estudiante;
        this.tipoInstrumento = tipoInstrumento;
        this.tipoPrestamo = tipoPrestamo;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estatus = estatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public TipoInstrumento getTipoInstrumento() {
        return tipoInstrumento;
    }

    public void setTipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.tipoInstrumento = tipoInstrumento;
    }

    public TipoPrestamo getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(TipoPrestamo tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
