package edu.ucla.fusa.android.modelo.academico;

import java.util.Date;

import edu.ucla.fusa.android.modelo.fundacion.Parroquia;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Estudiante extends Persona {

    private int id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String correo;
    private String direccion;
    private int edad;
    private Date fechanac;
    private Parroquia parroquia;
    private String sexo;
    private String telefonoFijo;
    private String telefonoMovil;
    private String nombreApellido;
    private byte[] imagen;
    private String nombreImagen;
    private String becado;
    private String ensamble;
    private String fundacion;
    private String inscritoConservatorio;
    private String inscritoCoro;
    private String instrumentoPropio;
    private String[] postulacion;
    private String[] solicitudPrestamo;
    private Representante representante;
    private Usuario usuario;
    private String estatus;

    public Estudiante() {
    }

    public Estudiante(int id, String nombre, String apellido, String cedula, String correo, String direccion, int edad, Date fechanac, Parroquia parroquia, String sexo, String telefonoFijo, String telefonoMovil, String nombreApellido, byte[] imagen, String nombreImagen, String becado, String ensamble, String fundacion, String inscritoConservatorio, String inscritoCoro, String instrumentoPropio, String[] postulacion, String[] solicitudPrestamo, Representante representante, Usuario usuario, String estatus) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.correo = correo;
        this.direccion = direccion;
        this.edad = edad;
        this.fechanac = fechanac;
        this.parroquia = parroquia;
        this.sexo = sexo;
        this.telefonoFijo = telefonoFijo;
        this.telefonoMovil = telefonoMovil;
        this.nombreApellido = nombreApellido;
        this.imagen = imagen;
        this.nombreImagen = nombreImagen;
        this.becado = becado;
        this.ensamble = ensamble;
        this.fundacion = fundacion;
        this.inscritoConservatorio = inscritoConservatorio;
        this.inscritoCoro = inscritoCoro;
        this.instrumentoPropio = instrumentoPropio;
        this.postulacion = postulacion;
        this.solicitudPrestamo = solicitudPrestamo;
        this.representante = representante;
        this.usuario = usuario;
        this.estatus = estatus;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
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

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getBecado() {
        return becado;
    }

    public void setBecado(String becado) {
        this.becado = becado;
    }

    public String getEnsamble() {
        return ensamble;
    }

    public void setEnsamble(String ensamble) {
        this.ensamble = ensamble;
    }

    public String getFundacion() {
        return fundacion;
    }

    public void setFundacion(String fundacion) {
        this.fundacion = fundacion;
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

    public String[] getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(String[] postulacion) {
        this.postulacion = postulacion;
    }

    public Representante getRepresentante() {
        return representante;
    }

    public void setRepresentante(Representante representante) {
        this.representante = representante;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String[] getSolicitudPrestamo() {
        return solicitudPrestamo;
    }

    public void setSolicitudPrestamo(String[] solicitudPrestamo) {
        this.solicitudPrestamo = solicitudPrestamo;
    }
}
