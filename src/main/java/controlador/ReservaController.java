package controlador;

import logica.ReservaException;
import logica.ReservaService;
import modelo.Funcionario;
import modelo.Reserva;
import modelo.SolicitudReserva;

import java.util.List;

public class ReservaController {

    private final ReservaService reservaService = new ReservaService();

    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    public List<Reserva> listarPorFuncionario(String idFuncionario) {
        return reservaService.listarPorFuncionario(idFuncionario);
    }

    public Reserva crearReserva(
            SolicitudReserva solicitud,
            Funcionario funcionario
    ) throws ReservaException {

        return reservaService.crearReserva(solicitud, funcionario);
    }
}