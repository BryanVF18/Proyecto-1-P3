//Prueba para corroborar que los metodos estan bien implementados

import modelo.Administrador;
import logica.LoginService;
import persistencia.AdministradorDAO;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Creamos un administrador de prueba y lo guardamos
        AdministradorDAO administradorDAO = new AdministradorDAO();
        List<Administrador> administradores = new ArrayList<>();
        administradores.add(new Administrador("admin", "1234"));
        administradorDAO.guardarTodas(administradores);

        System.out.println("Administrador guardado. Revisa si apareció el archivo administradores.xml");

        // 2. Probamos el login con la clave correcta
        LoginService loginService = new LoginService();
        Administrador logueado = (Administrador) loginService.autenticar("admin", "1234");

        if (logueado != null) {
            System.out.println("Login exitoso para: " + logueado.getId());
        } else {
            System.out.println("Login fallido");
        }

        // 3. Probamos el cambio de clave
        boolean cambioExitoso = loginService.cambiarClave("admin", "1234", "clave-nueva");
        System.out.println("Cambio de clave exitoso: " + cambioExitoso);

        // 4. Confirmamos que la clave nueva ya quedó guardada
        Administrador conClaveNueva = (Administrador) loginService.autenticar("admin", "clave-nueva");
        System.out.println("Login con clave nueva funciona: " + (conClaveNueva != null));
    }
}

//Metodos probados, si corre correctamente
//CAMBIAR EL MAIN PARAPROBAR LAS DEMAS IMPLE,MENTACIONES