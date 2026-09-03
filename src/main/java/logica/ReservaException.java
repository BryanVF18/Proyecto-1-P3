package logica;

/*Esta excepción permite mostrar errores propios del módulo, por ejemplo:
-No se seleccionaron categorías
-La fecha ya pasó
-El horario es incorrecto
-No hay recursos disponibles*/
public class ReservaException extends Exception {

    public ReservaException(String mensaje) {
        super(mensaje);
    }
}