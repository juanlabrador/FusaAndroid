package edu.ucla.fusa.android.modelo.instrumentos;

import java.util.Date;

/**
 * Created by juanlabrador on 22/01/15.
 */
public class Prestamo {
    
    private int id;
    private String estatus;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private SolicitudPrestamo solicitudPrestamo;
    private Instrumento instrumento;

    public Prestamo() {
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
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

    public SolicitudPrestamo getSolicitudPrestamo() {
        return solicitudPrestamo;
    }

    public void setSolicitudPrestamo(SolicitudPrestamo solicitudPrestamo) {
        this.solicitudPrestamo = solicitudPrestamo;
    }
}
