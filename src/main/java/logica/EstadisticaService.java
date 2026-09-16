package logica;

import modelo.Recurso;
import modelo.Reserva;
import persistencia.ReservaDAO;
import modelo.Categoria;
import persistencia.CategoriaDAO;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EstadisticaService {

    private final ReservaDAO reservaDAO;
    private final CategoriaDAO categoriaDAO;

/*las estadisticas no se guardan en un archivo XML porque se calculan directamente a partir de las reservas existentes.
De esta forma se evita guardar informacion repetida y las estadisticas siempre reflejan los datos actuales*/
public EstadisticaService() {
    reservaDAO = new ReservaDAO();
    categoriaDAO = new CategoriaDAO();
}

    public List<String> obtenerCategoriasReservadas(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        List<Reserva> reservas = reservaDAO.buscarTodas();
        List<String> categorias = new ArrayList<>();

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            if (reserva.estaActiva()
                    && fechaEstaEnRango(reserva.getFecha(), fechaInicio, fechaFin)) {

                List<Recurso> recursos = reserva.getRecursosAsignados();

                if (recursos != null) {
                    for (int j = 0; j < recursos.size(); j++) {
                        Recurso recurso = recursos.get(j);
                        String categoria = recurso.getCategoria();

                        if (!categorias.contains(categoria)) {
                            categorias.add(categoria);
                        }
                    }
                }
            }
        }

        return categorias;
    }

    public int contarRecursosPorCategoria(
            String idCategoria,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        List<Reserva> reservas = reservaDAO.buscarTodas();
        int cantidad = 0;

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            if (reserva.estaActiva()
                    && fechaEstaEnRango(reserva.getFecha(), fechaInicio, fechaFin)) {

                List<Recurso> recursos = reserva.getRecursosAsignados();

                if (recursos != null) {
                    for (int j = 0; j < recursos.size(); j++) {
                        Recurso recurso = recursos.get(j);

                        if (idCategoria.equals(recurso.getCategoria())) {
                            cantidad++;
                        }
                    }
                }
            }
        }

        return cantidad;
    }

    public List<String> obtenerSemanas(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        List<String> semanas = new ArrayList<>();

        LocalDate fechaActual = fechaInicio;

        while (!fechaActual.isAfter(fechaFin)) {
            String semana = obtenerNombreSemana(fechaActual);

            if (!semanas.contains(semana)) {
                semanas.add(semana);
            }

            fechaActual = fechaActual.plusDays(1);
        }

        return semanas;
    }

    public int contarActividadesPorSemana(
            String semana,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        List<Reserva> reservas = reservaDAO.buscarTodas();
        int cantidad = 0;

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            if (reserva.estaActiva()
                    && fechaEstaEnRango(reserva.getFecha(), fechaInicio, fechaFin)) {

                String semanaReserva = obtenerNombreSemana(
                        reserva.getFecha()
                );

                if (semana.equals(semanaReserva)) {
                    cantidad++;
                }
            }
        }

        return cantidad;
    }

    private boolean fechaEstaEnRango(
            LocalDate fecha,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        return !fecha.isBefore(fechaInicio)
                && !fecha.isAfter(fechaFin);
    }

    private String obtenerNombreSemana(LocalDate fecha) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int numeroSemana = fecha.get(
                weekFields.weekOfWeekBasedYear()
        );

        int anio = fecha.get(
                weekFields.weekBasedYear()
        );

        return "Semana " + numeroSemana + " - " + anio;
    }

    public String obtenerDescripcionCategoria(String idCategoria) {
        List<Categoria> categorias = categoriaDAO.buscarTodas();

        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);

            if (categoria.getId().equals(idCategoria)) {
                return categoria.getDescripcion();
            }
        }

        return idCategoria;
    }
}