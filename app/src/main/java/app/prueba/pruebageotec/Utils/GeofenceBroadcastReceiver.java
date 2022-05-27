package app.prueba.pruebageotec.Utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.List;

import app.prueba.pruebageotec.Controladores.LoginController;
import app.prueba.pruebageotec.MainActivity;
import app.prueba.pruebageotec.Modelos.Info;
import app.prueba.pruebageotec.Providers.GeofenceProvider;
import app.prueba.pruebageotec.Providers.InfoGeoProvider;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiver";

    private String punto;
    private LatLng mPositionActual;
    private String lati;
    private String longi;
    private String FechaMos;
    private String HoraMos;



    InfoGeoProvider mInfoGeoProvider;
    LoginController mLoginController;

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        mInfoGeoProvider = new InfoGeoProvider();
        mLoginController = new LoginController();

        NotificationHelper notificationHelper = new NotificationHelper(context);


        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);


        if (geofencingEvent.hasError()){
            Log.d(TAG, "OnReceiver: Error Receivin geofecing Event...");
            return;
        }



        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        //Punto de Activacion
        Location location = geofencingEvent.getTriggeringLocation();
        mPositionActual = new LatLng(location.getLatitude(), location.getLongitude());

        lati = Float.toString((float) mPositionActual.latitude);
        longi = Float.toString((float) mPositionActual.longitude);


        int transicionType = geofencingEvent.getGeofenceTransition();
        for (Geofence geofence: geofenceList){
            Log.d(TAG, "OnReceiver: Error Receivin:" + geofence.getRequestId());
        }

        switch (transicionType){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                punto = "ENTER";
                notificationHelper.sendHighPriorityNotification("ENTER","Has ingresado a la GeoCerca", MainActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                punto = "DWELL";
                notificationHelper.sendHighPriorityNotification("DWELL","Estas en la Geocerca", MainActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                punto = "EXIT";
                notificationHelper.sendHighPriorityNotification("EXIT","Saliste de la GeoCerca", MainActivity.class);
                break;
        }


        DatosFecha();

    }

    private void DatosFecha() {
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();

        int Dia = time.monthDay;
        int Mes = time.month;
        int Año = time.year;

        int MesTrue= Mes+1;

         String diaDis = Integer.toString(Dia);
         String mesDis = Integer.toString(MesTrue);
         String añoDis = Integer.toString(Año);

         FechaMos = añoDis + "/" + mesDis + "/" + diaDis;

        Calendar c = Calendar.getInstance();

        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);

        String HoraF = Integer.toString(hora);
        String MinF = Integer.toString(minuto);

        HoraMos = HoraF + ":" + MinF;

        guardarDatos();
    }

    private void guardarDatos() {
        Info info = new Info();
        info.setFecha(FechaMos);
        info.setHora(HoraMos);
        info.setTipo(punto);
        info.setLatitud(lati);
        info.setLongitud(longi);
        info.setId(mLoginController.getUid());
        mInfoGeoProvider.create(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

}



