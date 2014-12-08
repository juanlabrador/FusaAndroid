package edu.ucla.fusa.android.modelo.herramientas;

public class ItemListAplications {

    private int icon;
    private String app;
    private boolean connected;

    public ItemListAplications(String app, int icon, boolean connected) {
        this.app = app;
        this.icon = icon;
        this.connected = connected;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}