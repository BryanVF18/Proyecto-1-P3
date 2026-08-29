public class Administrador extends Usuario {

    public Administrador(String id, String clave, String rol) {
        super(id, clave, rol);
    }

    @Override
    public String toString() {
        return "Administrador{" + "id='" + getId() + '\'' + '}';
    }
}