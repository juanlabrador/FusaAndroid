package edu.ucla.fusa.android.modelo.herramientas;

public class ItemListInstrumentos {

    public boolean seleccion;
    public String instrumento;

    public ItemListInstrumentos(String instrumento, boolean seleccion) {
        this.instrumento = instrumento;
        this.seleccion = seleccion;
    }

    public boolean getSeleccion() {
        return this.seleccion;
    }

    public String getInstrumento() {
        return this.instrumento;
    }

    public void setSeleccion(boolean seleccion) {
        this.seleccion = seleccion;
    }

}