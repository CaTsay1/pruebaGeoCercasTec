package app.prueba.pruebageotec.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;

import app.prueba.pruebageotec.Controladores.LoginController;
import app.prueba.pruebageotec.R;
import app.prueba.pruebageotec.Utils.EmailUtils;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegistrar, btnEntrar;
    private TextInputEditText txtCorreo, txtContraseña;
    private TextView btnRecuperarContraseña;
    int bandera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializar();
        conectividad();
        onclick();
    }

    private void conectividad() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            // Conection.setVisibility(View.GONE);
            bandera = 0;
            setlisener();  //metodo para mantener la animacion ilimitadamente
        }else {
            // Conection.setVisibility(View.VISIBLE);
            bandera = 1;
            Toast.makeText(LoginActivity.this, "No Se Pudo Conectar A Internet " +
                    "Verifique El Acceso A Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setlisener(){

        txtCorreo.addTextChangedListener(textWatcher);
        txtContraseña.addTextChangedListener(textWatcher);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginController.iniciarSesion(getCorreo(),getContraseña(),
                        new WeakReference<>(LoginActivity.this),
                        new WeakReference<>(txtContraseña));

            }
        });

        txtContraseña.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (puedoIniciarSesion()){
                        LoginController.iniciarSesion(getCorreo(),getContraseña(),
                                new WeakReference<>(LoginActivity.this),
                                new WeakReference<>(txtContraseña));
                    }
                }

                return false;
            }
        });


    }

    public String getCorreo() {
        return txtCorreo.getText().toString();
    }

    public String getContraseña() {
        return txtContraseña.getText().toString();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            puedoIniciarSesion();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean puedoIniciarSesion(){
        String correo = getCorreo().trim();
        String contraseña = getContraseña().trim();

        if (EmailUtils.esCorreoValido(correo) && contraseña.length()>5){
            btnEntrar.setEnabled(true);
            return true;

        }else {
            btnEntrar.setEnabled(false);
            return false;
        }

    }

    private void onclick() {

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bandera == 0){
                    startActivity(new Intent(LoginActivity.this, RegsitroActivity.class));
                }
                else if (bandera == 1){
                    Toast.makeText(LoginActivity.this, "No Se Pudo Conectar A Internet " +
                            "Verifique El Acceso A Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRecuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bandera == 0){
                    startActivity(new Intent(LoginActivity.this,RecuperarContraActivity.class));
                }
                else if (bandera == 1){
                    Toast.makeText(LoginActivity.this, "No Se Pudo Conectar A Internet " +
                            "Verifique El Acceso A Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void inicializar() {
        this.btnRegistrar = findViewById(R.id.btnRegistro);
        this.btnEntrar = findViewById(R.id.btnEntrarLogin);
        this.txtCorreo = findViewById(R.id.Txt_Correo_Login);
        this.txtContraseña = findViewById(R.id.Txt_Contraseña_Login);
        this.btnRecuperarContraseña= findViewById(R.id.btnRecuperarContraseña);
    }
}