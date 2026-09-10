package controlador;

import logica.ActividadService;
import modelo.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;

public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController() {
        actividadService = new ActividadService();
    }

    public Reserva buscarActividad(LocalDate fecha, LocalTime hora) {
        return actividadService.buscarActividad(fecha, hora);
    }
}