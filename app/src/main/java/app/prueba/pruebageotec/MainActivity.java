package app.prueba.pruebageotec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import app.prueba.pruebageotec.Controladores.LoginController;
import app.prueba.pruebageotec.Login.LoginActivity;
import app.prueba.pruebageotec.Modelos.GeoFenM;
import app.prueba.pruebageotec.Providers.GeofenceProvider;
import app.prueba.pruebageotec.Utils.GeofenceBroadcastReceiver;
import app.prueba.pruebageotec.Utils.GeofenceHelper;
import app.prueba.pruebageotec.Views.ListaRegistroActivity;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static String TAG = "MainActivity";

    FloatingActionButton btn_List, btn_Clear, btn_Close;

    private GoogleMap mMap;
    SupportMapFragment mMapFragament;
    private GeofencingClient geofencingClient;

    //variables geofence
    private float GeoFence_Radius = 200;
    private GeofenceHelper geofenceHelper;
    private String GEOFENCE_ID = "SONE_GEOFENCE_ID";

    private final static int LOCATION_REQUEST_CODE = 1; //Bandera de permisos
    private final static int SETTINGS_REQUEST_CODE = 2;

    private final static int BACKGROUND_lOCATION = 2;

    //Propiedades para obtener la pocision con el gps
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private LatLng mPositionActual;

    LoginController mLoginController;
    GeofenceProvider mGeofenceProvider;

    int banCreate = 0;
    String lati;
    String longi;


    GeofenceBroadcastReceiver mGeoBroad;

    //LLamado para obtener la pocision cada vez que la persona se mueva
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    mPositionActual = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(16f)
                                    .build()
                    ));
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();
        getGeoCerca();
        onClick();
    }

    private void getGeoCerca() {

        mGeofenceProvider.getData(mLoginController.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("latitud")){
                        lati = documentSnapshot.getString("latitud");
                    }
                    if (documentSnapshot.contains("longitud")){
                        longi = documentSnapshot.getString("longitud");
                    }

                    double lat = Double.valueOf(lati);
                    double lon = Double.valueOf(longi);

                    LatLng mLant = new LatLng(lat,lon);
                    onMapLongClick(mLant);
                    banCreate = 2;
                }
            }
        });
    }

    private void onClick() {
        btn_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaRegistroActivity.class);
                startActivity(intent);
            }
        });

        btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                banCreate = 0;
                borrarBaseLtn();
            }
        });

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                banCreate = 0;
                logout();
            }
        });
    }

    private void logout() {
        mLoginController.Logout();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void inicializar() {

        btn_List = findViewById(R.id.btn_List);
        btn_Clear = findViewById(R.id.btn_Clear);
        btn_Close = findViewById(R.id.btn_Exit);

        mFusedLocation = LocationServices.getFusedLocationProviderClient(MainActivity.this);// con esta propiedad se puedde iniciar o deter la ubicacion cuando sea conveniente

        mMapFragament = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragament.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);

        mGeoBroad = new GeofenceBroadcastReceiver();

        mGeofenceProvider = new GeofenceProvider();
        mLoginController = new LoginController();
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);

        mLocationRequest = new LocationRequest();//instanciamos mLocationRequest
        mLocationRequest.setInterval(1000);// cada cuanto actualiza ubicacion
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //uso con la mayor precision de gps
        mLocationRequest.setSmallestDisplacement(5);

        startLocation();

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {  // pedimos permisos de localizacion
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // preguntamos si concede los permisos
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // si concedio el permiso
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions(); // metodo por si no concedio los permisos
            }
        }

        if (requestCode == BACKGROUND_lOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // preguntamos si concede los permisos
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // si concedio el permiso
                    Toast.makeText(this, "Deseas Agragar geofences....", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Necesita Permisos Background_location para geofences trigger....", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean gpsActived() { //metodo para saber si el usuario tiene el gps activo
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //si tiene el gps activado
            isActive = true;
        }
        return isActive;
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                                //hablita los permisos para localizacion del celular
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else {
                    showAlertDialogNOGPS();
                }
            } else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                showAlertDialogNOGPS();
            }
        }
    }

    private void showAlertDialogNOGPS() { // alert dialogo en caso de no tener activo el gps
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if (Build.VERSION.SDK_INT >= 29){
            //we need background permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){
                handleMapLongClick(latLng);
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    //showDialog Permisos
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_lOCATION);
                } else {
                    //showDialog Permisos
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_lOCATION);
                }
            }

        }else {
            handleMapLongClick(latLng);
        }
    }

    private void handleMapLongClick(LatLng latLng){



        if (banCreate == 2){
            banCreate =1;
            optionsCerca(latLng);
        }
        else if (banCreate == 1){
            optionsCerca(latLng);
            borrarBaseLtn();
            crearCerca(latLng);

        }
        else if (banCreate == 0){
            optionsCerca(latLng);
            crearCerca(latLng);
            banCreate = 1;
        }

    }

    private void optionsCerca(LatLng latLng) {
        mMap.clear();  //Con este metodo se borra el marker
        addMarker(latLng);
        addCircle(latLng, GeoFence_Radius);
        addGeofence(latLng, GeoFence_Radius);
    }

    private void crearCerca(LatLng latLng) {

        String latis = Double.toString(latLng.latitude);
        String longis = Double.toString(latLng.longitude);

        GeoFenM geof = new GeoFenM();
        geof.setId(mLoginController.getUid());
        geof.setLatitud(latis);
        geof.setLongitud(longis);
        mGeofenceProvider.create(geof, mLoginController.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                }else {
                    Toast.makeText(MainActivity.this, "No Se Pudo Crear GeoCerca", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void borrarBaseLtn() {
        mGeofenceProvider.borrarInformacion(mLoginController.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                }else {
                    Toast.makeText(MainActivity.this, "No Se Pudo Borrar la GeoCerca", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addGeofence(LatLng latLng, float radius) {
        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence
                .GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSucces: GeoFence Added....");
            }
        }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String ErrorMessage = geofenceHelper.getErrorCode(e);
                        Log.d(TAG, "OnFailure" + ErrorMessage);
                    }
                });
    }

    private  void addMarker(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private  void addCircle(LatLng latLng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);

    }

}