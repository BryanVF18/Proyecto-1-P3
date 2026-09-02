package persistencia;

import modelo.Recurso;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="recursos")
public class Recursos {

    private List<Recurso> listado = new ArrayList<>();

    @XmlElement(name="recurso")
    public List<Recurso> getListado() {
        return listado;
    }

    public void setListado(List<Recurso> listado) {
        this.listado = listado;
    }
}
