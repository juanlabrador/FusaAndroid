package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 02/02/15.
 */
public class LapsoAcademico {
    
    private int id;
    private int lapso;
    private String estatus;

    public LapsoAcademico() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLapso() {
        return lapso;
    }

    public void setLapso(int lapso) {
        this.lapso = lapso;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
