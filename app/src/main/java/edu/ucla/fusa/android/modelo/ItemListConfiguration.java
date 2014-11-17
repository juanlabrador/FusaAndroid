package edu.ucla.fusa.android.modelo;

import edu.ucla.fusa.android.interfaces.Item;

public class ItemListConfiguration implements Item {

    public int icono;
    public final String title;

    public ItemListConfiguration(String paramString, int paramInt) {
        this.title = paramString;
        this.icono = paramInt;
    }

    public int getIcono() {
        return this.icono;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isSection() {
        return false;
    }

    public void setIcono(int paramInt) {
        this.icono = paramInt;
    }
}