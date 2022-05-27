package app.prueba.pruebageotec.Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import app.prueba.pruebageotec.Modelos.Info;

public class InfoGeoProvider {

    CollectionReference mCollection;

    public InfoGeoProvider() { mCollection = FirebaseFirestore.getInstance().collection("Informacion");
    }

    public Task<Void> create(Info info){ return mCollection.document().set(info); }

    public Task<DocumentSnapshot> getData (String id){ return mCollection.document(id).get(); }

    public Task<Void> borrarInformacion(String id){
        return mCollection.document(id).delete();
    }

    public Query getInfo(String idUser){
        return mCollection.whereEqualTo("id",idUser);
    }

    public Query getInfoCompleta(String idUser, String tipo, String fecha){
        return mCollection.whereEqualTo("id",idUser).whereEqualTo("tipo", tipo).whereEqualTo("fecha",fecha);
    }

    public Query getInfoFecha(String idUser, String fecha){
        return mCollection.whereEqualTo("id",idUser).whereEqualTo("fecha",fecha);
    }

    public Query getInfoType(String idUser, String tipo){
        return mCollection.whereEqualTo("id",idUser).whereEqualTo("tipo", tipo);
    }
}
