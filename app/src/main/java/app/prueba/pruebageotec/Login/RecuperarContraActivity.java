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

import app.prueba.pruebageotec.Controladores.RecuperarContraseñaController;
import app.prueba.pruebageotec.R;
import app.prueba.pruebageotec.Utils.EmailUtils;

public class RecuperarContraActivity extends AppCompatActivity {

    private Button btnRecuperarContraseña;
    private TextInputEditText txtCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);
        init();
        setListener();
    }

    private void setListener(){

        txtCorreo.addTextChangedListener(textWatcher);

        btnRecuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecuperarContraseñaController.recuperarContraseña(getCorreo(),
                        new WeakReference<>(RecuperarContraActivity.this),
                        new WeakReference<>(txtCorreo));
            }
        });

        txtCorreo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (puedeoRecuperarContraseña()){
                        RecuperarContraseñaController.recuperarContraseña(getCorreo(),
                                new WeakReference<>(RecuperarContraActivity.this),
                                new WeakReference<>(txtCorreo));
                    }

                }
                return false;
            }
        });

    }

    public String getCorreo() {
        return txtCorreo.getText().toString();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            puedeoRecuperarContraseña();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean puedeoRecuperarContraseña(){
        String correo = getCorreo().trim();

        if (EmailUtils.esCorreoValido(correo)){
            btnRecuperarContraseña.setEnabled(true);
            return true;
        }else{
            btnRecuperarContraseña.setEnabled(false);
            return false;
        }
    }

    private void init(){
        this.btnRecuperarContraseña = findViewById(R.id.btnEnviar_Recuperar);
        this.txtCorreo = findViewById(R.id.Txt_Recuperar_Correo);
    }
}