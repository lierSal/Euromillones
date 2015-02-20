package euromillones.ateneasystems.es.euromillones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
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

import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentAdminUsuarios;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentMiCuenta;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentNuevoResultado;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentPredicciones;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentUltimosResultados;

public class PrivateActivity extends ActionBarActivity {

    private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence tituloSeccion;
    private CharSequence tituloApp;

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
        Log.e("NivelUSer", datosUsuario.getMailUser());
        /**
         * Primero cargamos la informacion del archivo de configuracion
         */
        final SharedPreferences config = getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);

        /**
         * Otras Funciones
         */
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
                Toast.makeText(this, "Abriendo Sobre APP", Toast.LENGTH_SHORT).show();
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
}