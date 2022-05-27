package app.prueba.pruebageotec.Utils;

import android.util.Patterns;

public class EmailUtils {
    public static boolean esCorreoValido (String correo){
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }
}
