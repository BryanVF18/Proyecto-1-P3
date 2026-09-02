package modelo;
// JAXB no sabe convertir LocalDate/LocalTime a XML por si solo.
// Este adaptador hace esa conversion: texto <-> objeto de fecha/hora.

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    @Override
    public LocalTime unmarshal(String valor){
        return LocalTime.parse(valor);
    }

    @Override
    public String marshal(LocalTime valor){
        return valor.toString();
    }
}
//unmarshal convierte texto del XML a un LocalDate (para cuando leemos)
//marshal hace lo contrario, convierte el LocalDate a texto (para cuando guardamos).