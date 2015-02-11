package euromillones.ateneasystems.es.euromillones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cubel on 5/02/15.
 */

public class FragmentPredicciones extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la Vista que se debe mostrar en pantalla.
        View rootView = inflater.inflate(R.layout.fragment_predicciones, container,
                false);
        /**
         * Declaración de componentes en Fragment
         */
        final TextView tv_nmasRepetido = (TextView) rootView.findViewById(R.id.tv_nmasRepetido);
        final TextView tv_nmasRepetidoMes = (TextView) rootView.findViewById(R.id.tv_nmasRepetidoMes);
        final TextView tv_nSemiAleatorio = (TextView) rootView.findViewById(R.id.tv_nSemiAleatorio);
        Button btn_BuscarOtroNumero = (Button) rootView.findViewById(R.id.btn_BuscarOtroNumero);
        /**
         * Funciones de arranque
         */
        tv_nmasRepetido.setText(cargarMasRepetidos());
        tv_nmasRepetidoMes.setText(cargarMasRepetidosMes());
        tv_nSemiAleatorio.setText(cargarSemiAleatorios());
        /**
         * Funcion botones
         */
        btn_BuscarOtroNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recargarmos el SemiAleatorio
                tv_nSemiAleatorio.setText(cargarSemiAleatorios());
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
        respuestaJSON = conectBD.consultaSQL(cadena);
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
        respuestaJSON = conectBD.consultaSQL(cadena);
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
            cadena.put("datos", "");//Le asignamos los datos que necesitemos

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON
        /**
         * ENVIAMOS CONSULTA
         */
        // Enviamos la consulta y metemos lo recibido dentro de la variable respuesta
        respuestaJSON = conectBD.consultaSQL(cadena);
        try {
            respuesta = respuestaJSON.getString("numero");
        } catch (JSONException e) {
            e.printStackTrace();
            respuesta = "";
        }

        return respuesta;
    }
}