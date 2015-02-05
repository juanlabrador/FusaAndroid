package edu.ucla.fusa.android.modelo.academico;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class ClaseParticular implements Parcelable {
    
    private int id;
    private Catedra catedra;
    private Instructor instructor;
    private List<HorarioArea> horarioArea;
    private LapsoAcademico lapso;
    private Nivel nivel;
    private String estatus;

    public ClaseParticular() {
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public LapsoAcademico getLapso() {
        return lapso;
    }

    public void setLapso(LapsoAcademico lapso) {
        this.lapso = lapso;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<HorarioArea> getHorarioArea() {
        return horarioArea;
    }

    public void setHorarioArea(List<HorarioArea> horarioArea) {
        this.horarioArea = horarioArea;
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
        parcel.writeValue(catedra);
        parcel.writeValue(instructor);
        parcel.writeList(horarioArea);
        parcel.writeValue(lapso);
        parcel.writeValue(nivel);
        parcel.writeString(estatus);
    }
}
