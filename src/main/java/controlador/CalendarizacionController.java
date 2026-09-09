package controlador;

import logica.CalendarizacionService;
import modelo.Categoria;
import modelo.Recurso;
import modelo.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarizacionController {

    private final CalendarizacionService calendarizacionService;

    public CalendarizacionController() {
        calendarizacionService = new CalendarizacionService();
    }

    public List<Categoria> listarCategorias() {
        return calendarizacionService.listarCategorias();
    }

    public List<Recurso> listarRecursosPorCategoria(String idCategoria) {
        return calendarizacionService.listarRecursosPorCategoria(idCategoria);
    }

    public Reserva buscarReserva(Recurso recurso, LocalDate fecha, LocalTime hora) {

        return calendarizacionService.buscarReserva(recurso, fecha, hora);
    }
}