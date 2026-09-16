package logica;

public class GeminiException extends Exception {

    public GeminiException(String mensaje) {
        super(mensaje);
    }

    public GeminiException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
