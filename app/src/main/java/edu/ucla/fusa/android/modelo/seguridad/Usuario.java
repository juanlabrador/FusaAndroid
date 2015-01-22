package edu.ucla.fusa.android.modelo.seguridad;

import java.util.Calendar;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Usuario {

    private transient int id;
    private String username;
    private String password;
    private byte[] foto;
    private TipoUsuario tipoUsuario;
    private String estatus;
    private Calendar fechaCreacion;
    private Calendar ultimoAcceso;

    public Usuario() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estado) {
        this.estatus = estado;
    }

    public Calendar getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Calendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Calendar getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Calendar ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
}