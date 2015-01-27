package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class EstudiantePorClase {
    
    private ClaseParticular claseParticular;
    private Instructor instructor;
    private Horario horario;
    private AreaEstudio areaEstudio;

    public EstudiantePorClase() {
    }

    public ClaseParticular getClaseParticular() {
        return claseParticular;
    }

    public void setClaseParticular(ClaseParticular claseParticular) {
        this.claseParticular = claseParticular;
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
