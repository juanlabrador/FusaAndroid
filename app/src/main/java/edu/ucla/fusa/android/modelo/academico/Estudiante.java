package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Estudiante extends Persona {

    private int id;
    private Usuario usuario;

    public Estudiante() {
        super();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
