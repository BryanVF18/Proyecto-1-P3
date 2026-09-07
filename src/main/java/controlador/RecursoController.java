package controlador;

import logica.RecursoException;
import logica.RecursoService;
import modelo.Recurso;

import java.util.ArrayList;
import java.util.List;

public class RecursoController {

    private final RecursoService recursoService = new RecursoService();

    public List<Recurso> listarTodos() {
        return recursoService.listarTodos();
    }

    public List<Recurso> buscar(String categoriaId, String descripcion) {

        List<Recurso> resultado;

        if (categoriaId != null && !categoriaId.trim().isEmpty()) {
            resultado = recursoService.filtrarPorCategoria(categoriaId.trim());
        } else {
            resultado = new ArrayList<>(recursoService.listarTodos());
        }

        if (descripcion != null && !descripcion.trim().isEmpty()) {
            resultado.retainAll(recursoService.buscarPorDescripcion(descripcion.trim()));
        }

        return resultado;
    }

    public Recurso agregar(String id, String categoria, String descripcion) throws RecursoException {
        return recursoService.agregar(id, categoria, descripcion);
    }

    public void modificar(String id, String categoria, String descripcion) throws RecursoException {
        recursoService.modificar(id, categoria, descripcion);
    }

    public void eliminar(String id) throws RecursoException {
        recursoService.eliminar(id);
    }
}
