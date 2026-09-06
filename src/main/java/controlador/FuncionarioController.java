package controlador;

import logica.FuncionarioException;
import logica.FuncionarioService;
import modelo.Funcionario;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioController {

    private final FuncionarioService funcionarioService = new FuncionarioService();

    public List<Funcionario> listarTodos() {
        return funcionarioService.listarTodos();
    }

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
