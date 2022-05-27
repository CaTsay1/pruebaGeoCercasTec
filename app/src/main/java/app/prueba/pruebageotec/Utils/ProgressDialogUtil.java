package app.prueba.pruebageotec.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import java.lang.ref.WeakReference;

public class ProgressDialogUtil {
    public static WeakReference<ProgressDialog> getDialogWeak(Context context, String title, String message)
    // metodo estatico que devuelve una referencia debil de el ProgressDialog
    //referencia debil o WeakReference es una referencia suave que no obliga al objeto a permanecer en la memoria
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false); // se desabilita la cancelacion del progreso cuando se toca cualquier parte de la pantalla

        if (title != null){   // mensaje y titulo es opcional y se lo comprueba que no sea nulo antes de setearlo
            progressDialog.setTitle(title);
        }
        if (message != null){
            progressDialog.setMessage(message);
        }
        return new WeakReference<>(progressDialog);
    }
}
