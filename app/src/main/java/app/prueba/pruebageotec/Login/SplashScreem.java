package app.prueba.pruebageotec.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.prueba.pruebageotec.MainActivity;

public class SplashScreem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //obtenemos el usuario actual

        if (user == null) {   //si es nulo es porq aun no inicia sesion
            starNewAct(LoginActivity.class);  // por lo anterior mostramos el login

        } else {
            starNewAct(MainActivity.class);  //si tiene sesion mostramos activity por el metodo starNewAct

        }

    }

    private void starNewAct(Class clase){
        startActivity(new Intent(SplashScreem.this,clase));
        finish();// este destruye esta pantalla (SplashScreen) una vez estemos en la siguiente

    }

}