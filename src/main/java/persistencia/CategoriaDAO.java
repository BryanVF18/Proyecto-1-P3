package persistencia;

import modelo.Categoria;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private static final String ARCHIVO = "categorias.xml";

    public void guardarTodas(List<Categoria> categorias) {
        try {
            Categorias envoltorio = new Categorias();
            envoltorio.setListado(categorias);

            JAXBContext contexto = JAXBContext.newInstance(Categorias.class);
            Marshaller marshaller = contexto.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(
                    envoltorio,
                    new File(ARCHIVO)
            );

        } catch (JAXBException e) {
            throw new RuntimeException("Error al guardar categorias", e);
        }
    }

    public List<Categoria> buscarTodas() {

        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try {
            JAXBContext contexto = JAXBContext.newInstance(Categorias.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();

            Categorias envoltorio = (Categorias) unmarshaller.unmarshal(archivo);

            if (envoltorio.getListado() == null) {
                return new ArrayList<>();
            }

            return envoltorio.getListado();

        } catch (JAXBException e) {
            throw new RuntimeException("Error al leer categorias", e);
        }
    }
}