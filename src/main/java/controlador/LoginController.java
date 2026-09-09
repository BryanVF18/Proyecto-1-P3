package controlador;

import logica.LoginService;
import modelo.Usuario;

public class LoginController {

    private final LoginService loginService;

    public LoginController() {
        loginService = new LoginService();
    }

    public Usuario autenticar(String id, String clave) {
        return loginService.autenticar(id, clave);
    }

    public boolean cambiarClave(String id, String claveActual, String nuevaClave) {
        return loginService.cambiarClave(id, claveActual, nuevaClave);
    }
}