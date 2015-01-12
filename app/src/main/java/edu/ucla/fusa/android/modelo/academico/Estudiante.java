package edu.ucla.fusa.android.modelo.academico;

import java.util.Date;

import edu.ucla.fusa.android.modelo.fundacion.Parroquia;
import edu.ucla.fusa.android.modelo.seguridad.Usuario;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Estudiante extends Persona {

    private int id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String correo;
    private int edad;
    private Date fechanac;
    private String sexo;
    private String telefonoFijo;
    private String telefonoMovil;
    private String becado;
    private String inscritoConservatorio;
    private String inscritoCoro;
    private String instrumentoPropio;
    private String estatus;
    private byte[] foto;

    public Estudiante() {
    }

    public Estudiante(int id, String nombre, String apellido, String cedula, String correo, int edad, Date fechanac, String sexo, String telefonoFijo, String telefonoMovil, String becado, String inscritoConservatorio, String inscritoCoro, String instrumentoPropio, String estatus, byte[] foto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.correo = correo;
        this.edad = edad;
        this.fechanac = fechanac;
        this.sexo = sexo;
        this.telefonoFijo = telefonoFijo;
        this.telefonoMovil = telefonoMovil;
        this.becado = becado;
        this.inscritoConservatorio = inscritoConservatorio;
        this.inscritoCoro = inscritoCoro;
        this.instrumentoPropio = instrumentoPropio;
        this.estatus = estatus;
        this.foto = foto;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getApellido() {
        return apellido;
    }

    @Override
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String getCedula() {
        return cedula;
    }

    @Override
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @Override
    public String getCorreo() {
        return correo;
    }

    @Override
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Date getFechanac() {
        return fechanac;
    }

    public void setFechanac(Date fechanac) {
        this.fechanac = fechanac;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
