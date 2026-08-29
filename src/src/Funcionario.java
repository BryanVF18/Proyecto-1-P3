public class Funcionario extends Usuario {

    private String nombre;
    private String telefono;

    public Funcionario(String id, String clave, String rol, String nombre, String telefono) {
        super(id, clave, rol);
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

    @Override
    public String toString() {
        return "Funcionario{" + "id='" + getId() + '\'' + ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' + '}';
    }
}