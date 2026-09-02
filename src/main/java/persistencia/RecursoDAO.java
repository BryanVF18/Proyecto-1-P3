package persistencia;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import modelo.Recurso;

import java.io.File;
import java.util.List;

public class RecursoDAO {

    private static final String ARCHIVO = "recursos.xml";

    public void guardarTodas(List<Recurso> recursos) {
        try {
            Recursos envoltorio = new Recursos();
            envoltorio.setListado(recursos);

            JAXBContext contexto = JAXBContext.newInstance(Recursos.class);
            Marshaller marshaller = (Marshaller) contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(envoltorio, new File(ARCHIVO));

        } catch (JAXBException e) {
            throw new RuntimeException("Error al guardar recursos", e);
        }
    }

    public List<Recurso> buscarTodas() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new java.util.ArrayList<>();
        }
        try {
            JAXBContext contexto = JAXBContext.newInstance(Recursos.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            Recursos envoltorio = (Recursos) unmarshaller.unmarshal(archivo);
            return envoltorio.getListado();

        } catch (JAXBException e) {
            throw new RuntimeException("Error al leer recursos", e);
        }
    }
}