package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class EstudiantePorAgrupacion {
    
    private int id;
    private Estudiante estudiante;
    private LapsoAcademico lapsoAcademico;

    public EstudiantePorAgrupacion() {
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

    public LapsoAcademico getLapsoAcademico() {
        return lapsoAcademico;
    }

    public void setLapsoAcademico(LapsoAcademico lapsoAcademico) {
        this.lapsoAcademico = lapsoAcademico;
    }
}
