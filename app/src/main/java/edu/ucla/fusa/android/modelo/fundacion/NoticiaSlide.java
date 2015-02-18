package edu.ucla.fusa.android.modelo.fundacion;

import edu.ucla.fusa.android.modelo.evento.Evento;

/**
 * Created by juanlabrador on 05/12/14.
 */
public class NoticiaSlide {

    private long id;
    private Noticia noticia;


    public NoticiaSlide() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Noticia getNoticia() {
        return noticia;
    }

    public void setNoticia(Noticia noticia) {
        this.noticia = noticia;
    }
}