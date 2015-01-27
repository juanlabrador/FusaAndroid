package edu.ucla.fusa.android.modelo.academico;

import java.util.List;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class ClaseParticular {
    
    private int id;
    private Catedra catedra;
    private Instructor instructor;
    private List<HorarioArea> horarioArea;

    public ClaseParticular() {
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
}
