package edu.ucla.fusa.android.modelo.academico;

import java.util.Calendar;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Usuario {

    private transient int id;
    private String nombre;
    private String password;
    private byte[] foto;
    private TipoUsuario tipoUsuario;
    private String estatus;
    private Calendar fechaCreacion;
    private Calendar ultimoAcceso;

    public Usuario() {}

    public Usuario(int id, String nombre, String password, byte[] foto,
                   TipoUsuario tipoUsuario, String estatus,
                   Calendar fechaCreacion, Calendar ultimoAcceso) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.foto = foto;
        this.tipoUsuario = tipoUsuario;
        this.estatus = estatus;
        this.fechaCreacion = fechaCreacion;
        this.ultimoAcceso = ultimoAcceso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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