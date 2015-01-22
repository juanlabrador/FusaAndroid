package edu.ucla.fusa.android.modelo.academico;

import edu.ucla.fusa.android.modelo.seguridad.Usuario;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Estudiante extends Persona {

    private int id;
    private String becado;
    private String inscritoConservatorio;
    private String inscritoCoro;
    private String instrumentoPropio;
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getBecado() {
        return becado;
    }

    public void setBecado(String becado) {
        this.becado = becado;
    }

    public String getInscritoConservatorio() {
        return inscritoConservatorio;
    }

    public void setInscritoConservatorio(String inscritoConservatorio) {
        this.inscritoConservatorio = inscritoConservatorio;
    }

    public String getInscritoCoro() {
        return inscritoCoro;
    }

    public void setInscritoCoro(String inscritoCoro) {
        this.inscritoCoro = inscritoCoro;
    }

    public String getInstrumentoPropio() {
        return instrumentoPropio;
    }

    public void setInstrumentoPropio(String instrumentoPropio) {
        this.instrumentoPropio = instrumentoPropio;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

}
