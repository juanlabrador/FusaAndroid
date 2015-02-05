package edu.ucla.fusa.android.modelo.instrumentos;

import android.os.Parcel;
import android.os.Parcelable;

import edu.ucla.fusa.android.modelo.academico.EstudiantePorAgrupacion;

/**
 * Created by juanlabrador on 12/01/15.
 */
public class SolicitudPrestamo implements Parcelable {

    private int id;
    private EstudiantePorAgrupacion estudiantePorAgrupacion;
    private TipoInstrumento tipoInstrumento;
    private TipoPrestamo tipoPrestamo;
    private String fechaEmision;
    private String fechaVencimiento;
    private String estatus;

    public SolicitudPrestamo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EstudiantePorAgrupacion getEstudiantePorAgrupacion() {
        return estudiantePorAgrupacion;
    }

    public void setEstudiantePorAgrupacion(EstudiantePorAgrupacion estudiantePorAgrupacion) {
        this.estudiantePorAgrupacion = estudiantePorAgrupacion;
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

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeValue(estudiantePorAgrupacion);
        parcel.writeValue(tipoInstrumento);
        parcel.writeValue(tipoPrestamo);
        parcel.writeString(fechaEmision);
        parcel.writeString(fechaVencimiento);
        parcel.writeString(estatus);
    }
}
