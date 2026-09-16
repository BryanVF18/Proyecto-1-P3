package logica;

import modelo.Categoria;
import modelo.DatosReservaIA;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioGeminiTest {

    @Test
    public void respuestaCorrectaSeConvierteEnDatosDeReserva()
            throws GeminiException {
        ServicioGemini servicio = new ServicioGemini();
        List<Categoria> categorias = List.of(
                new Categoria("CAT-01", "Salas"),
                new Categoria("CAT-02", "Audiovisuales")
        );

        DatosReservaIA datos = servicio.interpretarRespuestaIA(
                "ACTIVIDAD=Capacitación de usuarios\n"
                        + "FECHA=2026-09-20\n"
                        + "HORA_INICIO=08:00\n"
                        + "HORA_FIN=10:00\n"
                        + "CATEGORIAS=CAT-01,CAT-02",
                categorias
        );

        assertEquals("Capacitación de usuarios", datos.getActividad());
        assertEquals(LocalDate.of(2026, 9, 20), datos.getFecha());
        assertEquals(LocalTime.of(8, 0), datos.getHoraInicio());
        assertEquals(LocalTime.of(10, 0), datos.getHoraFin());
        assertEquals(List.of("CAT-01", "CAT-02"), datos.getIdsCategorias());
    }

    @Test
    public void categoriaNoDisponibleGeneraError() {
        ServicioGemini servicio = new ServicioGemini();
        List<Categoria> categorias = List.of(
                new Categoria("CAT-01", "Salas")
        );

        assertThrows(
                GeminiException.class,
                () -> servicio.interpretarRespuestaIA(
                        "ACTIVIDAD=Reunión\n"
                                + "FECHA=2026-09-20\n"
                                + "HORA_INICIO=08:00\n"
                                + "HORA_FIN=10:00\n"
                                + "CATEGORIAS=CAT-99",
                        categorias
                )
        );
    }
}
