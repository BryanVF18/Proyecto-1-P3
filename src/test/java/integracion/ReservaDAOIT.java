package integracion;

import modelo.Funcionario;
import modelo.Reserva;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistencia.ReservaDAO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaDAOIT {

    private static final File ARCHIVO = new File("reservas.xml");
    private static final File RESPALDO = new File("reservas-backup-test.xml");

    private final ReservaDAO reservaDAO = new ReservaDAO();

    @BeforeEach
    public void prepararPrueba() throws Exception {

        if (ARCHIVO.exists()) {
            Files.copy(
                    ARCHIVO.toPath(),
                    RESPALDO.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    @AfterEach
    public void restaurarArchivo() throws Exception {

        if (ARCHIVO.exists()) {
            Files.delete(ARCHIVO.toPath());
        }

        if (RESPALDO.exists()) {
            Files.move(
                    RESPALDO.toPath(),
                    ARCHIVO.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    @Test
    public void guardarYLeerReservaDesdeXML() {

        Funcionario funcionario = new Funcionario(
                "TEST-01",
                "1234",
                "Funcionario Prueba",
                "88888888"
        );

        Reserva reserva = new Reserva(
                "RES-TEST-001",
                "Actividad de prueba",
                funcionario,
                LocalDate.of(2026, 9, 20),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0)
        );

        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva);

        reservaDAO.guardarTodas(reservas);

        List<Reserva> reservasLeidas = reservaDAO.buscarTodas();

        assertEquals(1, reservasLeidas.size());

        Reserva reservaLeida = reservasLeidas.get(0);

        assertEquals(
                "RES-TEST-001",
                reservaLeida.getId()
        );

        assertEquals(
                "Actividad de prueba",
                reservaLeida.getActividad()
        );

        assertEquals(
                LocalDate.of(2026, 9, 20),
                reservaLeida.getFecha()
        );

        assertEquals(
                LocalTime.of(8, 0),
                reservaLeida.getHoraInicio()
        );

        assertEquals(
                LocalTime.of(10, 0),
                reservaLeida.getHoraFin()
        );

        assertNotNull(
                reservaLeida.getFuncionario()
        );

        assertEquals(
                "TEST-01",
                reservaLeida.getFuncionario().getId()
        );
    }
}