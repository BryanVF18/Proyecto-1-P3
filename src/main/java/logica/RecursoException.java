package logica;

//Excepciones para los casos de id vacío/repetido, descripción vacía, id no encontrado al modificar/eliminar

public class RecursoException extends Exception {
    public RecursoException(String message) {
        super(message);
    }
}
