package logica;

import modelo.SolicitudReserva;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ValidadorSolicitudReserva {

    public void validar(
            SolicitudReserva solicitud,
            LocalDate fechaActual
    ) throws ReservaException {

        if (solicitud == null) {
            throw new ReservaException(
                    "La solicitud de reserva no puede estar vacia"
            );
        }

        if (solicitud.getActividad() == null
                || solicitud.getActividad().isBlank()) {

            throw new ReservaException(
                    "Debe indicar el nombre de la actividad"
            );
        }

        if (solicitud.getFecha() == null) {
            throw new ReservaException(
                    "Debe seleccionar una fecha"
            );
        }

        if (fechaActual != null
                && solicitud.getFecha().isBefore(fechaActual)) {

            throw new ReservaException(
                    "No se puede reservar en una fecha pasada"
            );
        }

        if (solicitud.getHoraInicio() == null
                || solicitud.getHoraFin() == null) {

            throw new ReservaException(
                    "Debe indicar la hora inicial y la hora final"
            );
        }

        if (!solicitud.getHoraInicio().isBefore(
                solicitud.getHoraFin()
        )) {
            throw new ReservaException(
                    "La hora inicial debe ser anterior a la hora final"
            );
        }

        if (solicitud.getIdsCategorias().isEmpty()) {
            throw new ReservaException(
                    "Debe seleccionar al menos una categoria de recurso"
            );
        }

        Set<String> idsSinRepetir = new HashSet<>();

        for (String idCategoria : solicitud.getIdsCategorias()) {

            if (idCategoria == null || idCategoria.isBlank()) {
                throw new ReservaException(
                        "La categoria seleccionada no es valida"
                );
            }

            if (!idsSinRepetir.add(idCategoria)) {
                throw new ReservaException(
                        "No se puede seleccionar dos veces la categoria "
                                + idCategoria
                );
            }
        }
    }
}