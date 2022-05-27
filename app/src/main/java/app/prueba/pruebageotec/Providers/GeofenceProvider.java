package app.prueba.pruebageotec.Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.prueba.pruebageotec.Modelos.GeoFenM;

public class GeofenceProvider {

    CollectionReference mCollection;

    public GeofenceProvider() { mCollection = FirebaseFirestore.getInstance().collection("Geofence");
    }

    public Task<Void> create(GeoFenM geoFenM, String id){ return mCollection.document(id).set(geoFenM); }

    public Task<DocumentSnapshot> getData (String id){ return mCollection.document(id).get(); }

    public Task<Void> borrarInformacion(String id){
        return mCollection.document(id).delete();
    }
}
