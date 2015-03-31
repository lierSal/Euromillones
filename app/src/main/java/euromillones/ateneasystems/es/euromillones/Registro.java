package euromillones.ateneasystems.es.euromillones;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZMD5;


public class Registro extends ActionBarActivity {
    /**
     * Variables que se ponen aqui para poder acceder desde el AsycTask
     */
    ProgressBar pb_cargando;
    Button btn_registro;

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
        btn_registro = (Button) findViewById(R.id.btn_registro);
        pb_cargando = (ProgressBar) findViewById(R.id.pb_cargando);
        /**
         * Declaracion de variables
         */

        /**
         * Funcion de los botones
         */
        //BOTON REGISTRO
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> datos = new ArrayList<String>();
                datos.add(String.valueOf(et_user.getText()));
                datos.add(String.valueOf(et_pass.getText()));
                datos.add(String.valueOf(et_nombre.getText()));
                RegistroSegundoPlano registrarUsuario = new RegistroSegundoPlano();
                registrarUsuario.execute(datos);
            }
        });
        //Fin Boton Registro
    }

    /**
     * Funcion para mostrar mensajes toast
     */
    public void miToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    /**
     * Funcion para cargar el post registro
     */
    public void irPostRegistro() {
        //cerrar este activity y mostrar uno de que ya estas registrado
        final Intent actividadPostRegistro = new Intent(this, PostRegistroActivity.class);//Esto lo ponemos aqui porque dentro del boton no funciona
        //Log.e("Registro:", "COMPLETADO");
        startActivity(actividadPostRegistro);
        finish();
    }

    /**
     * Funcion para hacer registro
     */
    public ArrayList registroUsuario(String mail, String pass, String nombre) {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        ArrayList<String> returnRespuesta = new ArrayList<String>(); //Esto devolvera si es correcto o no
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
        respuestaJSON = conectBD.consultaSQLJSON(cadena);
        //Log.e("DATOS RECIBIDOS:", respuestaJSON.toString());
        try {
            //Ahora extraemos del JSON la parte "Respuesta" para saber si es un OK o un Error
            respuesta = respuestaJSON.getString("Respuesta");
            if (respuesta.equals("OK")) {
                Log.e("Entra en Devolver:", "True");
                returnRespuesta.add("true");

            } else {
                returnRespuesta.add("false");
                returnRespuesta.add(respuesta);
                /*Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
                devovlerRespuesta = false;*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnRespuesta;
    }

    ;

    /**
     * Prueba Asintask
     */
    private class RegistroSegundoPlano extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(ArrayList... params) {
            //Aqui llamamos a la funcion, como el array viene en params[0] tenemos que sacar
            //los diferentes campos del array con .get(x)
            //esta funcion devuelve otro array
            return registroUsuario(String.valueOf(params[0].get(0)), String.valueOf(params[0].get(1)), String.valueOf(params[0].get(2)));
        }

        ;

        /**
         * Se ejecuta antes de empezar la conexion con la base de datos
         */
        protected void onPreExecute() {
            btn_registro.setVisibility(View.GONE);
            pb_cargando.setVisibility(View.VISIBLE);
        }

        /**
         * Se ejecuta después de terminar "doInBackground".
         * <p/>
         * Se ejecuta en el hilo: PRINCIPAL
         * <p/>
         * //@param String con los valores pasados por el return de "doInBackground".
         */

        @Override
        protected void onPostExecute(ArrayList<String> respuesta) {
            if (respuesta.get(0).equals("true")) {
                irPostRegistro();
            } else if (respuesta.get(0).equals("false")) {
                btn_registro.setVisibility(View.VISIBLE);
                pb_cargando.setVisibility(View.GONE);
                miToast(respuesta.get(1));
            }
        }

    }

}
