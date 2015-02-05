package edu.ucla.fusa.android.modelo.herramientas;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemListNoticia implements Parcelable {

    public long id;
    public String titulo;
    public String fecha;
    public byte[] imagen;
    public String descripcion;
    public int haveFoto;

    public ItemListNoticia() {}

    public ItemListNoticia(long id, String titulo, String fecha, byte[] imagen, String descripcion, int haveFoto) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.haveFoto = haveFoto;
    }

    public int getHaveFoto() {
        return haveFoto;
    }

    public void setHaveFoto(int haveFoto) {
        this.haveFoto = haveFoto;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public long getId() {
        return this.id;
    }

    public byte[] getImagen() {
        return this.imagen;
    }

    public String getTitulo() {
        return this.titulo;
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

    public void setId(long paramInt) {
        this.id = paramInt;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(titulo);
        parcel.writeString(fecha);
        parcel.writeByteArray(imagen);
        parcel.writeString(descripcion);
        parcel.writeInt(haveFoto);
    }
}