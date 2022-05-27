package app.prueba.pruebageotec.Controladores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import app.prueba.pruebageotec.MainActivity;
import app.prueba.pruebageotec.Utils.ProgressDialogUtil;

public class LoginController {

    private FirebaseAuth mAuth;

    public LoginController(){mAuth=FirebaseAuth.getInstance();}

    public static void iniciarSesion(String correo, String contraseña,
                                     WeakReference<Activity> activityWeakReference,
                                     WeakReference<TextInputEditText>textContrasenaWeakReference){

        WeakReference<ProgressDialog> progressDialogWeakReference = ProgressDialogUtil.getDialogWeak(
                activityWeakReference.get(),null,"Iniciando Sesion ....");
        progressDialogWeakReference.get().show();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        Activity activity = activityWeakReference.get();
                        ProgressDialog progressDialog = progressDialogWeakReference.get();

                        if (activity != null && progressDialog != null){
                            progressDialog.dismiss();
                            if (task.isSuccessful()){

                                activity.startActivity(new Intent(activity, MainActivity.class));
                                activity.finish();
                            }else {
                                TextInputEditText txtContraseña = textContrasenaWeakReference.get();
                                if (txtContraseña != null){
                                    txtContraseña.setText("");
                                }
                                Toast.makeText(activity,"Se ha Producido un Error al "+
                                        "Intentar Iniciar Sesion",Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }

    public String getUid(){
        if (mAuth.getCurrentUser()!=null){
            return mAuth.getCurrentUser().getUid();
        }else {
            return null;
        }


    }


    public void Logout(){
        if (mAuth!=null){
            mAuth.signOut();
        }
    }

    public boolean existSession() {
        boolean exist = false;
        if (mAuth.getCurrentUser() != null) {
            exist = true;
        }
        return exist;
    }

}
