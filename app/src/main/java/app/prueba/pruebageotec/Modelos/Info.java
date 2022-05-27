package app.prueba.pruebageotec.Modelos;

public class Info {

    String id;
    String fecha;
    String tipo;
    String longitud;
    String latitud;
    String hora;

    public Info() {
    }

    public Info(String id, String fecha, String tipo, String longitud, String latitud, String hora) {
        this.id = id;
        this.fecha = fecha;
        this.tipo = tipo;
        this.longitud = longitud;
        this.latitud = latitud;
        this.hora = hora;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
}
