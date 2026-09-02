package persistencia;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import modelo.Funcionario;

import java.io.File;
import java.util.List;

public class FuncionarioDAO {

    private static final String ARCHIVO = "funcionarios.xml";

    public void guardarTodas(List<Funcionario> funcionarios) {
        try {
            Funcionarios envoltorio = new Funcionarios();
            envoltorio.setListado(funcionarios);

            JAXBContext contexto = JAXBContext.newInstance(Funcionarios.class);
            Marshaller marshaller = (Marshaller) contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(envoltorio, new File(ARCHIVO));

        } catch (JAXBException e) {
            throw new RuntimeException("Error al guardar funcionarios", e);
        }
    }

    public List<Funcionario> buscarTodas() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new java.util.ArrayList<>();
        }
        try {
            JAXBContext contexto = JAXBContext.newInstance(Funcionarios.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            Funcionarios envoltorio = (Funcionarios) unmarshaller.unmarshal(archivo);
            return envoltorio.getListado();

        } catch (JAXBException e) {
            throw new RuntimeException("Error al leer funcionarios", e);
        }
    }
}