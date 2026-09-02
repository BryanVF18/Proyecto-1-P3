package modelo;
// Entidad del negocio. No sabe nada de XML ni de pantallas,
// solo representa el dato tal como lo define el enunciado.

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "categoria")
@XmlAccessorType(XmlAccessType.FIELD)

public class Categoria {

    private String id;
    private String descripcion;

    public Categoria() {
        //Tengo entendido que JAXB necesita un constructor  vacio
    }

public Categoria(String id, String descripcion){
    this.id = id;
    this.descripcion = descripcion;
}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getDescripcion() {
    return descripcion;
}
public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
}
}
