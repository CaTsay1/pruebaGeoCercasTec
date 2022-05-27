package app.prueba.pruebageotec.Modelos;

import com.google.android.gms.maps.model.LatLng;

public class GeoFenM {

    String id;
    String longitud;
    String latitud;

    public GeoFenM() {
    }

    public GeoFenM(String id, String longitud, String latitud) {
        this.id = id;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
