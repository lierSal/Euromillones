package euromillones.ateneasystems.es.euromillones.Fragments;

import android.content.Context;
import android.content.Intent;
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
import euromillones.ateneasystems.es.euromillones.ListUsersActivity;
import euromillones.ateneasystems.es.euromillones.MainActivity;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */
public class FragmentAdminUsuarios extends Fragment {
    /**
     * Variables para el codigo entero
     */
    private Intent actividadListUsers;
    private TextView tv_nusuarios;
    private TextView tv_ncolaboradores;
    private TextView tv_nadministradores;
    private Button btn_musuarios;
    private Button btn_mcolaboradores;
    private Button btn_madministradores;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la Vista que se debe mostrar en pantalla.
        View rootView = inflater.inflate(R.layout.fragment_admin_usuarios, container,
                false);
        /**
         * Declaración de componentes en Fragment
         */
        tv_nusuarios = (TextView)rootView.findViewById(R.id.tv_nusuarios);
        tv_ncolaboradores = (TextView)rootView.findViewById(R.id.tv_ncolaboradores);
        tv_nadministradores = (TextView)rootView.findViewById(R.id.tv_nadministradores);
        btn_musuarios = (Button)rootView.findViewById(R.id.btn_musuarios);
        btn_mcolaboradores = (Button)rootView.findViewById(R.id.btn_mcolaboradores);
        btn_madministradores = (Button)rootView.findViewById(R.id.btn_madministradores);

        /**
         * Declaracion de Variables
         */
        actividadListUsers = new Intent(this.getActivity(), ListUsersActivity.class);

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
        btn_musuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarListaActivity("1");
            }
        });
        btn_mcolaboradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarListaActivity("2");
            }
        });
        btn_madministradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarListaActivity("3");
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
    //Cargar Lista de usuarios
    public void cargarListaActivity(String nivel){
        actividadListUsers.putExtra("Nivel",nivel);
        //Abrir siguiente activity
        startActivity(actividadListUsers);
    }
    //Cargar Numero
    public String cargarNumero(String grupo) {
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Donde ira el numero de respuesta
        try {
            cadena.put("tarea", "Numero Usuarios");//Le asignamos los datos que necesitemos
            cadena.put("datos", grupo);//Le asignamos los datos que necesitemos

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
            respuesta = respuestaJSON.getString("cantidad");
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

                respuesta.add(cargarNumero("1"));
                respuesta.add(cargarNumero("2"));
                respuesta.add(cargarNumero("3"));

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
        protected void onPostExecute(ArrayList<String> cantidad) {

                tv_ncolaboradores.setText(cantidad.get(1));
                tv_nadministradores.setText(cantidad.get(2));
                tv_nusuarios.setText(cantidad.get(0));

        }

    }
}