package edu.ucla.fusa.android.modelo.herramientas;

import java.util.Date;

public class ItemListEstudiantesComodato {

    public int foto;
    public String nombre;
    public int Edad;
    public String instrumento;
    public int puntuacion;

    public ItemListEstudiantesComodato(int foto, String nombre, int edad, String instrumento, int puntuacion) {
        this.foto = foto;
        this.nombre = nombre;
        Edad = edad;
        this.instrumento = instrumento;
        this.puntuacion = puntuacion;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int edad) {
        Edad = edad;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}