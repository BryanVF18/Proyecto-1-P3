package controlador;

import logica.CategoriaException;
import logica.CategoriaService;
import modelo.Categoria;

import java.util.List;

public class CategoriaController {

    private final CategoriaService categoriaService = new CategoriaService();

    public List<Categoria> listarTodos() {
        return categoriaService.listarTodo();
    }

    public List<Categoria> buscar(String descripcion) {

        if (descripcion != null && !descripcion.trim().isEmpty()) {
            return categoriaService.buscarPorNombre(descripcion.trim());
        }

        return categoriaService.listarTodo();
    }

    public Categoria agregar(String descripcion) throws CategoriaException {
        return categoriaService.agregar(descripcion);
    }

    public void modificar(String id, String descripcion) throws CategoriaException {
        categoriaService.modificar(id, descripcion);
    }

    public void eliminar(String id) throws CategoriaException {
        categoriaService.eliminar(id);
    }
}
