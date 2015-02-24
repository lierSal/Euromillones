package euromillones.ateneasystems.es.euromillones;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.Clases.ZMD5;


public class CambioPassActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_pass);
        /**
         * Para versiones de Android superiores a la 2.3.7 necesitamos agregar estas lineas
         * asi funcionara cualquier conexion exterior
         */
        if (android.os.Build.VERSION.SDK_INT > 10) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /**
         * Declaración de componentes
         */
        final EditText et_pass = (EditText) findViewById(R.id.et_pass);
        final EditText et_pass_nuevo = (EditText) findViewById(R.id.et_pass_nuevo);
        final EditText et_pass_nuevo_2 = (EditText) findViewById(R.id.et_pass_nuevo_2);
        final TextView tv_respuesta = (TextView) findViewById(R.id.tv_respuesta);
        Button btn_guardar = (Button) findViewById(R.id.btn_guardar);
        /**
         * Declaracion de variables
         */
        final Intent actividadLogin = new Intent(this, Login_Activity.class);//Esto lo ponemos aqui porque dentro del boton no funciona
        /**
         * Funcion de los botones
         */
        //BOTON REGISTRO
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean cambioCompletado = true;
                cambioCompletado = cambioPass(String.valueOf(et_pass.getText()), String.valueOf(et_pass_nuevo.getText()), String.valueOf(et_pass_nuevo_2.getText()));
                if (cambioCompletado) {
                    //cerrar este activity y mostrar uno de que ya estas registrado
                    Log.e("Registro:", "COMPLETADO");
                    startActivity(actividadLogin);
                    finish();
                } else {
                    //mostrar error
                    Log.e("Registro:", "ERROR");
                }
            }
        });
        //Fin Boton Registro
    }

    /**
     * Funcion para hacer registro
     */
    public Boolean cambioPass(String pass, String passN, String passN2) {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        String cadenaJSONDatos = new String();//Esto es para pasarle varias variables en un texto plano
        String passGenerado = new String();//Aqui ira el pass completo
        ZMD5 md5 = new ZMD5(); //creamos la variable md5 que se usara para hacer lo necesario para el PASS
        ZDatosTemporales datosTemp = (ZDatosTemporales) getApplicationContext();//Para cargar datos temporales

        //Comprobamos si coincide la contraseña actual con la contraseña actual codificada
        if (md5.comprobarMD5(pass, datosTemp.getPassUser())) {//Para comprobar las dos contraseñas antes de cambiarlas
            //Si coinciden, ahora faltra comprobar que coinciden las dos siguientes
            if (passN.equals(passN2)) {
                //Si coinciden por lo tanto enviamos los datos
                //Generamos MD5
                passGenerado = md5.generarMD5(passN);//Le mandamos la contraseña y el se encarga de generar el salt y md5
                //Generamos JSON
                cadenaJSONDatos = "{\"mail\":\"" + datosTemp.getMailUser() + "\",\"pass\":\"" + passGenerado + "\"}";
                try {
                    cadena.put("tarea", "Actualizar Pass");//Le asignamos los datos que necesitemos
                    cadena.put("datos", cadenaJSONDatos);//Le asignamos los datos que necesitemos

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cadena.toString(); //Para obtener la cadena de texto de tipo JSON
                /**
                 * ENVIAMOS CONSULTA
                 */

                // Enviamos la consulta y metemos lo recibido dentro de la variable respuesta
                respuestaJSON = conectBD.consultaSQLJSON(cadena);
                //Log.e("DATOS RECIBIDOS:", respuestaJSON.toString());
                try {
                    //Ahora extraemos del JSON la parte "Respuesta" para saber si es un OK o un Error
                    respuesta = respuestaJSON.getString("Respuesta");
                    if (respuesta.equals("OK")) {
                        Toast.makeText(this, R.string.tv_pass_cambiado_correctamente, Toast.LENGTH_LONG).show();
                        devovlerRespuesta = true;

                    } else {
                        Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
                        devovlerRespuesta = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //Las dos contraseñas nuevas no coinciden
                Toast.makeText(this, R.string.tv_error_pass_nuevo, Toast.LENGTH_LONG).show();
                devovlerRespuesta = false;
            }
        } else {
            //La contraseña actual no es correcta
            Toast.makeText(this, R.string.tv_error_pass_actual, Toast.LENGTH_LONG).show();
            devovlerRespuesta = false;
        }


        return devovlerRespuesta;
    }

    ;

}
