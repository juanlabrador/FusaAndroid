package edu.ucla.fusa.android.modelo.academico;

import java.util.Calendar;
import java.util.concurrent.CancellationException;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class Horario {

    private int id;
    private Dia dia;
    private Calendar horaInicio;
    private Calendar horaFin;

    public Horario(int id, Dia dia, Calendar horaInicio, Calendar horaFin) {
        this.id = id;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public Calendar getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Calendar horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Calendar getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Calendar horaFin) {
        this.horaFin = horaFin;
    }
}
