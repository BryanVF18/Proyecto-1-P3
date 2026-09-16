package logica;

import modelo.Reserva;
import persistencia.ReservaDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ActividadService {

    private final ReservaDAO reservaDAO;

    public ActividadService() {
        reservaDAO = new ReservaDAO();
    }

    public List<Reserva> buscarActividades(LocalDate fecha, LocalTime hora) {
        List<Reserva> reservas = reservaDAO.buscarTodas();
        List<Reserva> actividadesEncontradas = new ArrayList<>();

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            if (reserva.estaActiva()
                    && fecha.equals(reserva.getFecha())
                    && horaEstaDentroDeReserva(hora, reserva)) {

                actividadesEncontradas.add(reserva);
            }
        }

        return actividadesEncontradas;
    }

    private boolean horaEstaDentroDeReserva(LocalTime hora, Reserva reserva) {
        return !hora.isBefore(reserva.getHoraInicio())
                && hora.isBefore(reserva.getHoraFin());
    }
}