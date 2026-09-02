package modelo;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String valor){
        return LocalDate.parse(valor);
    }

    @Override
    public String marshal(LocalDate valor){
        return valor.toString();
    }
}
//unmarshal convierte texto del XML a un LocalDate (para cuando leemos)
//marshal hace lo contrario, convierte el LocalDate a texto (para cuando guardamos).