package app.prueba.pruebageotec.Controladores;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import app.prueba.pruebageotec.MainActivity;
import app.prueba.pruebageotec.Modelos.Usuario;
import app.prueba.pruebageotec.Utils.ProgressDialogUtil;

public class RegistroController {
    public static void registrarUsuario(String nombre, String correo, String contrasena,
                                        WeakReference<Activity> activityWeakReference){  //etse metodo guarda el usuario y la contraseña dentro de AUTH FIREBASE

        WeakReference<ProgressDialog> progressDialogWeakReference = ProgressDialogUtil.getDialogWeak(activityWeakReference.get(),null,"Registrando...");

        progressDialogWeakReference.get().show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            guradarUsuarioDataBase(nombre,correo, activityWeakReference,progressDialogWeakReference);

                        }else {
                            Activity activity = activityWeakReference.get();
                            ProgressDialog progressDialog = progressDialogWeakReference.get();
                            if (activity != null && progressDialog != null){
                                progressDialog.dismiss();
                                Toast.makeText(activity,"Error al intentar registrar usuario",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    public static void guradarUsuarioDataBase(String nombre, String correo,
                                              WeakReference<Activity> activityWeakReference,
                                              WeakReference<ProgressDialog> progressDialogWeakReference){ //etse metodo guarda el nombre y correo dentro de DATABASE FRIREBASE

        try {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//obtenemos el usuario actual

            String uid = firebaseUser.getUid();


            Usuario usuario = new Usuario(uid, correo,nombre);

            FirebaseFirestore.getInstance().collection("Usuarios")
                    .document(uid)
                    .set(usuario, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            Activity activity = activityWeakReference.get();
                            ProgressDialog progressDialog = progressDialogWeakReference.get();

                            if (activity != null && progressDialog != null ) {
                                progressDialog.dismiss();  // cancelamos el progres dialogo

                                if (task.isSuccessful()){    //comprobamos que la tarea se ha realizado satisfactoriamente
                                    Intent intent = new Intent(activity, MainActivity.class);//Pasamos a la pantalla principal
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);//con esto se elimina la pantalla de registro y login
                                    activity.startActivity(intent);

                                }else {
                                    Toast.makeText(activity,"Error al tratar de guardar los"
                                            +"datos del usuario en la nube", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

        }catch (NullPointerException e){
            e.getCause();
        }
    }
}
