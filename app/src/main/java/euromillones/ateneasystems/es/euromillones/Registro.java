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
import euromillones.ateneasystems.es.euromillones.Clases.ZMD5;


public class Registro extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
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
        final EditText et_user = (EditText) findViewById(R.id.et_user);
        final EditText et_pass = (EditText) findViewById(R.id.et_pass);
        final EditText et_nombre = (EditText) findViewById(R.id.et_nombre);
        final TextView tv_respuesta = (TextView) findViewById(R.id.tv_respuesta);
        Button btn_registro = (Button) findViewById(R.id.btn_registro);
        /**
         * Declaracion de variables
         */
        final Intent actividadPostRegistro = new Intent(this, PostRegistroActivity.class);//Esto lo ponemos aqui porque dentro del boton no funciona
        /**
         * Funcion de los botones
         */
        //BOTON REGISTRO
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean registroCompletado = true;
                registroCompletado = registroUsuario(String.valueOf(et_user.getText()), String.valueOf(et_pass.getText()), String.valueOf(et_nombre.getText()));
                if (registroCompletado) {
                    //cerrar este activity y mostrar uno de que ya estas registrado
                    Log.e("Registro:", "COMPLETADO");
                    startActivity(actividadPostRegistro);
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
    public Boolean registroUsuario(String mail, String pass, String nombre) {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        String cadenaJSONDatos = new String();//Esto es para pasarle varias variables en un texto plano
        String passGenerado = new String();//Aqui ira el pass completo
        ZMD5 md5 = new ZMD5(); //creamos la variable md5 que se usara para hacer lo necesario para el PASS
        passGenerado = md5.generarMD5(pass);//Le mandamos la contraseña y el se encarga de generar el salt y md5
        Log.e("Mail", mail);
        Log.e("Nombre", nombre);
        Log.e("Pass", pass);
        Log.e("PassMD5", passGenerado);

        cadenaJSONDatos = "{\"mail\":\"" + mail + "\",\"nombre\":\"" + nombre + "\",\"pass\":\"" + passGenerado + "\"}";
        Log.e("JSON", cadenaJSONDatos);


        try {
            cadena.put("tarea", "Registro");//Le asignamos los datos que necesitemos
            cadena.put("datos", cadenaJSONDatos);//Le asignamos los datos que necesitemos

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON

        /**
         * ENVIAMOS CONSULTA
         */

        // Enviamos la consulta y metemos lo recibido dentro de la variable respuesta
        respuestaJSON = conectBD.consultaSQL(cadena);
        //Log.e("DATOS RECIBIDOS:", respuestaJSON.toString());
        try {
            //Ahora extraemos del JSON la parte "Respuesta" para saber si es un OK o un Error
            respuesta = respuestaJSON.getString("Respuesta");
            if (respuesta.equals("OK")) {
                Log.e("Entra en Devolver:", "True");
                devovlerRespuesta = true;

            } else {
                Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
                devovlerRespuesta = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devovlerRespuesta;
    }

    ;

}
