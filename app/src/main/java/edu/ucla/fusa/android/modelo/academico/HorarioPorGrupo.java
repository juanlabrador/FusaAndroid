package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class HorarioPorGrupo {

    private int id;
    private AreaEstudio areaEstudio;
    private Horario horario;
    private Grupo grupo;

    public HorarioPorGrupo(int id, AreaEstudio areaEstudio, Horario horario, Grupo grupo) {
        this.id = id;
        this.areaEstudio = areaEstudio;
        this.horario = horario;
        this.grupo = grupo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AreaEstudio getAreaEstudio() {
        return areaEstudio;
    }

    public void setAreaEstudio(AreaEstudio descripcion) {
        this.areaEstudio = areaEstudio;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
