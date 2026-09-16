package controlador;

import logica.ActividadService;
import modelo.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController() {
        actividadService = new ActividadService();
    }

    public List<Reserva> buscarActividades(LocalDate fecha, LocalTime hora) {
        return actividadService.buscarActividades(fecha, hora);
    }
}