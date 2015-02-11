package edu.ucla.fusa.android.modelo.instrumentos;

import android.os.Parcel;
import android.os.Parcelable;

import edu.ucla.fusa.android.modelo.academico.Agrupacion;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.evento.Evento;

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
    private Prestamo prestamo;
    private SolicitudPrestamo solicitud;
    private Evento evento;
    private Agrupacion agrupacion;

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

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public SolicitudPrestamo getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudPrestamo solicitud) {
        this.solicitud = solicitud;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Agrupacion getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(Agrupacion agrupacion) {
        this.agrupacion = agrupacion;
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
        parcel.writeValue(estudiante);
        parcel.writeValue(prestamo);
        parcel.writeValue(solicitud);
        parcel.writeValue(agrupacion);
        parcel.writeString(String.valueOf(mensajeLeido));
    }
}
