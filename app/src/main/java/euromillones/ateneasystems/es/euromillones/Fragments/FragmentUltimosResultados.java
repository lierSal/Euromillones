package euromillones.ateneasystems.es.euromillones.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import euromillones.ateneasystems.es.euromillones.R;


/**
 * Created by cubel on 11/02/15.
 */
public class FragmentUltimosResultados extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


/**
 * Declaracion de Variables  y Objetos para el array de sorteos
 */

        ArrayList<ZSorteosDatos> listaSorteos = new ArrayList<ZSorteosDatos>();


        /**
         * Declaracion de Variables normales
         */
        SharedPreferences config = this.getActivity().getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);//para traer la configuracion
        ZDatosTemporales configTerminal = new ZDatosTemporales();
        JSONArray respuestaJSON = new JSONArray(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        /**
         * Funcion para cargar el contenido
         */
        try {
            cadena.put("tarea", "Ultimos Sorteos");//Le asignamos los datos que necesitemos
            cadena.put("datos", config.getString("cantidadUltimosResultados", "10"));//Le asignamos los datos que necesitemos

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON
        // ENVIAMOS CONSULTA
        // Enviamos la consulta y cargamos los datos en los array
        respuestaJSON = conectBD.consultaSQLARRAY(cadena);
        Log.e("RESPUESTAJSON", String.valueOf(conectBD.consultaSQL(cadena)));
        try {

            //Hacemos un for para añadir datos
            for (int i = 0; i < respuestaJSON.length(); i++) {
                JSONObject jsonObject = respuestaJSON.getJSONObject(i);
                //Aqui sacaremos los datos del Array en modo (Clave Valor) las Claves son los nombres pasados en la
                //Cadena JSON
                // textoFormateado = "";
                ZSorteosDatos dato = new ZSorteosDatos();
                dato.setId(i);
                dato.setFechaSorteo(jsonObject.getString("fecha"));
                dato.setNumeroSorteo(jsonObject.getString("numero"));
                listaSorteos.add(dato);
                /*numeroSorteo[i] = jsonObject.getString("numero");
                fechaSorteo[i] = jsonObject.getString("fecha");*/
                Log.e("NUMERO HIJO", String.valueOf(i));
                Log.e("NUMERO FECHA", jsonObject.getString("numero"));
                Log.e("NUMERO SORTEO", jsonObject.getString("fecha"));

            }
            ;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //ReclyclerView, Adapter

        //final ArrayList<Course> courses;
        //ReadLocalJSON readLocalJSON = new ReadLocalJSON();
        //courses = readLocalJSON.getCourses(getActivity(),respuestaJSON);
        //courses = readLocalJSON

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ZSorteosAdapter(listaSorteos, R.layout.cardview_item_sorteos));


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Por si quieren configurar algom como Grilla solo cambian la linea de arriba por esta:
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //Float Button
/*
        final int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
        final ImageButton imageButton = (ImageButton) getActivity().findViewById(R.id.fab_1);


        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Mejorando.la: Aprende a crear el futuro de la Web",
                        Toast.LENGTH_LONG).show();


            }
        });*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ultimos_resultados, container, false);
        return rootView;
    }


}