package logica;

import modelo.SolicitudReserva;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidadorSolicitudReservaTest {

    private final ValidadorSolicitudReserva validador =
            new ValidadorSolicitudReserva();

    @Test
    public void solicitudCorrectaNoGeneraError() {

        SolicitudReserva solicitud = new SolicitudReserva(
                "Reunion de proyecto",
                LocalDate.of(2026, 9, 15),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                List.of("CAT-01")
        );

        assertDoesNotThrow(() ->
                validador.validar(
                        solicitud,
                        LocalDate.of(2026, 9, 9)
                )
        );
    }

    @Test
    public void solicitudNulaGeneraError() {

        ReservaException excepcion = assertThrows(
                ReservaException.class,
                () -> validador.validar(
                        null,
                        LocalDate.of(2026, 9, 9)
                )
        );

        assertEquals(
                "La solicitud de reserva no puede estar vacia",
                excepcion.getMessage()
        );
    }

    @Test
    public void actividadVaciaGeneraError() {

        SolicitudReserva solicitud = new SolicitudReserva(
                "",
                LocalDate.of(2026, 9, 15),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                List.of("CAT-01")
        );

        assertThrows(
                ReservaException.class,
                () -> validador.validar(
                        solicitud,
                        LocalDate.of(2026, 9, 9)
                )
        );
    }

    @Test
    public void fechaPasadaGeneraError() {

        SolicitudReserva solicitud = new SolicitudReserva(
                "Reunion",
                LocalDate.of(2026, 9, 8),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                List.of("CAT-01")
        );

        ReservaException excepcion = assertThrows(
                ReservaException.class,
                () -> validador.validar(
                        solicitud,
                        LocalDate.of(2026, 9, 9)
                )
        );

        assertEquals(
                "No se puede reservar en una fecha pasada",
                excepcion.getMessage()
        );
    }

    @Test
    public void horaInicioMayorAHoraFinGeneraError() {

        SolicitudReserva solicitud = new SolicitudReserva(
                "Reunion",
                LocalDate.of(2026, 9, 15),
                LocalTime.of(11, 0),
                LocalTime.of(9, 0),
                List.of("CAT-01")
        );

        assertThrows(
                ReservaException.class,
                () -> validador.validar(
                        solicitud,
                        LocalDate.of(2026, 9, 9)
                )
        );
    }

    @Test
    public void sinCategoriasGeneraError() {

        SolicitudReserva solicitud = new SolicitudReserva(
                "Reunion",
                LocalDate.of(2026, 9, 15),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                List.of()
        );

        ReservaException excepcion = assertThrows(
                ReservaException.class,
                () -> validador.validar(
                        solicitud,
                        LocalDate.of(2026, 9, 9)
                )
        );

        assertEquals(
                "Debe seleccionar al menos una categoria de recurso",
                excepcion.getMessage()
        );
    }

    @Test
    public void categoriasRepetidasGeneranError() {

        SolicitudReserva solicitud = new SolicitudReserva(
                "Reunion",
                LocalDate.of(2026, 9, 15),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                List.of("CAT-01", "CAT-01")
        );

        assertThrows(
                ReservaException.class,
                () -> validador.validar(
                        solicitud,
                        LocalDate.of(2026, 9, 9)
                )
        );
    }
}