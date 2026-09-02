package persistencia;

import modelo.Administrador;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "administradores")
public class Administradores {

    private List<Administrador> listado = new ArrayList<>();

    @XmlElement(name = "administrador")
    public List<Administrador> getListado() {
        return listado;
    }

    public void setListado(List<Administrador> listado) {
        this.listado = listado;
    }
}
