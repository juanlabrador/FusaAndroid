package edu.ucla.fusa.android.modelo.academico;


/**
 * Created by juanlabrador on 26/01/15.
 */
public class HorarioArea {
    
    private int id;
    private Horario horario;
    private AreaEstudio areaEstudio;

    public HorarioArea() {
    }

    public HorarioArea(int id, Horario horario, AreaEstudio areaEstudio) {
        this.id = id;
        this.horario = horario;
        this.areaEstudio = areaEstudio;
    }

    public AreaEstudio getAreaEstudio() {
        return areaEstudio;
    }

    public void setAreaEstudio(AreaEstudio areaEstudio) {
        this.areaEstudio = areaEstudio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }
}
