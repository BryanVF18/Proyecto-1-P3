package logica;

import modelo.Administrador;
import modelo.Funcionario;
import modelo.Usuario;
import persistencia.AdministradorDAO;
import persistencia.FuncionarioDAO;

import java.util.List;

public class LoginService {

    private final AdministradorDAO administradorDAO = new AdministradorDAO();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    public Usuario autenticar(String id, String clave) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null && usuario.getClave().equals(clave)) {
            return usuario;
        }
        return null;
    }

    public boolean cambiarClave(String id, String claveActual, String nuevaClave) {
        Usuario usuario = buscarPorId(id);
        if (usuario == null) {
            return false;
        }

        boolean cambioValido = usuario.cambiarClave(claveActual, nuevaClave);
        if (!cambioValido) {
            return false;
        }

        guardarSegunTipo(usuario);
        return true;
    }

    private Usuario buscarPorId(String id) {
        List<Administrador> administradores = administradorDAO.buscarTodas();
        for (Administrador admin : administradores) {
            if (admin.getId().equals(id)) {
                return admin;
            }
        }

        List<Funcionario> funcionarios = funcionarioDAO.buscarTodas();
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId().equals(id)) {
                return funcionario;
            }
        }

        return null;
    }

    private void guardarSegunTipo(Usuario usuario) {
        if (usuario instanceof Administrador) {
            List<Administrador> administradores = administradorDAO.buscarTodas();
            reemplazarEnLista(administradores, usuario);
            administradorDAO.guardarTodas(administradores);

        } else if (usuario instanceof Funcionario) {
            List<Funcionario> funcionarios = funcionarioDAO.buscarTodas();
            reemplazarEnLista(funcionarios, usuario);
            funcionarioDAO.guardarTodas(funcionarios);
        }
    }

    private <T extends Usuario> void reemplazarEnLista(List<T> lista, Usuario actualizado) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(actualizado.getId())) {
                lista.set(i, (T) actualizado);
                return;
            }
        }
    }
}