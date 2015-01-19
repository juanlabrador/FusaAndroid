package edu.ucla.fusa.android.modelo.evento;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by juanlabrador on 17/01/15.
 */
public class Evento {
    
    private int id;
    private String nombre;
    private byte[] logistica;
    private Date fecha;
    private Date hora;
    private Lugar lugar;
    private String estatus;

    public Evento() {
    }

    public Evento(int id, String nombre, byte[] logistica, Date fecha, Date hora, Lugar lugar, String estatus) {
        this.id = id;
        this.nombre = nombre;
        this.logistica = logistica;
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

    public byte[] getLogistica() {
        return logistica;
    }

    public void setLogistica(byte[] logistica) {
        this.logistica = logistica;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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
}
