package euromillones.ateneasystems.es.euromillones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.Clases.ZMD5;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentAbout;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentAdminUsuarios;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentMiCuenta;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentNuevoResultado;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentPredicciones;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentUltimosResultados;

//Importamos la libreria de google

public class PrivateActivity extends ActionBarActivity {

    private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence tituloSeccion;
    private CharSequence tituloApp;

    /**
     * GCM
     * Esto es para que podamos recibir notificaciones Push en la app
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";

    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 21;//El ultimo numero son los dias

    String SENDER_ID = "230391418531";

    static final String TAG = "GCMDemo";

    private Context context;
    private String regid;
    private GoogleCloudMessaging gcm;
    private String mailUserGCM;
    private String idUserGCM;
    // FIN GCM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);
        /**
         * Para versiones de Android superiores a la 2.3.7 necesitamos agregar estas lineas
         * asi funcionara cualquier conexion exterior
         */
        if (android.os.Build.VERSION.SDK_INT > 10) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /**
         * Declaracion de Objetos
         */
        final TextView tv_version = (TextView) findViewById(R.id.tv_version);
        final TextView tv_web = (TextView) findViewById(R.id.tv_web);
        /**
         * Variables
         */
        //int versionCode = BuildConfig.VERSION_CODE; //Codigo de Version de Android Studio
        String versionName = "V" + BuildConfig.VERSION_NAME; //Version de Play Store
        //Tambien llamamos a la clase ZDatosTemporales para guardar los datos recibidos
        ZDatosTemporales datosUsuario = (ZDatosTemporales) getApplicationContext();
        String nivelUser = datosUsuario.getNivelUser();
        String mailUser = datosUsuario.getMailUser();
        final String AndroidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);//Muestra el ID del Dispositivo
        Log.e("NivelUSer", datosUsuario.getMailUser());
        /**
         * Primero cargamos la informacion del archivo de configuracion
         */
        final SharedPreferences config = getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);
        final SharedPreferences configPush = getSharedPreferences("configPush", Context.MODE_PRIVATE);

        /**
         * Otras Funciones
         */
        /**
         * Comprobamos que tenga Google Play Service instalado
         */
        mailUserGCM = datosUsuario.getMailUser();
        context = getApplicationContext();
        //Chequemos si está instalado Google Play Services
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(PrivateActivity.this);

            //Obtenemos el Registration ID guardado
            regid = getRegistrationId(context);

            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                TareaRegistroGCM tarea = new TareaRegistroGCM();
                tarea.execute(AndroidId);
            }
        } else {
            Log.i(TAG, "No se ha encontrado Google Play Services.");
        }
        //FIN Comprobacion
        //Añadir o Actualziar los ID Push
        //registroPush(mailUser,AndroidId,configPush.getString("registration_id","0"));
        //registroPush(mailUser,AndroidId,"HOLA");


        //Poner la version de Android en TextView
        tv_version.setText(versionName);
        //Poner link en TextView
        tv_web.setText(Html.fromHtml("<a href=\"http://www.ateneasystems.es\">" + getResources().getString(R.string.text_Web_Empresa) + "</a> "));
        tv_web.setMovementMethod(LinkMovementMethod.getInstance());
        //tv_web.setTextColor(getResources().getColor(R.color.Negro_Puro));//Cambiar color

        //COMO ESTABA
        //opcionesMenu = new String[] {"Opción 1", "Opción 2", "Opción 3", "Login"};
        /**
         * Cargamos el menu dependiendo del nivel de usuario
         */
        //Primero comprobamos que tenga nivel, si no tiene cerraremos el activity y lo mandamos al login
        /*if (nivelUser.equals("")){
            final Intent actividadLogin = new Intent(this, Login_Activity.class);
            //Abrir siguiente activity
            startActivity(actividadLogin);
            //Eliminamos este activity
            finish();
        }*/
        switch (nivelUser) {
            case "1":
                opcionesMenu = new String[]{getResources().getString(R.string.bl_Ultimos_Resultados), getResources().getString(R.string.bl_Predicciones), getResources().getString(R.string.bl_Mi_Cuenta)};
                break;
            case "2":
                opcionesMenu = new String[]{getResources().getString(R.string.bl_Ultimos_Resultados), getResources().getString(R.string.bl_Predicciones), getResources().getString(R.string.bl_Mi_Cuenta), getResources().getString(R.string.bl_Nuevo_Resultado)};
                break;
            case "3":
                opcionesMenu = new String[]{getResources().getString(R.string.bl_Ultimos_Resultados), getResources().getString(R.string.bl_Predicciones), getResources().getString(R.string.bl_Mi_Cuenta), getResources().getString(R.string.bl_Nuevo_Resultado), getResources().getString(R.string.bl_Admin_Usuarios)};
                break;
        }
            /*
        if (nivelUser.equals("1")) {
            opcionesMenu = new String[]{getResources().getString(R.string.bl_Ultimos_Resultados), getResources().getString(R.string.bl_Predicciones), getResources().getString(R.string.bl_Mi_Cuenta)};
        } else if (nivelUser.equals("2")) {
            opcionesMenu = new String[]{getResources().getString(R.string.bl_Ultimos_Resultados), getResources().getString(R.string.bl_Predicciones), getResources().getString(R.string.bl_Mi_Cuenta), getResources().getString(R.string.bl_Nuevo_Resultado)};
        } else if (nivelUser.equals("3")) {
            opcionesMenu = new String[]{getResources().getString(R.string.bl_Ultimos_Resultados), getResources().getString(R.string.bl_Predicciones), getResources().getString(R.string.bl_Mi_Cuenta), getResources().getString(R.string.bl_Nuevo_Resultado), getResources().getString(R.string.bl_Admin_Usuarios)};
        }
        ;*/


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(
                getSupportActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1, opcionesMenu));

        drawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Fragment fragment = null;

                switch (position) {
                    case 0:
                        fragment = new FragmentUltimosResultados();
                        break;
                    case 1:
                        fragment = new FragmentPredicciones();
                        break;
                    case 2:
                        fragment = new FragmentMiCuenta();
                        break;
                    case 3:
                        fragment = new FragmentNuevoResultado();
                        break;
                    case 4:
                        fragment = new FragmentAdminUsuarios();
                        break;
                }

                FragmentManager fragmentManager =
                        getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();

                drawerList.setItemChecked(position, true);

                tituloSeccion = opcionesMenu[position];
                getSupportActionBar().setTitle(tituloSeccion);

                drawerLayout.closeDrawer(drawerList);
            }
        });

        tituloSeccion = getTitle();
        tituloApp = getTitle();

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.drawable.ic_action_navigation,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(tituloSeccion);
                ActivityCompat.invalidateOptionsMenu(PrivateActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(tituloApp);
                ActivityCompat.invalidateOptionsMenu(PrivateActivity.this);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Funciones
     */
    public void borrarPreferencias() {
        /**
         * Primero cargamos la informacion del archivo de configuracion
         */
        final SharedPreferences config = getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = config.edit();
        ZDatosTemporales DU = (ZDatosTemporales) getApplicationContext();
        editor.putString("id", "");
        editor.putString("nombre", "");
        editor.putString("user", "");
        editor.putString("nivel", "");
        editor.putString("passCod", "");
        editor.putBoolean("checkRecordarLogin", false);
        editor.commit();
    }

    public void registroPush(String mail, String idDispositivo, String idGCM) {
        /**
         * Esta funcion sirve para registrar el dispositivo push en nuestra base de datos
         * en caso de existir solo lo actualiza pero nosotoros no tenemos nada que hacer,
         * eso se encargara la api del servidor
         */
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        String cadenaJSONDatos = new String();//Esto es para pasarle varias variables en un texto plano
        String passGenerado = new String();//Aqui ira el pass completo
        ZMD5 md5 = new ZMD5(); //creamos la variable md5 que se usara para hacer lo necesario para el PASS
        //passGenerado = md5.generarMD5(pass);//Le mandamos la contraseña y el se encarga de generar el salt y md5
        String tipoPush = "Nuevos Sorteos";
        Log.e("Mail", mail);
        //Log.e("Nombre", nombre);
        //Log.e("Pass", pass);
        Log.e("PassMD5", passGenerado);

        cadenaJSONDatos = "{\"idUser\":\"" + mail + "\",\"idDispositivo\":\"" + idDispositivo + "\",\"idPush\":\"" + idGCM + "\",\"tipoPush\":\"" + tipoPush + "\"}";
        Log.e("JSON", cadenaJSONDatos);


        try {
            cadena.put("tarea", "Registro Push");//Le asignamos los datos que necesitemos
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
        Log.e("DATOS RECIBIDOS:", respuestaJSON.toString());
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


    }

    /**
     * MENUS
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_private, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_cerrar_session:
                //Eliminamos las varialbes de configuracion
                borrarPreferencias();
                Toast.makeText(this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                finish();//Cierra el activity
                break;
            case R.id.action_settings:
                //Toast.makeText(this, "Abriendo Configuracion APP", Toast.LENGTH_SHORT).show();
                final Intent actividadOpciones = new Intent(this, OpcionesActivity.class);
                startActivity(actividadOpciones);
                break;
            case R.id.action_about:
                Fragment fragment = null;
                fragment = new FragmentAbout();
                Toast.makeText(this, "Abriendo Sobre APP", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager =
                        getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                getSupportActionBar().setTitle(R.string.title_fragment_about);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean menuAbierto = drawerLayout.isDrawerOpen(drawerList);

        if (menuAbierto)
            menu.findItem(R.id.action_cerrar_session).setVisible(false);
        else
            menu.findItem(R.id.action_cerrar_session).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Otras Funciones
     */
    /**
     * GCM
     */
    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "Dispositivo no soportado.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        SharedPreferences prefs = getSharedPreferences(
                "configPush",
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.length() == 0) {
            Log.d(TAG, "Registro GCM no encontrado.");
            return "";
        }

        String registeredUser =
                prefs.getString(PROPERTY_USER, "user");

        int registeredVersion =
                prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");

        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        } else if (System.currentTimeMillis() > expirationTime) {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        } else if (!mailUserGCM.equals(registeredUser)) {
            Log.d(TAG, "Nuevo nombre de usuario.");
            return "";
        }

        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    private class TareaRegistroGCM extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String msg = "";

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);

                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);

                //Nos registramos en nuestro servidor
                boolean registrado = registroServidor(mailUserGCM, params[0], regid);

                //Guardamos los datos del registro
                if (registrado) {
                    setRegistrationId(context, params[0], regid);
                }
            } catch (IOException ex) {
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
        }
    }

    private void setRegistrationId(Context context, String user, String regId) {
        SharedPreferences prefs = getSharedPreferences(
                "configPush",
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER, user);
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putLong(PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);

        editor.commit();
    }

    private boolean registroServidor(String mail, String idDispositivo, String regId) {
        /**
         * Esta funcion sirve para registrar el dispositivo push en nuestra base de datos
         * en caso de existir solo lo actualiza pero nosotoros no tenemos nada que hacer,
         * eso se encargara la api del servidor
         */
        //Declaramos Variables
        Boolean res = false;
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        String cadenaJSONDatos = new String();//Esto es para pasarle varias variables en un texto plano
        String passGenerado = new String();//Aqui ira el pass completo
        ZMD5 md5 = new ZMD5(); //creamos la variable md5 que se usara para hacer lo necesario para el PASS
        //passGenerado = md5.generarMD5(pass);//Le mandamos la contraseña y el se encarga de generar el salt y md5
        String tipoPush = "Nuevos Sorteos";
        Log.e("Mail", mail);
        //Log.e("Nombre", nombre);
        //Log.e("Pass", pass);
        Log.e("PassMD5", passGenerado);

        cadenaJSONDatos = "{\"idUser\":\"" + mail + "\",\"idDispositivo\":\"" + idDispositivo + "\",\"idPush\":\"" + regId + "\",\"tipoPush\":\"" + tipoPush + "\"}";
        Log.e("JSON", cadenaJSONDatos);


        try {
            cadena.put("tarea", "Registro Push");//Le asignamos los datos que necesitemos
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
        Log.e("DATOS RECIBIDOS:", respuestaJSON.toString());
        try {
            //Ahora extraemos del JSON la parte "Respuesta" para saber si es un OK o un Error
            respuesta = respuestaJSON.getString("Respuesta");
            if (respuesta.equals("OK")) {
                Log.e("Entra en Devolver:", "True");
                res = true;

            } else {
                Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
                res = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

}