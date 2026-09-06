package logica;

import modelo.Funcionario;
import persistencia.FuncionarioDAO;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    public List<Funcionario> listarTodos() {
        return funcionarioDAO.buscarTodas();
    }

    public Funcionario buscarPorId(String id) {

        List<Funcionario> funcionarios = funcionarioDAO.buscarTodas();

        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario f = funcionarios.get(i);

            if (f.getId().equals(id)) {
                return f;
            }
        }

        return null;
    }

    public List<Funcionario> buscarPorNombre(String texto) {

        List<Funcionario> resultado = new ArrayList<>();
        List<Funcionario> funcionarios = funcionarioDAO.buscarTodas();
        String textoBusqueda = texto.toLowerCase().trim();

        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario f = funcionarios.get(i);

            if (f.getNombre() != null
                    && f.getNombre().toLowerCase().contains(textoBusqueda)) {
                resultado.add(f);
            }
        }

        return resultado;
    }

    public Funcionario agregar(String id, String nombre, String telefono) throws FuncionarioException {

        validarCamposObligatorios(id, nombre);

        if (buscarPorId(id) != null) {
            throw new FuncionarioException(
                    "Ya existe un funcionario con el id " + id
            );
        }

        Funcionario nuevo = new Funcionario(id, id, nombre, telefono);

        List<Funcionario> funcionarios = funcionarioDAO.buscarTodas();
        funcionarios.add(nuevo);
        funcionarioDAO.guardarTodas(funcionarios);

        return nuevo;
    }

    // No se permite modificar el id ni la clave desde aqui, solo nombre y telefono.
    public void modificar(String id, String nombre, String telefono) throws FuncionarioException {

        validarCamposObligatorios(id, nombre);

        List<Funcionario> funcionarios = funcionarioDAO.buscarTodas();
        boolean encontrado = false;

        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario f = funcionarios.get(i);

            if (f.getId().equals(id)) {
                f.setNombre(nombre);
                f.setTelefono(telefono);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new FuncionarioException(
                    "No existe un funcionario con el id " + id
            );
        }

        funcionarioDAO.guardarTodas(funcionarios);
    }

    public void eliminar(String id) throws FuncionarioException {

        List<Funcionario> funcionarios = funcionarioDAO.buscarTodas();
        Funcionario aEliminar = null;

        for (int i = 0; i < funcionarios.size(); i++) {
            if (funcionarios.get(i).getId().equals(id)) {
                aEliminar = funcionarios.get(i);
                break;
            }
        }

        if (aEliminar == null) {
            throw new FuncionarioException(
                    "No existe un funcionario con el id " + id
            );
        }

        funcionarios.remove(aEliminar);
        funcionarioDAO.guardarTodas(funcionarios);
    }

    private void validarCamposObligatorios(String id, String nombre) throws FuncionarioException {

        if (id == null || id.trim().isEmpty()) {
            throw new FuncionarioException("El id es obligatorio");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new FuncionarioException("El nombre es obligatorio");
        }
    }
}
