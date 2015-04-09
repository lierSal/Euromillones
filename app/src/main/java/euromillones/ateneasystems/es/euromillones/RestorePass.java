package euromillones.ateneasystems.es.euromillones;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;


public class RestorePass extends ActionBarActivity {
    /**
     * Variables para el asynctask
     */
    Button btn_restorePass;
    ProgressBar pb_cargando;
    TextView tv_respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_pass);
        //Declaracion de componentes
        final EditText et_mail = (EditText) findViewById(R.id.et_mail);
        btn_restorePass = (Button) findViewById(R.id.btn_restorePass);
        pb_cargando = (ProgressBar) findViewById(R.id.pb_cargando);
        tv_respuesta = (TextView) findViewById(R.id.tv_respuesta);


        //Funciones de inicio
        et_mail.setText(getIntent().getStringExtra("mail"));

        //Funciones de botones
        btn_restorePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestorePassAsync restorePass = new RestorePassAsync();
                restorePass.execute(String.valueOf(et_mail.getText()));
            }
        });

    }

    /**
     * funciones
     */
    //Funcion para cargar solicitar nueva contraseña
    public Boolean restorePass(String mail){
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        try {
            cadena.put("tarea", "Crear Key Restore Pass");//Le asignamos los datos que necesitemos
            cadena.put("datos", mail);//Le asignamos los datos que necesitemos

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
                    devovlerRespuesta = true;
                } else {
                devovlerRespuesta = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devovlerRespuesta;
    }
    /**
     * Asyntask Logins
     */
    private class RestorePassAsync extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            //Aqui llamamos a la funcion, como el array viene en params[0] tenemos que sacar
            //los diferentes campos del array con .get(x)
            //esta funcion devuelve otro array
            //Log.e("RECIBE",strings[0]);
            Boolean respuesta = false;
            respuesta = restorePass(strings[0]);
            return respuesta;
        }

        ;

        /**
         * Se ejecuta antes de empezar la conexion con la base de datos
         */
        protected void onPreExecute() {
            btn_restorePass.setVisibility(View.GONE);
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
        protected void onPostExecute(Boolean respuesta) {
            if (respuesta) {
                //pb_cargando.setVisibility(View.GONE);
                //btn_restorePass.setVisibility(View.VISIBLE);
                //Mostramos una notifacion
                Toast.makeText(getApplication(),"Enviado Correo Electronico",Toast.LENGTH_LONG).show();
                //Cerramos este activity
                finish();
            } else {
               // cargarPrivate();atenea
                pb_cargando.setVisibility(View.GONE);
                btn_restorePass.setVisibility(View.VISIBLE);
                //Mostramos mensaje de error
                tv_respuesta.setText("El Correo Electronico no existe en la Base de Datos");
            }
        }

    }


    
}
