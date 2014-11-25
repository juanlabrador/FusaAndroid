package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class Instructor extends Persona {

    private int id;
    private String nombre;

    public Instructor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
