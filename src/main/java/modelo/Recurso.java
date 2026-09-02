package modelo;
// Entidad del negocio. No sabe nada de XML ni de pantallas,
// solo representa el dato tal como lo define el enunciado.

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recurso")
@XmlAccessorType(XmlAccessType.FIELD)

public class Recurso {
    private String id; // numero de activo, lo asigna el usuario, NO es autogenerado (a diferencia de Categoria)
    private String categoria;
    private String descripcion;

    public Recurso() {
        //Constructor vacio para JAXB
    }

    public Recurso(String id, String categoria, String descripcion) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
