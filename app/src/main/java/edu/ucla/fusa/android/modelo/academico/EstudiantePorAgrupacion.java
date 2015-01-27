package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class EstudiantePorAgrupacion {
    
    private Agrupacion agrupacion;
    private Instructor instructor;
    private Horario horario;
    private AreaEstudio areaEstudio;

    public EstudiantePorAgrupacion() {
    }

    public Agrupacion getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(Agrupacion agrupacion) {
        this.agrupacion = agrupacion;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public AreaEstudio getAreaEstudio() {
        return areaEstudio;
    }

    public void setAreaEstudio(AreaEstudio areaEstudio) {
        this.areaEstudio = areaEstudio;
    }
}
