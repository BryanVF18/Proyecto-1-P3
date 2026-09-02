package persistencia;

import modelo.Funcionario;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "funcionarios")
public class Funcionarios {

    private List<Funcionario> listado = new ArrayList<>();

    @XmlElement(name = "funcionario")
    public List<Funcionario> getListado() {
        return listado;
    }

    public void setListado(List<Funcionario> listado) {
        this.listado = listado;
    }
}