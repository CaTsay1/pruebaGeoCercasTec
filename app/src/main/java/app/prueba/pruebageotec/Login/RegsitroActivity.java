package app.prueba.pruebageotec.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.Objects;

import app.prueba.pruebageotec.Controladores.RegistroController;
import app.prueba.pruebageotec.R;
import app.prueba.pruebageotec.Utils.EmailUtils;

public class RegsitroActivity extends AppCompatActivity {

    private Button btnRegistrar;
    private TextInputEditText txtNombre, txtCorreo,txtContraseña, txtConfirmacionContraseña;


    String urlPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsitro);

        init();
        setLiseners();

    }

    private void setLiseners(){

        txtNombre.addTextChangedListener(textWatcher);
        txtCorreo.addTextChangedListener(textWatcher);
        txtContraseña.addTextChangedListener(textWatcher);
        txtConfirmacionContraseña.addTextChangedListener(textWatcher);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroController.registrarUsuario(getNombre(),getCorreo(),getContraseña(),// se llama a Regsitro controler
                        new WeakReference<>(RegsitroActivity.this));      // y se le pasa los datos
            }
        });

        txtConfirmacionContraseña.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE){
                    //registrar usuario con el teclado
                    if (PuedoRegistrarUsuario()){   //al darle en el boton de finalizar del teclado pregunta si puede registrar el usuario

                        RegistroController.registrarUsuario(getNombre(),getCorreo(),getContraseña(),  // se llama a Regsitro controler
                                new WeakReference<>(RegsitroActivity.this));      // y se le pasa los datos

                    }
                }

                return false;
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //cada vez q el usuario escriba en la caja de texto revisamos que cumpla con los requisitos llamando al siguiente metodo
            PuedoRegistrarUsuario();


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean PuedoRegistrarUsuario(){  //en este metodo revisamos los datos ingresados para ver si cumplen con lo requierido
        String nombre = getNombre().trim();
        String correo = getCorreo().trim();

        String contraseña = getContraseña().trim();
        String confirmarcontraseña = getConfirmacionContraseña().trim();


        if (nombre.length()>2&&
                EmailUtils.esCorreoValido(correo)&&
                contraseña.length()>5  &&
                confirmarcontraseña.equals(contraseña)){
            btnRegistrar.setEnabled(true);

            return true;
        }else {

            btnRegistrar.setEnabled(false);
            return false;
        }

    }

    public String getNombre() { return Objects.requireNonNull(txtNombre.getText()).toString(); }

    public String getCorreo() { return Objects.requireNonNull(txtCorreo.getText()).toString(); }

    public String getContraseña() { return Objects.requireNonNull(txtContraseña.getText()).toString();}

    public String getConfirmacionContraseña() { return txtConfirmacionContraseña.getText().toString();}


    private void init(){
        this.btnRegistrar = findViewById(R.id.btn_Registrar);
        this.txtNombre = findViewById(R.id.Nombre_Registro);
        this.txtCorreo = findViewById(R.id.Correo_Registro);
        this.txtContraseña = findViewById(R.id.Contraseña_Registro);
        this.txtConfirmacionContraseña = findViewById(R.id.Confirmar_Contraseña_Registro);

    }
}