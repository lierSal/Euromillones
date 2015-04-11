package euromillones.ateneasystems.es.euromillones;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.Clases.ZMD5;


public class Login_Activity extends ActionBarActivity {
    /**
     *
     * Variables para el Lectura NFC
     */
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private Context ctx = this;//Context
    //Fin NFC
    /**
     * Variables para tod
     */
    ProgressBar pb_cargando;
    Button btn_login;
    Button btn_registro;
    SharedPreferences config;
    TextView tv_respuesta;
    String cargarInitPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cargarInitPrivate = getIntent().getStringExtra("Cargar");
        Log.d("--> RECIBIDO <--:",String.valueOf(cargarInitPrivate));
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
        final CheckBox cb_recordar = (CheckBox) findViewById(R.id.cb_recordar);
        Button btn_snPass = (Button) findViewById(R.id.btn_snPass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_registro = (Button) findViewById(R.id.btn_registro);
        pb_cargando = (ProgressBar) findViewById(R.id.pb_cargando);
        tv_respuesta = (TextView) findViewById(R.id.tv_respuesta);

        /**
         * Declaracion de variables
         */
        String passCodConfig = new String();
        String user = new String();
        final Intent actividadRegistro = new Intent(this, Registro.class);//Esto lo ponemos aqui porque dentro del boton no funciona
        final Intent cargarSolNuevoPass = new Intent(this, RestorePass.class);
        //cargarInitPrivate = getIntent().getStringExtra("Cargar");

        /**
         * Primero cargamos la informacion del archivo de configuracion
         */
        config = getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);
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
                //Abrimos private
                cargarPrivate();
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
        //Solicitar nuevo Pass
        btn_snPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarSolNuevoPass.putExtra("mail",String.valueOf(et_user.getText()));
              startActivity(cargarSolNuevoPass);
            }
        });
        //BOTON LOGIN
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Metemos los datos necesarios en el Array
                ArrayList<String> datos = new ArrayList<String>();
                datos.add("Boton Login");
                datos.add(String.valueOf(String.valueOf(et_user.getText())));
                datos.add(String.valueOf(String.valueOf(et_pass.getText())));
                datos.add(String.valueOf(cb_recordar.isChecked()));
                LoginSegundoPlano loginUser = new LoginSegundoPlano();
                loginUser.execute(datos);
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
        /**
         * cargamos el contenido en las variables NFC
         */
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) { //Para detectar si tiene NFC o NO
            //Toast.makeText(this, "Dispositivo sin NFC", Toast.LENGTH_LONG).show();
        } else {
            if (mNfcAdapter.isEnabled()) {
                if (config.getBoolean("checkRecordarLogin", false)) {
                    //Nada
                } else {
                    //No es autologin por lo tanto
                    Toast.makeText(this, "Puedes  hacer Login con el TAG NFC", Toast.LENGTH_LONG).show();
                }
            } else {
                if (config.getBoolean("checkRecordarLogin", false)) {
                    //Nada
                } else {
                    //No es autologin por lo tanto
                    Toast.makeText(this, "Activa el NFC en Opciones del Dispositivo", Toast.LENGTH_LONG).show();
                }

            }
        }
        //Fin

        // create an intent with tag data and deliver to this activity

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");

            mIntentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};

    }

    /**
     * Funcion para ir a la aplicacion
     */
    public void cargarPrivate() {
        Intent actividadPrivate = new Intent(this, PrivateActivity.class);//Esto lo ponemos aqui porque dentro del boton no funciona
        actividadPrivate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//Para no duplicar el activity.
        //Con lo siguiente comprobamos si hay datos a cargar (por una notificacion) de no haber datos lo cargara vacio
        if (cargarInitPrivate == null){
            Log.e("Entra","NULL");
            //Log.e("Entra",cargarInitPrivate);
            actividadPrivate.putExtra("Cargar", "Vacio");//aqui diremos que no queremos cargar nada
        } else {
            Log.e("Entra","NO NULL");
            Log.e("Entra",cargarInitPrivate);
            actividadPrivate.putExtra("Cargar",cargarInitPrivate);//aqui diremos que si queremos cargar algo
        }

        //Abrir siguiente activity
        startActivity(actividadPrivate);
        //Y cerramos esta
        finish();

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
                Log.e("PassTag", pass);
                Log.e("PassBDD", respuestaJSON.getString("pass"));
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


    /**
     * Asyntask Logins
     */
    private class LoginSegundoPlano extends AsyncTask<ArrayList<String>, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(ArrayList... params) {
            //Aqui llamamos a la funcion, como el array viene en params[0] tenemos que sacar
            //los diferentes campos del array con .get(x)
            //esta funcion devuelve otro array
            Boolean cargarInfo;
            Boolean respuesta = false;
            if (params[0].get(0).equals("Boton Login")) {
                respuesta = comprobarUsuario(String.valueOf(params[0].get(1)), String.valueOf(params[0].get(2)));
                cargarInfo = Boolean.valueOf(String.valueOf(params[0].get(3)));
                if (cargarInfo) {
                    //Quiere recordar los datos
                    SharedPreferences.Editor editor = config.edit();
                    ZDatosTemporales DU = (ZDatosTemporales) getApplicationContext();
                    editor.putString("id", DU.getIdUser());
                    editor.putString("nombre", DU.getNombreUser());
                    editor.putString("user", DU.getMailUser());
                    editor.putString("nivel", DU.getNivelUser());
                    editor.putString("passCod", DU.getPassUser());
                    editor.putBoolean("checkRecordarLogin", true);
                    editor.commit();
                } else {
                    //No quiere recordar los datos
                    SharedPreferences.Editor editor = config.edit();
                    editor.putString("id", "");
                    editor.putString("nombre", "");
                    editor.putString("user", "");
                    editor.putString("nivel", "");
                    editor.putBoolean("checkRecordarLogin", false);
                    editor.commit();
                }

            } else if (params[0].get(0).equals("TAG Login")) {
                respuesta = autologin(String.valueOf(params[0].get(1)), String.valueOf(params[0].get(2)));
                //respuesta = comprobarUsuario();
                //Log.e("Entra", String.valueOf(params[0].get(1)));
                //No quiere recordar los datos
                SharedPreferences.Editor editor = config.edit();
                editor.putString("id", "");
                editor.putString("nombre", "");
                editor.putString("user", "");
                editor.putString("nivel", "");
                editor.putBoolean("checkRecordarLogin", false);
                editor.commit();
            }
            return respuesta;
        }

        ;

        /**
         * Se ejecuta antes de empezar la conexion con la base de datos
         */
        protected void onPreExecute() {
            btn_registro.setVisibility(View.GONE);
            pb_cargando.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
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
            if (!respuesta) {
                btn_registro.setVisibility(View.VISIBLE);
                pb_cargando.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
                tv_respuesta.setText(R.string.tv_User_Error);
            } else {
                cargarPrivate();
            }
        }

    }

    /**
     * Funcones para detecar tag
     */
    @Override
    public void onNewIntent(Intent intent) {
        //Metemos los datos necesarios en el Array
        ArrayList<String> datosRecibidos = new ArrayList<String>();
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = action + "\n\n" + tag.toString();

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        Log.e("cantidad", String.valueOf(recs.length));//Muestra la cantidad de datos correctamente
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            /*s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
                                    new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding) +
                                    "\"");*/
                            datosRecibidos.add(new String(payload, langCodeLen + 1,
                                    payload.length - langCodeLen - 1, textEncoding));
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

        }
        //Toast.makeText(this, "APP: " + datosRecibidos.get(0), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Nombre: "+datosRecibidos.get(1), Toast.LENGTH_LONG).show();
        // Toast.makeText(this, "Pass:"+datosRecibidos.get(2), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Id: "+datosRecibidos.get(3), Toast.LENGTH_LONG).show();
        //mTextView.setText(s);
        //Log.e("Datos",  "APP: "+datosRecibidos.get(0)+" - Nombre: "+datosRecibidos.get(1)+" - Pass:"+datosRecibidos.get(2)+" - Id: "+datosRecibidos.get(3));
        /**
         * Hacemos la funcion como si del boton login se tratase para qe salga el cargador
         */
        //Metemos los datos necesarios en el Array
        ArrayList<String> datos = new ArrayList<String>();
        datos.add("TAG Login");
        datos.add(datosRecibidos.get(0));
        datos.add(datosRecibidos.get(1));
        //datos.add("false");
        LoginSegundoPlano loginUser = new LoginSegundoPlano();
        loginUser.execute(datos);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("RE ON RESUME:", String.valueOf(getIntent().getStringExtra("Cargar")));
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

}
