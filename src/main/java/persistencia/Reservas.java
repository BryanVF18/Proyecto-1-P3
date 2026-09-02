package persistencia;

import modelo.Reserva;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="reservas")
public class Reservas {

    private List<Reserva> listado=new ArrayList<>();

    @XmlElement(name="reserva")
    public List<Reserva> getListado() {
        return listado;
    }

    public void setListado(List<Reserva> listado) {
        this.listado = listado;
    }
}
