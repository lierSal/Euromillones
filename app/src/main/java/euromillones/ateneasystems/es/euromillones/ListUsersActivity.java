package euromillones.ateneasystems.es.euromillones;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.ListViewPersonalizado.ZSorteosAdapter;
import euromillones.ateneasystems.es.euromillones.ListViewPersonalizado.ZSorteosDatos;
import euromillones.ateneasystems.es.euromillones.ListViewPersonalizado.ZUsuariosAdapter;
import euromillones.ateneasystems.es.euromillones.ListViewPersonalizado.ZUsuariosDatos;


public class ListUsersActivity extends AppCompatActivity {

    private ArrayList<ZUsuariosDatos> listaUsuarios = new ArrayList<ZUsuariosDatos>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        /**
         * Funciones de arranque
         */
        CargandoElementosSegundoPlano cargarTarjetas = new CargandoElementosSegundoPlano();
        cargarTarjetas.execute(getIntent().getStringExtra("Nivel"));
        //Mostramos las tarjetas
        cargarTarjetas();//Por algun motivo si elimino esta funcion (que tambien la llamo en el asyctask, da error.
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    /**
     * Funcion para cargar los datos en el array y mostrarlos en pantalla.
     */

    public void cargarTarjetas() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        /*FloatingActionButton botonFloat = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        botonFloat.attachToListView(recyclerView);*/

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new ZUsuariosAdapter(listaUsuarios, R.layout.cardview_item_users));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Por si quieren configurar algom como Grilla solo cambian la linea de arriba por esta:
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    /**
     * Funcion para cargar los datos de la web en el array
     */
    public void contenido(String cantidad) {
        JSONArray respuestaJSON = new JSONArray(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        /**
         * Funcion para cargar el contenido
         */
        try {
            cadena.put("tarea", "Traer Usuarios");//Le asignamos los datos que necesitemos
            cadena.put("datos", cantidad);//Le asignamos los datos que necesitemos

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON
        // ENVIAMOS CONSULTA
        // Enviamos la consulta y cargamos los datos en los array
        respuestaJSON = conectBD.consultaSQLARRAY(cadena);
        //Log.e("RESPUESTAJSON", String.valueOf(respuestaJSON));
        try {

            //Hacemos un for para añadir datos
            for (int i = 0; i < respuestaJSON.length(); i++) {
                JSONObject jsonObject = respuestaJSON.getJSONObject(i);
                //Aqui sacaremos los datos del Array en modo (Clave Valor) las Claves son los nombres pasados en la
                //Cadena JSON
                // textoFormateado = "";
                ZUsuariosDatos dato = new ZUsuariosDatos();
                dato.setId(i);
                dato.setNombreUser(jsonObject.getString("Nombre"));
                dato.setEmailUser(jsonObject.getString("Mail"));
                listaUsuarios.add(dato);
                /*numeroSorteo[i] = jsonObject.getString("numero");
                fechaSorteo[i] = jsonObject.getString("fecha");*/
                //Log.e("NUMERO HIJO", String.valueOf(i) + " de " + String.valueOf(respuestaJSON.length()));
                //Log.e("NUMERO NOMBRE", jsonObject.getString("nombre"));
                //Log.e("NUMERO MAIL", jsonObject.getString("mail"));

            }
            ;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ultimos_resultados, container, false);
        return rootView;
    }*/
    /**
     * Asintask
     * Para cargar el contenido del servidor mientras se carga la interfaz en primer plano
     */
    private class CargandoElementosSegundoPlano extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            ArrayList<String> respuesta = new ArrayList<String>();
            contenido(params[0]);
            return true;
        }

        ;


        /**
         * Se ejecuta después de terminar "doInBackground".
         * <p/>
         * Se ejecuta en el hilo: PRINCIPAL
         * <p/>
         * //@param String con los valores pasados por el return de "doInBackground".
         */
        @Override
        protected void onPostExecute(Boolean confirmacion) {
            if (confirmacion) {
                cargarTarjetas();
            }

        }

    }
}
