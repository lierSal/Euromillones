package euromillones.ateneasystems.es.euromillones.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */

public class FragmentPredicciones extends Fragment {
    /**
     * Variables para el codigo entero
     */
    private TextView tv_nmasRepetido;
    private TextView tv_nmasRepetidoMes;
    private TextView tv_nSemiAleatorio;
    private SharedPreferences config;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la Vista que se debe mostrar en pantalla.
        View rootView = inflater.inflate(R.layout.fragment_predicciones, container,
                false);
        /**
         * Declaración de componentes en Fragment
         */
        tv_nmasRepetido = (TextView) rootView.findViewById(R.id.tv_nmasRepetido);
        tv_nmasRepetidoMes = (TextView) rootView.findViewById(R.id.tv_nmasRepetidoMes);
        tv_nSemiAleatorio = (TextView) rootView.findViewById(R.id.tv_nSemiAleatorio);
        Button btn_BuscarOtroNumero = (Button) rootView.findViewById(R.id.btn_BuscarOtroNumero);
        /**
         * Declaracion de Variables
         */
        config = getActivity().getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);//Para cargar configuraciones
        /**
         * Funciones de arranque
         */
        CargandoElementosSegundoPlano tarea = new CargandoElementosSegundoPlano();
        tarea.execute("Todo");
        /*tarea.execute("Repetido Mes");
        tarea.execute("Aleatorio");*/
        /**
         * Funcion botones
         */
        btn_BuscarOtroNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargandoElementosSegundoPlano tarea = new CargandoElementosSegundoPlano();
                //Recargarmos el SemiAleatorio
                //tv_nSemiAleatorio.setText(cargarSemiAleatorios());
                tarea.execute("Aleatorio");
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
    public String cargarMasRepetidos() {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Donde ira el numero de respuesta
        try {
            cadena.put("tarea", "Numeros Completo");//Le asignamos los datos que necesitemos
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
            respuesta = respuestaJSON.getString("numero");
        } catch (JSONException e) {
            e.printStackTrace();
            respuesta = "";
        }

        return respuesta;
    }

    public String cargarMasRepetidosMes() {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Donde ira el numero de respuesta
        try {
            cadena.put("tarea", "Numeros del Mes");//Le asignamos los datos que necesitemos
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
            respuesta = respuestaJSON.getString("numero");
        } catch (JSONException e) {
            e.printStackTrace();
            respuesta = "";
        }

        return respuesta;

    }

    public String cargarSemiAleatorios() {
        //Declaramos Variables

        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Donde ira el numero de respuesta
        try {
            cadena.put("tarea", "Numeros Semi");//Le asignamos los datos que necesitemos
            cadena.put("datos", config.getInt("cantidadAleatorios", 50)).toString();//Le asignamos los datos que necesitemos

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
            respuesta = respuestaJSON.getString("numero");
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
    private class CargandoElementosSegundoPlano extends AsyncTask<String, Integer, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> respuesta = new ArrayList<String>();
            if (params[0].equals("Todo")) {
                respuesta.add("Todo");
                respuesta.add(cargarSemiAleatorios());
                respuesta.add(cargarMasRepetidos());
                respuesta.add(cargarMasRepetidosMes());


            } else if (params[0].equals("Aleatorio")) {
                respuesta.add("Aleatorio");
                respuesta.add(cargarSemiAleatorios());
            }

            return respuesta;
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
        protected void onPostExecute(ArrayList<String> numeros) {
            if (numeros.get(0).equals("Todo")) {
                tv_nmasRepetido.setText(numeros.get(2));
                tv_nmasRepetidoMes.setText(numeros.get(3));
                tv_nSemiAleatorio.setText(numeros.get(1));
            } else if (numeros.get(0).equals("Aleatorio")) {
                tv_nSemiAleatorio.setText(numeros.get(1));
            }
        }

    }
}