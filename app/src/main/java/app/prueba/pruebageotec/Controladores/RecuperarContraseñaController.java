package app.prueba.pruebageotec.Controladores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import app.prueba.pruebageotec.Utils.ProgressDialogUtil;

public class RecuperarContraseñaController {
    public static void recuperarContraseña(String correo,
                                           WeakReference<Activity> activityWeackReference,
                                           WeakReference<TextInputEditText>textCorreoWeackReference){
        WeakReference <ProgressDialog> progressDialogWeakReference = ProgressDialogUtil.getDialogWeak(
                activityWeackReference.get(),null,"Recuperando Contraseña ...");

        progressDialogWeakReference.get().show();

        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(correo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        Activity activity = activityWeackReference.get();
                        TextInputEditText txtCorreo = textCorreoWeackReference.get();
                        ProgressDialog progressDialog = progressDialogWeakReference.get();

                        if (activity != null && txtCorreo != null && progressDialog != null){
                            progressDialog.dismiss();
                            txtCorreo.setText("");
                            if (task.isSuccessful()){
                                Toast.makeText(activity,"Se ha enviado un correo para poder cambiar la contraseña",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(activity,"Se ha producido un error al intentar recuperar la contraseña",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }
}
