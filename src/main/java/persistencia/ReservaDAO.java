package persistencia;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import modelo.Reserva;

import java.io.File;
import java.util.List;

public class ReservaDAO {

    private static final String ARCHIVO = "reservas.xml";

    public void guardarTodas(List<Reserva> reservas) {
        try {
            Reservas envoltorio = new Reservas();
            envoltorio.setListado(reservas);

            JAXBContext contexto = JAXBContext.newInstance(Reservas.class);
            Marshaller marshaller = (Marshaller) contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(envoltorio, new File(ARCHIVO));

        } catch (JAXBException e) {
            throw new RuntimeException("Error al guardar reservas", e);
        }
    }

    public List<Reserva> buscarTodas() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new java.util.ArrayList<>();
        }
        try {
            JAXBContext contexto = JAXBContext.newInstance(Reservas.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            Reservas envoltorio = (Reservas) unmarshaller.unmarshal(archivo);
            return envoltorio.getListado();

        } catch (JAXBException e) {
            throw new RuntimeException("Error al leer reservas", e);
        }
    }
}