package logica;

import modelo.Categoria;
import modelo.Recurso;
import modelo.Reserva;
import persistencia.CategoriaDAO;
import persistencia.RecursoDAO;
import persistencia.ReservaDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarizacionService {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final RecursoDAO recursoDAO = new RecursoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public List<Categoria> listarCategorias() {
        return categoriaDAO.buscarTodas();
    }

    public List<Recurso> listarRecursosPorCategoria(String idCategoria) {

        List<Recurso> resultado = new ArrayList<>();
        List<Recurso> recursos = recursoDAO.buscarTodas();

        for (int i = 0; i < recursos.size(); i++) {

            Recurso recurso = recursos.get(i);

            if (idCategoria.equals(recurso.getCategoria())) {
                resultado.add(recurso);
            }
        }

        return resultado;
    }

    public Reserva buscarReserva(Recurso recurso, LocalDate fecha, LocalTime hora) {

        List<Reserva> reservas = reservaDAO.buscarTodas();

        for (int i = 0; i < reservas.size(); i++) {

            Reserva reserva = reservas.get(i);

            if (!reserva.estaActiva()) {
                continue;
            }

            if (!fecha.equals(reserva.getFecha())) {
                continue;
            }

            if (!horaEstaDentroDeReserva(hora, reserva)) {
                continue;
            }

            if (reservaUsaRecurso(reserva, recurso.getId())) {
                return reserva;
            }
        }

        return null;
    }

    private boolean horaEstaDentroDeReserva(LocalTime hora, Reserva reserva) {

        return !hora.isBefore(reserva.getHoraInicio())
                && hora.isBefore(reserva.getHoraFin());
    }

    private boolean reservaUsaRecurso(Reserva reserva, String idRecurso) {

        List<Recurso> recursos = reserva.getRecursosAsignados();

        if (recursos == null) {
            return false;
        }

        for (int i = 0; i < recursos.size(); i++) {

            Recurso recurso = recursos.get(i);

            if (recurso != null
                    && idRecurso.equals(recurso.getId())) {

                return true;
            }
        }

        return false;
    }
}