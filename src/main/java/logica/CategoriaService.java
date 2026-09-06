package logica;

import modelo.Categoria;
import persistencia.CategoriaDAO;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class CategoriaService {
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

         public List<Categoria> listarTodo(){
             return categoriaDAO.buscarTodas();
         }

    public List<Categoria> buscarPorNombre(String texto){
        List<Categoria> resultado = new ArrayList<>();
        List<Categoria> categorias = categoriaDAO.buscarTodas();
        String textoBusqueda = texto.toLowerCase().trim();

        for (int i = 0; i<categorias.size(); i++){
            Categoria c = categorias.get(i);

            if (c.getDescripcion()!=null && c.getDescripcion().toLowerCase().contains(textoBusqueda)){
                resultado.add(c);
            }
        }
        return  resultado;
    }

    public Categoria agregar (String descripcion) throws CategoriaException{

            if (descripcion==null || descripcion.trim().isEmpty()){
                throw new CategoriaException("La descripcion no puede estar vacia");
            }

            List<Categoria> categorias = categoriaDAO.buscarTodas();
            String nuevoId = generarSiguienteId(categorias);

            Categoria nueva = new Categoria(nuevoId, descripcion);
            categorias.add(nueva);
            categoriaDAO.guardarTodas(categorias);

            return nueva;
    }

    public void modificar(String id, String descripcion)throws CategoriaException{

        if (descripcion==null || descripcion.trim().isEmpty()){
            throw new CategoriaException("La descripcion no puede estar vacia");
        }

        List<Categoria> categorias = categoriaDAO.buscarTodas();
        boolean encontrado = false;

        for(int i = 0 ; i< categorias.size();i++){
            Categoria c = categorias.get(i);
            if (c.getId().equals(id)){
                c.setDescripcion((descripcion));
                encontrado = true;
                break;
            }
        }
        if (!encontrado){
            throw new CategoriaException("No existe la categoria con el ID: "+ id);
        }
        categoriaDAO.guardarTodas((categorias));
    }

    public void eliminar(String id) throws CategoriaException{
             List<Categoria> categorias = categoriaDAO.buscarTodas();
             Categoria aEliminar = null;

             for (int i = 0; i< categorias.size(); i++){
                 if(categorias.get(i).getId().equals(id)){
                     aEliminar = categorias.get(i);
                     break;
                 }
             }

             if (aEliminar == null){
                 throw new CategoriaException("No existe una categoria con el ID: "+ id);
             }
             categorias.remove(aEliminar);
             categoriaDAO.guardarTodas(categorias);
    }


    private String generarSiguienteId(List<Categoria> categorias) {

        int maximoActual = 0;

        for (int i = 0; i < categorias.size(); i++) {

            Categoria c = categorias.get(i);
            String parteNumerica = c.getId().substring(4);
            int numero = Integer.parseInt(parteNumerica);

            if (numero > maximoActual) {
                maximoActual = numero;
            }
        }
        return String.format("CAT-%06d", maximoActual + 1);
    }
}


