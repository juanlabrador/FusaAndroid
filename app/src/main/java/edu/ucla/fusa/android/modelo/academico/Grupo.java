package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 25/11/14.
 *
 * Será mi clase que agrupara tanto catedra como agrupación
 */
public class Grupo {

    private int id;
    private String descripcion;
    private Instructor instructor;
    private int tipo;

    public Grupo(int id, String descripcion, Instructor instructor, int tipo) {
        this.id = id;
        this.descripcion = descripcion;
        this.instructor = instructor;
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
