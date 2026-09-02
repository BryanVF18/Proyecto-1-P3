package modelo;

public class Administrador extends Usuario{


    public Administrador(){
        super(null, null, "ADMINISTRADOR");
        // Constructor vacio requerido por JAXB para reconstruir el objeto al leer el XML.
        // Nunca se usa manualmente en el codigo del equipo.
    }

    public Administrador(String id, String clave) {
        super(id, clave, "ADMINISTRADOR");
    }
}
