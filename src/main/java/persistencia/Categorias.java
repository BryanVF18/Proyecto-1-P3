package persistencia;

import modelo.Categoria;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "categorias")
public class Categorias {

    private List<Categoria> listado = new ArrayList<>();

    @XmlElement(name = "categoria")
    public List<Categoria> getListado() {
        return listado;
    }

    public void setListado(List<Categoria> listado) {
        this.listado = listado;
    }
}