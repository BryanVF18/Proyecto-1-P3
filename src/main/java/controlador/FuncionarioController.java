package controlador;

import logica.FuncionarioException;
import logica.FuncionarioService;
import modelo.Funcionario;

import java.util.ArrayList;
import java.util.List;

/* Intermediario entre la vista (PanelFuncionarios) y la capa de
   logica (FuncionarioService). Asi la vista nunca conoce al DAO
   ni las reglas de negocio directamente, solo le pide cosas a este
   controlador y este decide a quien llamar. Es la pieza que
   materializa el patron MVC pedido en el enunciado. */
public class FuncionarioController {

    private final FuncionarioService funcionarioService = new FuncionarioService();

    public List<Funcionario> listarTodos() {
        return funcionarioService.listarTodos();
    }

    // Si viene un id se busca exacto; si no, se busca por nombre (contiene);
    // si ambos vienen vacios, se devuelve el listado completo.
    public List<Funcionario> buscar(String id, String nombre) {

        if (id != null && !id.trim().isEmpty()) {

            List<Funcionario> resultado = new ArrayList<>();
            Funcionario encontrado = funcionarioService.buscarPorId(id.trim());

            if (encontrado != null) {
                resultado.add(encontrado);
            }

            return resultado;
        }

        if (nombre != null && !nombre.trim().isEmpty()) {
            return funcionarioService.buscarPorNombre(nombre.trim());
        }

        return funcionarioService.listarTodos();
    }

    public void agregar(String id, String nombre, String telefono) throws FuncionarioException {
        funcionarioService.agregar(id, nombre, telefono);
    }

    public void modificar(String id, String nombre, String telefono) throws FuncionarioException {
        funcionarioService.modificar(id, nombre, telefono);
    }

    public void eliminar(String id) throws FuncionarioException {
        funcionarioService.eliminar(id);
    }
}
