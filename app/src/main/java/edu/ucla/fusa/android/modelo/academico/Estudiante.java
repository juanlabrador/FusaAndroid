package edu.ucla.fusa.android.modelo.academico;

import edu.ucla.fusa.android.modelo.seguridad.Usuario;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Estudiante extends Persona {

    private Usuario usuario;
    private String estatus;

    public Estudiante() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

}
