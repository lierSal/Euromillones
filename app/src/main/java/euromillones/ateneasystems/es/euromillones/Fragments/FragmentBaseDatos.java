package euromillones.ateneasystems.es.euromillones.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.ListViewPersonalizado.ZSorteosAdapter;
import euromillones.ateneasystems.es.euromillones.ListViewPersonalizado.ZSorteosDatos;
import euromillones.ateneasystems.es.euromillones.R;


/**
 * Created by cubel on 11/02/15.
 */
public class FragmentBaseDatos extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la Vista que se debe mostrar en pantalla.
        View rootView = inflater.inflate(R.layout.fragment_basedatos, container,
                false);
        /**
         * Declaración de componentes en Fragment
         */
        Button btn_CopiaDB = (Button) rootView.findViewById(R.id.btn_CopiaDB);
        /**
         * Funcion botones
         */
        btn_CopiaDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargandoElementosSegundoPlano tarea = new CargandoElementosSegundoPlano();
                tarea.execute();

            }
        });

        /**
         * Retornamos el fragment
         */
        // Devolvemos la vista para que se muestre en pantalla.
        return rootView;


    }

    /**
     * Funciones
     */
    public String realizarCopia() {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Donde ira el numero de respuesta
        try {
            cadena.put("tarea", "Copia de Seguridad");//Le asignamos los datos que necesitemos
            cadena.put("datos", "");//Le asignamos los datos que necesitemos

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON
        /**
         * ENVIAMOS CONSULTA
         */
        // Enviamos la consulta y metemos lo recibido dentro de la variable respuesta
        respuestaJSON = conectBD.consultaSQLJSON(cadena);
        try {
            respuesta = respuestaJSON.getString("archivo");
        } catch (JSONException e) {
            e.printStackTrace();
            respuesta = "";
        }

        return respuesta;
    }




    /**
     * Asintask
     * Para cargar el contenido del servidor mientras se carga la interfaz en primer plano
     */
    private class CargandoElementosSegundoPlano extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String URL = "";
            URL = "http://www.ateneasystems.es/PW/Euromillones/appAndroid/down.php?desc="+realizarCopia();



            return URL;
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
        protected void onPostExecute(String url) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }

    }

}