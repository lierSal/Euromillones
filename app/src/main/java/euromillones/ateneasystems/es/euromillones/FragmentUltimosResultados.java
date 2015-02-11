package euromillones.ateneasystems.es.euromillones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by cubel on 11/02/15.
 */
public class FragmentUltimosResultados extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la Vista que se debe mostrar en pantalla.
        View rootView = inflater.inflate(R.layout.fragment_ultimos_resultados, container,
                false);
        /**
         * Declaracion de Variables y Objetos para el array de sorteos
         */
        String[] numeroSorteo;
        String[] fechaSorteo;
        ListView lv_sorteos = (ListView) rootView.findViewById(R.id.lv_sorteos);
        ArrayList<ZSorteosDatos> listaSorteos = new ArrayList<ZSorteosDatos>();
        ZSorteosAdapter adapter;//Adapter personalizado

        /**
         * Declaracion de Variables normales
         */
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON

        /**
         * Funcion para cargar el contenido
         */
        try {
            cadena.put("tarea", "Ultimos Sorteos");//Le asignamos los datos que necesitemos
            cadena.put("datos", "5");//Le asignamos los datos que necesitemos

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON
        // ENVIAMOS CONSULTA
        // Enviamos la consulta y cargamos los datos en los array
        respuestaJSON = conectBD.consultaSQL(cadena);
        try {
            //Inflamos el array con la cantidad de datos del JSON
            numeroSorteo = new String[respuestaJSON.length()];
            fechaSorteo = new String[respuestaJSON.length()];
            //Hacemos un for para añadir datos
            for (int i = 0; i < respuestaJSON.length(); i++) {
                JSONObject jsonObject = respuestaJSON.getJSONObject(String.valueOf(i));
                //Aqui sacaremos los datos del Array en modo (Clave Valor) las Claves son los nombres pasados en la
                //Cadena JSON
                // textoFormateado = "";
                numeroSorteo[i] = jsonObject.getString("numero");
                fechaSorteo[i] = jsonObject.getString("fecha");
            }
            ;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Al adapter personalizado le pasamos el contexto y la lista que contiene
        // Añadimos el adapter al listview

       /*adapter = new ZSorteosAdapter(this, listaSorteos);
        lv_sorteos.setAdapter(adapter);*/

        // Devolvemos la vista para que se muestre en pantalla.
        return rootView;
    }

}
