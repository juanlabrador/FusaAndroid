package edu.ucla.fusa.android.modelo.evento;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by juanlabrador on 17/01/15.
 */
public class Evento implements Parcelable {
    
    private int id;
    private String nombre;
    private String descripcion;
    private String fecha;
    private Date hora;
    private Lugar lugar;
    private String estatus;

    public Evento() {
    }

    public Evento(int id, String nombre, String descripcion, String fecha, Date hora, Lugar lugar, String estatus) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.estatus = estatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeString(fecha);
        parcel.writeLong(hora.getTime());
        parcel.writeValue(lugar);
        parcel.writeString(estatus);

    }
}
