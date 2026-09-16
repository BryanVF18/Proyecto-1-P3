package controlador;

import logica.EstadisticaService;

import java.time.LocalDate;
import java.util.List;

public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController() {
        estadisticaService = new EstadisticaService();
    }

    public List<String> obtenerCategoriasReservadas(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        return estadisticaService.obtenerCategoriasReservadas(
                fechaInicio,
                fechaFin
        );
    }

    public int contarRecursosPorCategoria(
            String idCategoria,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        return estadisticaService.contarRecursosPorCategoria(
                idCategoria,
                fechaInicio,
                fechaFin
        );
    }

    public List<String> obtenerSemanas(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        return estadisticaService.obtenerSemanas(
                fechaInicio,
                fechaFin
        );
    }

    public int contarActividadesPorSemana(
            String semana,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        return estadisticaService.contarActividadesPorSemana(
                semana,
                fechaInicio,
                fechaFin
        );
    }
    public String obtenerDescripcionCategoria(String idCategoria) {
        return estadisticaService.obtenerDescripcionCategoria(idCategoria);
    }
}