package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/01/15.
 */
public class EstudiantePorClase {
    
    private int id;
    private ClaseParticular clase;

    public EstudiantePorClase() {
    }

    public ClaseParticular getClase() {
        return clase;
    }

    public void setClase(ClaseParticular clase) {
        this.clase = clase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
