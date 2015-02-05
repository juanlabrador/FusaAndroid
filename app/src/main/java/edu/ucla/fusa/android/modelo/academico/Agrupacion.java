package edu.ucla.fusa.android.modelo.academico;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class Agrupacion implements Parcelable {
    
    private int id;
    private String descripcion;
    private TipoAgrupacion tipoAgrupacion;
    private List<HorarioArea> horarioArea;
    private Instructor instructor;

    public Agrupacion() {
    }

    public int getId() {
        return id;
    }

    public List<HorarioArea> getHorarioArea() {
        return horarioArea;
    }

    public void setHorarioArea(List<HorarioArea> mHorarioArea) {
        this.horarioArea = mHorarioArea;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor mInstructor) {
        this.instructor = mInstructor;
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

    public TipoAgrupacion getTipoAgrupacion() {
        return tipoAgrupacion;
    }

    public void setTipoAgrupacion(TipoAgrupacion tipoAgrupacion) {
        this.tipoAgrupacion = tipoAgrupacion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(descripcion);
        parcel.writeValue(tipoAgrupacion);
        parcel.writeList(horarioArea);
        parcel.writeValue(instructor);
    }
}
