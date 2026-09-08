package logica;

import modelo.Categoria;
import modelo.Funcionario;
import modelo.Recurso;
import modelo.Reserva;
import modelo.SolicitudReserva;
import persistencia.CategoriaDAO;
import persistencia.RecursoDAO;
import persistencia.ReservaDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final RecursoDAO recursoDAO = new RecursoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ValidadorSolicitudReserva validador = new ValidadorSolicitudReserva();

    public List<Reserva> listarTodas() {
        return reservaDAO.buscarTodas();
    }

    public List<Reserva> listarPorFuncionario(String idFuncionario) {
        List<Reserva> resultado = new ArrayList<>();
        List<Reserva> reservas = reservaDAO.buscarTodas();

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            if (reserva.perteneceAlFuncionario(idFuncionario)) {
                resultado.add(reserva);
            }
        }

        return resultado;
    }

    public Reserva crearReserva(
            SolicitudReserva solicitud,
            Funcionario funcionario
    ) throws ReservaException {

        validador.validar(solicitud, LocalDate.now());
        validarFuncionario(funcionario);

        List<Reserva> reservasActuales = reservaDAO.buscarTodas();
        List<Recurso> recursos = recursoDAO.buscarTodas();
        List<Categoria> categorias = categoriaDAO.buscarTodas();

        List<Recurso> recursosAsignados = new ArrayList<>();
        List<String> categoriasNoDisponibles = new ArrayList<>();

        List<String> idsCategorias = solicitud.getIdsCategorias();

        for (int i = 0; i < idsCategorias.size(); i++) {
            String idCategoria = idsCategorias.get(i);

            validarCategoriaExistente(idCategoria, categorias);

            Recurso disponible = buscarPrimerRecursoDisponible(
                    idCategoria,
                    solicitud,
                    recursos,
                    reservasActuales
            );

            if (disponible == null) {
                categoriasNoDisponibles.add(
                        obtenerDescripcionCategoria(idCategoria, categorias)
                );
            } else {
                recursosAsignados.add(disponible);
            }
        }

        if (!categoriasNoDisponibles.isEmpty()) {
            throw new ReservaException(
                    crearMensajeSinDisponibilidad(categoriasNoDisponibles)
            );
        }

        String nuevoId = generarSiguienteId(reservasActuales);

        Reserva nuevaReserva = new Reserva(
                nuevoId,
                solicitud.getActividad().trim(),
                funcionario,
                solicitud.getFecha(),
                solicitud.getHoraInicio(),
                solicitud.getHoraFin()
        );

        nuevaReserva.setRecursosAsignados(recursosAsignados);
        reservasActuales.add(nuevaReserva);
        reservaDAO.guardarTodas(reservasActuales);

        return nuevaReserva;
    }

    private void validarFuncionario(Funcionario funcionario) throws ReservaException {
        if (funcionario == null
                || funcionario.getId() == null
                || funcionario.getId().trim().isEmpty()) {

            throw new ReservaException(
                    "Debe existir un funcionario para realizar la reserva"
            );
        }
    }

    private void validarCategoriaExistente(
            String idCategoria,
            List<Categoria> categorias
    ) throws ReservaException {

        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);

            if (categoria.getId().equals(idCategoria)) {
                return;
            }
        }

        throw new ReservaException(
                "No existe la categoria seleccionada: " + idCategoria
        );
    }

    private Recurso buscarPrimerRecursoDisponible(
            String idCategoria,
            SolicitudReserva solicitud,
            List<Recurso> recursos,
            List<Reserva> reservas
    ) {

        for (int i = 0; i < recursos.size(); i++) {
            Recurso recurso = recursos.get(i);

            if (idCategoria.equals(recurso.getCategoria())
                    && recursoEstaDisponible(recurso, solicitud, reservas)) {

                return recurso;
            }
        }

        return null;
    }

    private boolean recursoEstaDisponible(
            Recurso recurso,
            SolicitudReserva solicitud,
            List<Reserva> reservas
    ) {

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);

            if (reserva.seSuperponeCon(
                    solicitud.getFecha(),
                    solicitud.getHoraInicio(),
                    solicitud.getHoraFin()
            ) && reservaUsaRecurso(reserva, recurso.getId())) {

                return false;
            }
        }

        return true;
    }

    private boolean reservaUsaRecurso(Reserva reserva, String idRecurso) {
        List<Recurso> recursosAsignados = reserva.getRecursosAsignados();

        if (recursosAsignados == null) {
            return false;
        }

        for (int i = 0; i < recursosAsignados.size(); i++) {
            Recurso recursoAsignado = recursosAsignados.get(i);

            if (recursoAsignado != null
                    && idRecurso.equals(recursoAsignado.getId())) {

                return true;
            }
        }

        return false;
    }

    private String obtenerDescripcionCategoria(
            String idCategoria,
            List<Categoria> categorias
    ) {

        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);

            if (categoria.getId().equals(idCategoria)) {
                return categoria.getDescripcion();
            }
        }

        return idCategoria;
    }

    private String crearMensajeSinDisponibilidad(
            List<String> categoriasNoDisponibles
    ) {

        String mensaje = "No hay recursos disponibles para las siguientes categorias: ";

        for (int i = 0; i < categoriasNoDisponibles.size(); i++) {
            mensaje += categoriasNoDisponibles.get(i);

            if (i < categoriasNoDisponibles.size() - 1) {
                mensaje += ", ";
            }
        }

        return mensaje;
    }

    private String generarSiguienteId(List<Reserva> reservas) {
        int maximoActual = 0;

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            String id = reserva.getId();

            if (id != null && id.startsWith("RES-") && id.length() > 4) {
                try {
                    int numero = Integer.parseInt(id.substring(4));

                    if (numero > maximoActual) {
                        maximoActual = numero;
                    }
                } catch (NumberFormatException e) {
                    // Si existe un id con otro formato, se ignora.
                }
            }
        }

        return String.format("RES-%06d", maximoActual + 1);
    }
}