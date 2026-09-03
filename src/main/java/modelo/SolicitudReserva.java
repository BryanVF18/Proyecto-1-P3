package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*Esta clase contiene la información introducida en el formulario antes de crear
definitivamente una reserva.Se guardan los identificadores de las categorías porque
el funcionario selecciona categorías y el sistema escoge automáticamente los
recursos disponibles.*/

public class SolicitudReserva {

    private String actividad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private List<String> idsCategorias = new ArrayList<>();

    public SolicitudReserva() {
    }

    public SolicitudReserva(
            String actividad,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            List<String> idsCategorias
    ) {
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        setIdsCategorias(idsCategorias);
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public List<String> getIdsCategorias() {
        return new ArrayList<>(idsCategorias);
    }

    public void setIdsCategorias(List<String> idsCategorias) {
        if (idsCategorias == null) {
            this.idsCategorias = new ArrayList<>();
        } else {
            this.idsCategorias = new ArrayList<>(idsCategorias);
        }
    }
}
