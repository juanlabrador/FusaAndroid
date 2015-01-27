package edu.ucla.fusa.android.modelo.academico;

import java.util.Calendar;
import java.util.concurrent.CancellationException;

/**
 * Created by juanlabrador on 25/11/14.
 */
public class Horario {

    private int horario_id;
    private Dia dia;
    private String horaInicio;
    private String horaFin;

    public Horario() {
    }

    public int getHorario_id() {
        return horario_id;
    }

    public void setHorario_id(int horario_id) {
        this.horario_id = horario_id;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}
