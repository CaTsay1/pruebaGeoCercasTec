package app.prueba.pruebageotec.Modelos;

public class Usuario {

    private String id;
    private String correo;
    private String nombre;
    public Usuario() {
    }

    public Usuario(String id, String correo, String nombre) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
