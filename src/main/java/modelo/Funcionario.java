package modelo;

public class Funcionario extends Usuario{
    private String nombre;
    private String telefono;

    public Funcionario(){
        super(null, null, "ADMINISTRADOR");
        // Constructor vacio requerido por JAXB para reconstruir el objeto al leer el XML.
        // Nunca se usa manualmente en el codigo del equipo.
    }

    public Funcionario(String id, String clave, String nombre, String telefono) {
        super(id, clave, "FUNCIONARIO");
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
