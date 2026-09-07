package logica;


import modelo.Recurso;
import persistencia.RecursoDAO;
import java.util.List;
import java.util.ArrayList;

public class RecursoService {
    private final RecursoDAO recursoDAO = new RecursoDAO();

    public List<Recurso> listarTodos(){
        return recursoDAO.buscarTodas();
    }

    public List<Recurso> filtrarPorCategoria(String categoriaId){
        List<Recurso> resultado = new ArrayList<>();
        List<Recurso> recursos = recursoDAO.buscarTodas();
        String textoBusqueda = categoriaId.trim();

        for (int i = 0; i<recursos.size(); i++){
            Recurso r = recursos.get(i);

            if (r.getCategoria()!=null && r.getCategoria().equals(textoBusqueda)){
                resultado.add(r);
            }
        }
        return  resultado;
    }

    public Recurso buscarPorId(String id) {

        List<Recurso> recursos = recursoDAO.buscarTodas();

        for (int i = 0; i < recursos.size(); i++) {
            Recurso r = recursos.get(i);

            if (r.getId().equals(id)) {
                return r;
            }
        }

        return null;
    }

    public List<Recurso> buscarPorDescripcion(String texto) {

        List<Recurso> resultado = new ArrayList<>();
        List<Recurso> recursos = recursoDAO.buscarTodas();
        String textoBusqueda = texto.toLowerCase().trim();

        for (int i = 0; i < recursos.size(); i++) {
            Recurso r = recursos.get(i);

            if (r.getDescripcion() != null
                    && r.getDescripcion().toLowerCase().contains(textoBusqueda)) {
                resultado.add(r);
            }
        }

        return resultado;
    }

    private void validarCamposObligatorios(String id, String categoria, String descripcion) throws RecursoException {

        if (id == null || id.trim().isEmpty()) {
            throw new RecursoException("El id es obligatorio");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            throw new RecursoException("La categoria es obligatoria");
        }
        if(descripcion == null || descripcion.trim().isEmpty()){
            throw new RecursoException("La descripcion es obligatoria");
        }
    }

    public Recurso agregar(String id, String categoria, String descripcion) throws RecursoException {

        validarCamposObligatorios(id, categoria, descripcion);
        if (buscarPorId(id) != null) {
            throw new RecursoException(
                    "Ya existe un recurso con el id " + id
            );
        }

        Recurso nuevo = new Recurso(id, categoria, descripcion);

        List<Recurso> recursos = recursoDAO.buscarTodas();
        recursos.add(nuevo);
        recursoDAO.guardarTodas(recursos);
        return nuevo;
    }

    public void modificar(String id, String categoria, String descripcion) throws RecursoException {

        validarCamposObligatorios(id, categoria, descripcion);

        List<Recurso> recursos = recursoDAO.buscarTodas();
        boolean encontrado = false;

        for (int i = 0; i < recursos.size(); i++) {
            Recurso r = recursos.get(i);

            if (r.getId().equals(id)) {
                r.setCategoria(categoria);
                r.setDescripcion(descripcion);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new RecursoException(
                    "No existe un recurso con el id " + id
            );
        }

        recursoDAO.guardarTodas(recursos);
    }

    public void eliminar(String id) throws RecursoException {

        List<Recurso> recursos = recursoDAO.buscarTodas();
        Recurso aEliminar = null;

        for (int i = 0; i < recursos.size(); i++) {
            if (recursos.get(i).getId().equals(id)) {
                aEliminar = recursos.get(i);
                break;
            }
        }

        if (aEliminar == null) {
            throw new RecursoException(
                    "No existe un recurso con el id " + id
            );
        }

        recursos.remove(aEliminar);
        recursoDAO.guardarTodas(recursos);
    }
}

