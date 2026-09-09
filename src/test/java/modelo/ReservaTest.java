package modelo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaTest {

    private Reserva crearReserva() {

        Funcionario funcionario = new Funcionario(
                "F01",
                "F01",
                "Juan",
                "88888888"
        );

        return new Reserva(
                "RES-000001",
                "Reunion",
                funcionario,
                LocalDate.of(2026, 9, 15),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0)
        );
    }

    @Test
    public void reservaPerteneceAlFuncionarioCorrecto() {

        Reserva reserva = crearReserva();

        assertTrue(
                reserva.perteneceAlFuncionario("F01")
        );
    }

    @Test
    public void reservaNoPerteneceAOtroFuncionario() {

        Reserva reserva = crearReserva();

        assertFalse(
                reserva.perteneceAlFuncionario("F02")
        );
    }

    @Test
    public void reservaSeSuperponeConHorarioOcupado() {

        Reserva reserva = crearReserva();

        boolean resultado = reserva.seSuperponeCon(
                LocalDate.of(2026, 9, 15),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0)
        );

        assertTrue(resultado);
    }

    @Test
    public void reservaNoSeSuperponeConHorarioPosterior() {

        Reserva reserva = crearReserva();

        boolean resultado = reserva.seSuperponeCon(
                LocalDate.of(2026, 9, 15),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
        );

        assertFalse(resultado);
    }

    @Test
    public void reservaNoSeSuperponeEnOtraFecha() {

        Reserva reserva = crearReserva();

        boolean resultado = reserva.seSuperponeCon(
                LocalDate.of(2026, 9, 16),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0)
        );

        assertFalse(resultado);
    }

    @Test
    public void reservaInicialmenteEstaActiva() {

        Reserva reserva = crearReserva();

        assertTrue(
                reserva.estaActiva()
        );
    }

    @Test
    public void reservaCanceladaNoEstaActiva() {

        Reserva reserva = crearReserva();

        reserva.setEstado("CANCELADA");

        assertFalse(
                reserva.estaActiva()
        );
    }

    @Test
    public void reservaEsFutura() {

        Reserva reserva = crearReserva();

        assertTrue(
                reserva.esFutura(
                        LocalDate.of(2026, 9, 14)
                )
        );
    }

    @Test
    public void reservaNoEsFuturaElMismoDia() {

        Reserva reserva = crearReserva();

        assertFalse(
                reserva.esFutura(
                        LocalDate.of(2026, 9, 15)
                )
        );
    }
}