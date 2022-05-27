package app.prueba.pruebageotec.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import app.prueba.pruebageotec.Modelos.Info;
import app.prueba.pruebageotec.R;
import dmax.dialog.SpotsDialog;

public class InfoAdapter extends FirestoreRecyclerAdapter<Info, InfoAdapter.ViewHolder> {

    Context context;


    android.app.AlertDialog mDialog;

    public InfoAdapter(FirestoreRecyclerOptions<Info> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull InfoAdapter.ViewHolder holder, int position, @NonNull @NotNull Info model) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String RutaId = document.getId();
        String nombre;
        String fecha;
        String hora;
        String Lon;
        String Lat;

        nombre = model.getTipo();
        holder.txtNombre.setText(nombre);

        fecha = model.getFecha();
        holder.txtFecha.setText(fecha);

        hora = model.getHora();
        holder.txtHora.setText(hora);

        Lon = model.getLongitud();
        holder.txtLon.setText(Lon);

        Lat = model.getLatitud();
        holder.txtLat.setText(Lat);


    }



    @NonNull
    @NotNull
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_info,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombre, txtFecha, txtHora, txtLon, txtLat;
        View viewHolders;


        public ViewHolder(View view){
            super(view);
            txtNombre= view.findViewById(R.id.Nombre_Tipo);
            txtFecha = view.findViewById(R.id.Fecha_Card);
            txtLat = view.findViewById(R.id.Latitud_Card);
            txtLon = view.findViewById(R.id.Longitud_Card);
            txtHora = view.findViewById(R.id.Hora_Card);
            viewHolders = view;
        }
    }

}
