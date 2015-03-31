package euromillones.ateneasystems.es.euromillones;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
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
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.Clases.ZMD5;


public class CambioPassActivity extends ActionBarActivity {
    /**
     * Declaracion de componentes
     */
    private EditText et_pass;
    private EditText et_pass_nuevo;
    private EditText et_pass_nuevo_2;
    private TextView tv_respuesta;
    private Button btn_guardar;
    private ProgressBar pb_cargando;


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
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_pass_nuevo = (EditText) findViewById(R.id.et_pass_nuevo);
        et_pass_nuevo_2 = (EditText) findViewById(R.id.et_pass_nuevo_2);
        tv_respuesta = (TextView) findViewById(R.id.tv_respuesta);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        pb_cargando = (ProgressBar) findViewById(R.id.pb_cargando);
        /**
         * Declaracion de variables
         */

        /**
         * Funcion de los botones
         */
        //BOTON REGISTRO
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> datos = new ArrayList<String>();
                //datos.add("Guardar");
                datos.add(String.valueOf(et_pass.getText()));
                datos.add(String.valueOf(et_pass_nuevo.getText()));
                datos.add(String.valueOf(et_pass_nuevo_2.getText()));
                PassUser datosUser = new PassUser();
                datosUser.execute(datos);


                /*Boolean cambioCompletado = true;
                cambioCompletado = cambioPass(String.valueOf(et_pass.getText()), String.valueOf(et_pass_nuevo.getText()), String.valueOf(et_pass_nuevo_2.getText()));
                if (cambioCompletado) {
                    //cerrar este activity y mostrar uno de que ya estas registrado
                    Log.e("Registro:", "COMPLETADO");
                    startActivity(actividadLogin);
                    finish();
                } else {
                    //mostrar error
                    Log.e("Registro:", "ERROR");
                }*/
            }
        });
        //Fin Boton Registro
    }
    /**
     * Funcion para comprobar los datos antes de enviar
     */


    /**
     * Funcion para hacer registro
     */
    private ArrayList cambioPass(String pass, String passN, String passN2) {
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
        ArrayList respuestaReturn = new ArrayList();//para responder

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
                        /*Toast.makeText(this, R.string.tv_pass_cambiado_correctamente, Toast.LENGTH_LONG).show();
                        devovlerRespuesta = true;*/
                        respuestaReturn.add("OK");
                        respuestaReturn.add(getApplicationContext().getString(R.string.tv_pass_cambiado_correctamente));

                    } else {
                        /*Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
                        devovlerRespuesta = false;*/
                        respuestaReturn.add("Error");
                        respuestaReturn.add(respuesta);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //Las dos contraseñas nuevas no coinciden
                /*Toast.makeText(this, R.string.tv_error_pass_nuevo, Toast.LENGTH_LONG).show();
                devovlerRespuesta = false;*/
                respuestaReturn.add("Error");
                respuestaReturn.add(getApplicationContext().getString(R.string.tv_error_pass_nuevo));
            }
        } else {
            //La contraseña actual no es correcta
            /*Toast.makeText(this, R.string.tv_error_pass_actual, Toast.LENGTH_LONG).show();
            devovlerRespuesta = false;*/
            respuestaReturn.add("Error");
            respuestaReturn.add(getApplicationContext().getString(R.string.tv_error_pass_actual));
        }


        return respuestaReturn;
    }

    ;

    /**
     * Funcion para mostrat Toast
     */
    public void miToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    /**
     * Funcion para ir al login
     */
    public void cargarLogin() {
        final Intent actividadLogin = new Intent(this, Login_Activity.class);
        startActivity(actividadLogin);
        finish();
    }

    /**
     * Asintask para guardar contraseña del usuario
     */
    private class PassUser extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(ArrayList... params) {
            //Lo siguiente hay que eliminarlo ya que lo uso para probar el cargador
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Aqui llamamos a la funcion, como el array viene en params[0] tenemos que sacar
            //los diferentes campos del array con .get(x)
            //esta funcion devuelve otro un ArrayList
            Boolean resultado;//Para hacer una comprobacion
            JSONObject datosUser;
            ArrayList<String> respuesta = new ArrayList<String>();//para mandar al ultimo paso
            //If para las funciones
            //if (String.valueOf(params[0].get(0)).equals("Guardar")) {
            // respuesta.add("Guardar");
            return cambioPass(String.valueOf(params[0].get(0)), String.valueOf(params[0].get(1)), String.valueOf(params[0].get(2)));

        }

        ;

        /**
         * Se ejecuta antes de empezar la conexion con la base de datos
         */
        protected void onPreExecute() {
            btn_guardar.setVisibility(View.GONE);
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
            if (respuesta.get(0).equals("OK")) {
                miToast(respuesta.get(1));
                cargarLogin();
            } else {
                btn_guardar.setVisibility(View.VISIBLE);
                pb_cargando.setVisibility(View.GONE);
                miToast(respuesta.get(1));
            }
        }

    }

}
