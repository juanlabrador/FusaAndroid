package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 17/11/14.
 */
public class Usuario {

    private transient int id_u;
    private String nombre;
    private String clave;
    private byte[] foto;

    public Usuario() {
        super();
    }

    public Usuario(int id, String nombre, String clave, byte[] foto) {
        super();
        this.id_u = id;
        this.nombre = nombre;
        this.clave = clave;
        this.foto = foto;
    }

    public int getIdU() {
        return id_u;
    }

    public void setIdU(int id) {
        this.id_u = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}