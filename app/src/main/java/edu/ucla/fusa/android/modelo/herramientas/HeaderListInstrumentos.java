package edu.ucla.fusa.android.modelo.herramientas;

import edu.ucla.fusa.android.interfaces.Item;

public class HeaderListInstrumentos implements Item {

    private String titulo;

    public HeaderListInstrumentos(String paramString) {
        this.titulo = paramString;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public boolean isSection() {
        return true;
    }

    public void setTitulo(String paramString) {
        this.titulo = paramString;
    }
}