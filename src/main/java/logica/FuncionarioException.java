package logica;

/* Excepcion propia del modulo de Funcionarios. Se usa para reportar
   errores de validacion o de reglas de negocio, por ejemplo:
   - Campos obligatorios vacios
   - Id de funcionario repetido
   - Intentar modificar/eliminar un id que no existe
   Sigue el mismo criterio que ReservaException, para mantener el
   mismo estilo de manejo de errores en todo el proyecto. */
public class FuncionarioException extends Exception {

    public FuncionarioException(String mensaje) {
        super(mensaje);
    }
}
