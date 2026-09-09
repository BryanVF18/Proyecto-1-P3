package logica;

import modelo.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    @Test
    public void credencialesIncorrectasNoPermitenIngresar() {

        LoginService loginService = new LoginService();

        Usuario usuario = loginService.autenticar(
                "usuario-que-no-existe",
                "clave-incorrecta"
        );

        assertNull(usuario);
    }
}