package edu.ucla.fusa.android.modelo.academico;

/**
 * Created by juanlabrador on 05/02/15.
 */
public class EvaluacionPorClase {

    private String escala;
    private String estudio;
    private String fecha;
    private int id;
    private String observaciones;
    private String pieza;
    private String tareaAsignada;
    private ObjetivoPorClase objetivoPorClase;
    private EstadoCumplimiento estadoCumplimiento;
    private EstudiantePorClase estudiantePorClase;

    public EvaluacionPorClase() {
    }

    public String getEscala() {
        return escala;
    }

    public void setEscala(String escala) {
        this.escala = escala;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPieza() {
        return pieza;
    }

    public void setPieza(String pieza) {
        this.pieza = pieza;
    }

    public String getTareaAsignada() {
        return tareaAsignada;
    }

    public void setTareaAsignada(String tareaAsignada) {
        this.tareaAsignada = tareaAsignada;
    }

    public ObjetivoPorClase getObjetivoPorClase() {
        return objetivoPorClase;
    }

    public void setObjetivoPorClase(ObjetivoPorClase objetivoPorClase) {
        this.objetivoPorClase = objetivoPorClase;
    }

    public EstadoCumplimiento getEstadoCumplimiento() {
        return estadoCumplimiento;
    }

    public void setEstadoCumplimiento(EstadoCumplimiento estadoCumplimiento) {
        this.estadoCumplimiento = estadoCumplimiento;
    }

    public EstudiantePorClase getEstudiantePorClase() {
        return estudiantePorClase;
    }

    public void setEstudiantePorClase(EstudiantePorClase estudiantePorClase) {
        this.estudiantePorClase = estudiantePorClase;
    }
}
