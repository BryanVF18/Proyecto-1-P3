package persistencia;
//Leer comentarios, aplican para todos los DAOs
//RecursoDAO/ReservaDAO/AdministradorDAO/FuncionarioDAO

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import modelo.Administrador;

import java.io.File;
import java.util.List;

// Unico punto del sistema que sabe leer/escribir categorias.xml.
// El resto del programa solo llama guardarTodas() / buscarTodas(),
// sin preocuparse de como se guarda por dentro.
public class AdministradorDAO {

    private static final String ARCHIVO = "administradores.xml";

    // Convierte la lista completa a XML y la escribe en el archivo.
    public void guardarTodas(List<Administrador> administradores) {
        try {
            Administradores envoltorio = new Administradores();
            envoltorio.setListado(administradores);

            JAXBContext contexto = JAXBContext.newInstance(Administradores.class);
            Marshaller marshaller = (Marshaller) contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(envoltorio, new File(ARCHIVO));

        } catch (JAXBException e) {
            throw new RuntimeException("Error al guardar administradores", e);
        }
    }

    // Lee el archivo y lo convierte de vuelta en una lista de objetos.
    // Si el archivo no existe todavia (primera ejecucion), devuelve lista vacia.
    public List<Administrador> buscarTodas() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new java.util.ArrayList<>();
        }
        try {
            JAXBContext contexto = JAXBContext.newInstance(Administradores.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            Administradores envoltorio = (Administradores) unmarshaller.unmarshal(archivo);
            return envoltorio.getListado();

        } catch (JAXBException e) {
            throw new RuntimeException("Error al leer administradores", e);
        }
    }
}