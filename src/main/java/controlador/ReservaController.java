package controlador;

import logica.GeminiException;
import logica.ReservaException;
import logica.ReservaService;
import logica.ServicioGemini;
import modelo.Categoria;
import modelo.DatosReservaIA;
import modelo.Funcionario;
import modelo.Reserva;
import modelo.SolicitudReserva;

import java.util.List;

public class ReservaController {

    private final ReservaService reservaService = new ReservaService();
    private final ServicioGemini servicioGemini = new ServicioGemini();

    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    public List<Reserva> listarPorFuncionario(String idFuncionario) {
        return reservaService.listarPorFuncionario(idFuncionario);
    }

    public List<Reserva> listarActualesPorFuncionario(String idFuncionario) {
        return reservaService.listarActualesPorFuncionario(idFuncionario);
    }

    public List<Reserva> listarHistorialPorFuncionario(String idFuncionario) {
        return reservaService.listarHistorialPorFuncionario(idFuncionario);
    }

    public Reserva crearReserva(
            SolicitudReserva solicitud,
            Funcionario funcionario
    ) throws ReservaException {

        return reservaService.crearReserva(solicitud, funcionario);
    }

    public void cancelarReserva(String idReserva, Funcionario funcionario)
            throws ReservaException {
        reservaService.cancelarReserva(idReserva, funcionario);
    }

    public DatosReservaIA interpretarSolicitudConGemini(
            String solicitud,
            List<Categoria> categorias,
            String claveApi
    ) throws GeminiException {
        return servicioGemini.interpretarSolicitud(
                solicitud,
                categorias,
                claveApi
        );
    }
}
