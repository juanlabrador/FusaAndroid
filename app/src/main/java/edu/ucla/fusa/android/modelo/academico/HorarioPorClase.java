package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 05/02/15.
 */
public class HorarioPorClase {
    
    private int id;
    private ClaseParticular clase;
    private HorarioArea horario;

    public HorarioPorClase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ClaseParticular getClase() {
        return clase;
    }

    public void setClase(ClaseParticular clase) {
        this.clase = clase;
    }

    public HorarioArea getHorario() {
        return horario;
    }

    public void setHorario(HorarioArea horario) {
        this.horario = horario;
    }
}
