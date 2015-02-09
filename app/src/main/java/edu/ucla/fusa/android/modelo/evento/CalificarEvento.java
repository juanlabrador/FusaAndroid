package edu.ucla.fusa.android.modelo.evento;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.ucla.fusa.android.modelo.seguridad.Usuario;

/**
 * Created by juanlabrador on 07/02/15.
 */
public class CalificarEvento implements Parcelable {
    
    private int id;
    private int ponderacion;
    private long fechaPublicacion;
    private String comentario;
    private String correo;
    private String estatus;
    private Evento evento;
    private Usuario usuario;

    public CalificarEvento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(int ponderacion) {
        this.ponderacion = ponderacion;
    }

    public long getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(long fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(ponderacion);
        parcel.writeLong(fechaPublicacion);
        parcel.writeString(comentario);
        parcel.writeString(correo);
        parcel.writeString(estatus);
        parcel.writeValue(evento);
        parcel.writeValue(usuario);
    }
}
