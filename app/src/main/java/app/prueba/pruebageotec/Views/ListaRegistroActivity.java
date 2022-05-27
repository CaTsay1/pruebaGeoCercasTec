package app.prueba.pruebageotec.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

import app.prueba.pruebageotec.Adapters.InfoAdapter;
import app.prueba.pruebageotec.Controladores.LoginController;
import app.prueba.pruebageotec.Modelos.Info;
import app.prueba.pruebageotec.Providers.InfoGeoProvider;
import app.prueba.pruebageotec.R;

public class ListaRegistroActivity extends AppCompatActivity {

    RadioButton RadEnter, RadDwell, RadExit;
    ImageView btn_Buscar;
    Button btn_Fecha;
    TextView txt_Fecha;

    RecyclerView mRecycler;

    LoginController mloginController;
    InfoGeoProvider mInfoGeoProvider;

    InfoAdapter mInfoAdapter, mmInfoAdapter;

    private String fechaBus;
    private String Type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_registro);
        inicializar();
        onClick();
    }

    private void onClick() {
        btn_Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFecha();
            }
        });

        btn_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatosSelector();
            }
        });
    }

    private void DatosSelector() {
        if (RadEnter.isChecked()){
            Type = "ENTER";
            VerificarBusqueda();
        }
        else if (RadDwell.isChecked()){
            Type = "DWELL";
            VerificarBusqueda();
        }
        else if (RadExit.isChecked()){
            Type = "EXIT";
            VerificarBusqueda();
        }
    }

    private void VerificarBusqueda() {
        if (fechaBus != null && !fechaBus.isEmpty()){
            if (Type != null && !Type.isEmpty()){
                busquedaTotal();
            }else {
                busquedaFecha();
            }
        }else if (Type != null && !Type.isEmpty()){
            busquedaTipo();
        }
        else {
            Toast.makeText(this, "Debe ingresar minimo un filtro de busqueda", Toast.LENGTH_SHORT).show();
        }
    }

    private void busquedaTipo() {
        Query query = mInfoGeoProvider.getInfoType(mloginController.getUid(),Type);
        FirestoreRecyclerOptions<Info> options = new FirestoreRecyclerOptions.Builder<Info>()
                .setQuery(query,Info.class)
                .build();

        mmInfoAdapter = new InfoAdapter(options, ListaRegistroActivity.this);
        mmInfoAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mmInfoAdapter);
        mmInfoAdapter.startListening();
    }

    private void busquedaFecha() {
        Query query = mInfoGeoProvider.getInfoFecha(mloginController.getUid(),fechaBus);
        FirestoreRecyclerOptions<Info> options = new FirestoreRecyclerOptions.Builder<Info>()
                .setQuery(query,Info.class)
                .build();

        mmInfoAdapter = new InfoAdapter(options, ListaRegistroActivity.this);
        mmInfoAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mmInfoAdapter);
        mmInfoAdapter.startListening();
    }

    private void busquedaTotal() {
        Query query = mInfoGeoProvider.getInfoCompleta(mloginController.getUid(),Type,fechaBus);
        FirestoreRecyclerOptions<Info> options = new FirestoreRecyclerOptions.Builder<Info>()
                .setQuery(query,Info.class)
                .build();

        mmInfoAdapter = new InfoAdapter(options, ListaRegistroActivity.this);
        mmInfoAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mmInfoAdapter);
        mmInfoAdapter.startListening();
    }

    private void getFecha() {
        final Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ListaRegistroActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String años  = Integer.toString(year);
                String meses = Integer.toString(month+1);
                String dias = Integer.toString(dayOfMonth);
                txt_Fecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                fechaBus = años+"/"+meses+"/"+dias;
            }
        }
                , dia , mes , ano);

        datePickerDialog.show();

    }

    private void inicializar() {
        RadDwell = findViewById(R.id.Radio_Dwell);
        RadEnter = findViewById(R.id.Radio_Enter);
        RadExit = findViewById(R.id.Radio_Exit);
        btn_Buscar = findViewById(R.id.btn_Search_Info);
        btn_Fecha = findViewById(R.id.btn_FechaBuscar);
        txt_Fecha = findViewById(R.id.txt_Fecha_Info);

        mInfoGeoProvider = new InfoGeoProvider();
        mloginController = new LoginController();

        mRecycler = findViewById(R.id.Recycler_Info);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListaRegistroActivity.this);
        mRecycler.setLayoutManager(linearLayoutManager);
     }

    @Override
    protected void onStart() {
        super.onStart();
        getInfo();
    }

    private void getInfo() {
        Query query = mInfoGeoProvider.getInfo(mloginController.getUid());
        FirestoreRecyclerOptions<Info> options = new FirestoreRecyclerOptions.Builder<Info>()
                .setQuery(query,Info.class)
                .build();

        mInfoAdapter = new InfoAdapter(options, ListaRegistroActivity.this);
        mInfoAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mInfoAdapter);
        mInfoAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mmInfoAdapter != null){
            mmInfoAdapter.stopListening();
        }
        mInfoAdapter.stopListening();
    }
}