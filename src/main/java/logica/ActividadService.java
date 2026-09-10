package logica;

import modelo.Reserva;
import persistencia.ReservaDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ActividadService {

    private final ReservaDAO reservaDAO;

    public ActividadService() {
        reservaDAO = new ReservaDAO();
    }

    public Reserva buscarActividad(LocalDate fecha, LocalTime hora) {
        List<Reserva> reservas = reservaDAO.buscarTodas();

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            if (!reserva.estaActiva()) {
                continue;
            }

            if (!fecha.equals(reserva.getFecha())) {
                continue;
            }

            if (horaEstaDentroDeReserva(hora, reserva)) {
                return reserva;
            }
        }

        return null;
    }

    private boolean horaEstaDentroDeReserva(LocalTime hora, Reserva reserva) {
        return !hora.isBefore(reserva.getHoraInicio())
                && hora.isBefore(reserva.getHoraFin());
    }
}