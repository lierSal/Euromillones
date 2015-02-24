package euromillones.ateneasystems.es.euromillones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.Clases.ZMD5;


public class Login_Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        final TextView tv_respuesta = (TextView) findViewById(R.id.tv_respuesta);
        final CheckBox cb_recordar = (CheckBox) findViewById(R.id.cb_recordar);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_registro = (Button) findViewById(R.id.btn_registro);

        /**
         * Declaracion de variables
         */
        String passCodConfig = new String();
        String user = new String();
        final Intent actividadPrivate = new Intent(this, PrivateActivity.class);//Esto lo ponemos aqui porque dentro del boton no funciona
        final Intent actividadRegistro = new Intent(this, Registro.class);//Esto lo ponemos aqui porque dentro del boton no funciona

        /**
         * Primero cargamos la informacion del archivo de configuracion
         */
        final SharedPreferences config = getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);
        //Primero comprobamos si es la primera vez que se ha iniciado
        // si es la primera vez es cargaremos unos valores de inicio
        if (config.getBoolean("primerInicio", true)) {
            /**
             * Guardar datos
             */
            SharedPreferences.Editor editor = config.edit();

            editor.putBoolean("primerInicio", false);//Para decir que no es el primer inicio
            //ahora añadimos la configuracion que nos interesa
            editor.putString("cantidadUltimosResultados", "10");
            editor.commit();
        }
        //Ahora decimos que si el valor de checkbox es true que cargue los datos si no, nada
        //Le ponemos como segundo parametro un false para que nos devuelva false en caso de no
        //existir ese parametro.
        if (config.getBoolean("checkRecordarLogin", false)) {
            Log.e("Entra en:", "Si que hay datos");
            passCodConfig = config.getString("passCod", "");
            user = config.getString("user", "");
            if (autologin(user, passCodConfig)) {
                //Abrir siguiente activity
                startActivity(actividadPrivate);
                //Eliminamos este activity
                finish();
            } else {
                if (eliminarConfig()) {
                    tv_respuesta.setText(R.string.tv_User_Error);
                }

            }
        } else {
            Log.e("Entra en:", "No que hay datos");
            Log.e("Configuracion", String.valueOf(config.getBoolean("checkRecordarLogin", false)));
        }

        /**
         * Funcion de los botones
         */
        //BOTON LOGIN
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hacemos un if para saber si comprobarUsuario nos devuelve un true, en caso afirmativo
                //autorizamos el acceso
                if (comprobarUsuario(String.valueOf(et_user.getText()), String.valueOf(et_pass.getText()))) {
                    //Contraseña correcta
                    //Comprobamos si quiere recoedar los datos
                    if (cb_recordar.isChecked()) {
                        Log.e("Entra:", "Cheked");
                        //Quiere recordar los datos
                        /**
                         * Guardar datos si esta marcado el checkbox
                         */
                        SharedPreferences.Editor editor = config.edit();
                        ZDatosTemporales DU = (ZDatosTemporales) getApplicationContext();
                        editor.putString("id", DU.getIdUser());
                        editor.putString("nombre", DU.getNombreUser());
                        editor.putString("user", DU.getMailUser());
                        editor.putString("nivel", DU.getNivelUser());
                        editor.putString("passCod", DU.getPassUser());
                        editor.putBoolean("checkRecordarLogin", true);
                        editor.commit();
                        //Abrimos el nuevo activity

                    } else {
                        Log.e("Entra:", "NO Cheked");
                        //No quiere recordar los datos
                        SharedPreferences.Editor editor = config.edit();
                        editor.putString("id", "");
                        editor.putString("nombre", "");
                        editor.putString("user", "");
                        editor.putString("nivel", "");
                        editor.putBoolean("checkRecordarLogin", false);
                        editor.commit();
                    }
                    //Abrir siguiente activity
                    startActivity(actividadPrivate);
                    //Y cerramos esta
                    finish();

                } else {
                    //Usuario incorrecto
                    tv_respuesta.setText(R.string.tv_User_Error);
                }


            }
        });//Fin boton de login
        //BOTON REGISTRO
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(actividadRegistro);
            }
        });
        //Fin boton de Registro


    }

    /**
     * Funcion para hacer la consulta de login
     */
    public Boolean comprobarUsuario(String user, String pass) {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        try {
            cadena.put("tarea", "Login");//Le asignamos los datos que necesitemos
            cadena.put("datos", user);//Le asignamos los datos que necesitemos

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
                //Si es un OK, llamaremos a las CLASES ZMD5 para hacer la comprobacion del Pass
                ZMD5 md5 = new ZMD5();
                //Tambien llamamos a la clase ZDatosTemporales para guardar los datos recibidos
                ZDatosTemporales datosUsuario = (ZDatosTemporales) getApplicationContext();
                if (md5.comprobarMD5(pass, respuestaJSON.getString("pass"))) {
                    Log.e("PASS", "Correcto");
                    //Al ser correcto el pass, metemos los datos en las variables de la clase ZDatosTemporales
                    datosUsuario.setIdUser(Integer.parseInt(respuestaJSON.getString("id")));
                    datosUsuario.setMailUser(respuestaJSON.getString("mail"));
                    datosUsuario.setNivelUser(respuestaJSON.getString("nivel"));
                    datosUsuario.setNombreUser(respuestaJSON.getString("nombre"));
                    datosUsuario.setPassUser(respuestaJSON.getString("pass"));
                    devovlerRespuesta = true;
                } else {
                    Log.e("PASS", "Incorrecto");
                    devovlerRespuesta = false;
                }
            } else {
                devovlerRespuesta = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devovlerRespuesta;
    }

    /**
     * Funcion Autologin
     * Esta funcion es mas simple ya que toma en cuenta los datos guardados en la configuracion
     */
    public Boolean autologin(String user, String pass) {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        try {
            cadena.put("tarea", "Login");//Le asignamos los datos que necesitemos
            cadena.put("datos", user);//Le asignamos los datos que necesitemos

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
                //Si es un OK, llamaremos a las CLASES ZMD5 para hacer la comprobacion del Pass
                ZMD5 md5 = new ZMD5();
                //Tambien llamamos a la clase ZDatosTemporales para guardar los datos recibidos
                ZDatosTemporales datosUsuario = (ZDatosTemporales) getApplicationContext();
                if (pass.equals(respuestaJSON.getString("pass"))) {
                    Log.e("PASS", "Correcto");
                    //Al ser correcto el pass, metemos los datos en las variables de la clase ZDatosTemporales
                    datosUsuario.setIdUser(Integer.parseInt(respuestaJSON.getString("id")));
                    datosUsuario.setMailUser(respuestaJSON.getString("mail"));
                    datosUsuario.setNivelUser(respuestaJSON.getString("nivel"));
                    datosUsuario.setNombreUser(respuestaJSON.getString("nombre"));
                    datosUsuario.setPassUser(respuestaJSON.getString("pass"));
                    devovlerRespuesta = true;
                } else {
                    Log.e("PASS", "Incorrecto");
                    devovlerRespuesta = false;
                }
            } else {
                devovlerRespuesta = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devovlerRespuesta;
    }

    /**
     * Funcion Borrar Configuracion
     */
    public boolean eliminarConfig() {
        final SharedPreferences config = getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);
        //No quiere recordar los datos
        SharedPreferences.Editor editor = config.edit();
        editor.putString("id", "");
        editor.putString("nombre", "");
        editor.putString("user", "");
        editor.putString("nivel", "");
        editor.putBoolean("checkRecordarLogin", false);
        editor.commit();
        return true;
    }


}
