package edu.ucla.fusa.android.modelo.academico;

import java.util.Calendar;

import edu.ucla.fusa.android.modelo.fundacion.Parroquia;

/**
 * Created by juanlabrador on 10/01/15.
 */
public class Representante {

    private transient int id;
    private String cedula;
    private String nombre;
    private String apellido;
    private int edad;
    private String sexo;
    private Calendar fechanac;
    private String nombreApellido;
    private String ocupacion;
    private String parentesco;
    private String direccion;
    private Parroquia parroquia;
    private String telefonoFijo;
    private String telefonoMovil;
    private String correo;
    private String nombreImagen;
    private byte[] imagen;
    private String estatus;
    private Usuario usuario;

    public Representante() {
    }

    public Representante(int id, String cedula, String nombre, String apellido, int edad, String sexo, Calendar fechanac, String nombreApellido, String ocupacion, String parentesco, String direccion, Parroquia parroquia, String telefonoFijo, String telefonoMovil, String correo, String nombreImagen, byte[] imagen, String estatus, Usuario usuario) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.sexo = sexo;
        this.fechanac = fechanac;
        this.nombreApellido = nombreApellido;
        this.ocupacion = ocupacion;
        this.parentesco = parentesco;
        this.direccion = direccion;
        this.parroquia = parroquia;
        this.telefonoFijo = telefonoFijo;
        this.telefonoMovil = telefonoMovil;
        this.correo = correo;
        this.nombreImagen = nombreImagen;
        this.imagen = imagen;
        this.estatus = estatus;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Calendar getFechanac() {
        return fechanac;
    }

    public void setFechanac(Calendar fechanac) {
        this.fechanac = fechanac;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
