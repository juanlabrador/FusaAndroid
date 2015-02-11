package edu.ucla.fusa.android.modelo.instrumentos;

import android.os.Parcel;
import android.os.Parcelable;

import edu.ucla.fusa.android.modelo.academico.Estudiante;

/**
 * Created by juanlabrador on 10/02/15.
 */
public class Notificacion implements Parcelable {
    
    private int id;
    private String titulo;
    private String descripcion;
    private long fecha;
    private boolean mensajeLeido;
    private String estatus;
    private TipoNotificacion tipoNotificacion;
    private Estudiante estudiante;

    public Notificacion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public boolean isMensajeLeido() {
        return mensajeLeido;
    }

    public void setMensajeLeido(boolean mensajeLeido) {
        this.mensajeLeido = mensajeLeido;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(titulo);
        parcel.writeLong(fecha);
        parcel.writeString(descripcion);
        parcel.writeString(estatus);
        parcel.writeValue(tipoNotificacion);
        parcel.writeString(String.valueOf(mensajeLeido));
    }
}
