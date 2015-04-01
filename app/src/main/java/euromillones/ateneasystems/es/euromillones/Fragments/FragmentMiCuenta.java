package euromillones.ateneasystems.es.euromillones.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import euromillones.ateneasystems.es.euromillones.CambioPassActivity;
import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */
public class FragmentMiCuenta extends Fragment {
    /**
     * Componentes
     */
    private EditText et_mail;
    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_telefono;
    private TextView tv_cambiarPass;
    private Button btn_Guardar;
    private ProgressBar pb_cargando;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mi_cuenta, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * Declaracion de Variables normales
         */

        /**
         * Declaracion de
         */
        /**
         * Declaración de componentes en Fragment
         */
        et_mail = (EditText) getActivity().findViewById(R.id.et_mail);
        et_nombre = (EditText) getActivity().findViewById(R.id.et_nombre);
        et_apellidos = (EditText) getActivity().findViewById(R.id.et_apellidos);
        et_telefono = (EditText) getActivity().findViewById(R.id.et_telefono);
        tv_cambiarPass = (TextView) getActivity().findViewById(R.id.tv_cambiarPass);
        btn_Guardar = (Button) getActivity().findViewById(R.id.btn_Guardar);
        pb_cargando = (ProgressBar) getActivity().findViewById(R.id.pb_cargando);
        /**
         * Funciones de los botones
         */
        btn_Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> datos = new ArrayList<String>();
                datos.add("Guardar");
                datos.add(String.valueOf(et_mail.getText()));
                datos.add(String.valueOf(et_nombre.getText()));
                datos.add(String.valueOf(et_apellidos.getText()));
                datos.add(String.valueOf(et_telefono.getText()));
                InformacionUser datosUser = new InformacionUser();
                datosUser.execute(datos);
            }
        });
        tv_cambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cargar activity de Cambiar Pass
                //Toast.makeText(this, "Cambiar Pass", Toast.LENGTH_SHORT).show();
                Log.e("Pulsado", "Cambiar Contraseña");
                Intent actividadCambioPass = new Intent(getActivity(), CambioPassActivity.class);//Esto lo ponemos aqui porque dentro del boton no funciona
                startActivity(actividadCambioPass);
            }
        });
        /**
         * Cargar los datos desde la base de datos
         */
        ArrayList<String> datos = new ArrayList<String>();
        datos.add("Cargar");
        InformacionUser datosUser = new InformacionUser();
        datosUser.execute(datos);

    }

    /**
     * Cargar los datos del usuario
     */
    public JSONObject irABuscarDatos() {
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        ZDatosTemporales temporales = (ZDatosTemporales) getActivity().getApplicationContext(); //Para los datos temporales
        try {
            cadena.put("tarea", "Cargar Usuario");//Le asignamos los datos que necesitemos
            cadena.put("datos", temporales.getMailUser());//Le asignamos los datos que necesitemos


        } catch (JSONException e) {
            e.printStackTrace();
        }
        cadena.toString(); //Para obtener la cadena de texto de tipo JSON
        //Log.e("Envio",cadena.toString());
        /**
         * ENVIAMOS CONSULTA
         */
        // Enviamos la consulta y metemos lo recibido dentro de la variable respuesta
        respuestaJSON = conectBD.consultaSQLJSON(cadena);

        return respuestaJSON;
    }

    /**
     * Funciones
     *  @param mail
     * @param nombre
     * @param apellidos
     * @param telefono
     */

    public Boolean cambiarDatos(String mail, String nombre, String apellidos, String telefono) {
        //Log.e("Datos:", String.valueOf(mail));
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        String cadenaJSONDatos = new String();//Esto es para pasarle varias variables en un texto plano
        cadenaJSONDatos = "{\"mail\":\"" + mail + "\",\"nombre\":\"" + nombre + "\",\"apellidos\":\"" + apellidos + "\",\"tel\":\"" + telefono + "\"}";
        try {
            cadena.put("tarea", "Modificar Datos");//Le asignamos los datos que necesitemos
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
        //Log.e("DATOS RECIBIDOS:", respuestaJSON.toString());
        try {
            //Ahora extraemos del JSON la parte "Respuesta" para saber si es un OK o un Error
            respuesta = respuestaJSON.getString("Respuesta");
            if (respuesta.equals("OK")) {
                devovlerRespuesta = true;
            } else {
                //Log.e("PASS", "Incorrecto");
                devovlerRespuesta = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devovlerRespuesta;
    }

    /**
     * Funcion para mostrat Toast
     */
    public void miToast(String mensaje) {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();
    }


    /**
     * Asintask para guardar informacion del usuario o cargarla
     */
    private class InformacionUser extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(ArrayList... params) {
            //Lo siguiente hay que eliminarlo ya que lo uso para probar el cargador
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Aqui llamamos a la funcion, como el array viene en params[0] tenemos que sacar
            //los diferentes campos del array con .get(x)
            //esta funcion devuelve otro un ArrayList
            Boolean resultado;//Para hacer una comprobacion
            JSONObject datosUser;
            ArrayList<String> respuesta = new ArrayList<String>();//para mandar al ultimo paso
            //If para las funciones
            if (String.valueOf(params[0].get(0)).equals("Guardar")) {
                respuesta.add("Guardar");
                resultado = cambiarDatos(String.valueOf(params[0].get(1)), String.valueOf(params[0].get(2)), String.valueOf(params[0].get(3)), String.valueOf(params[0].get(4)));
                if (resultado) {
                    respuesta.add("Datos Actualizados Correctamente");
                } else {
                    respuesta.add("Error al Actualizar los Datos");
                }

            } else if (String.valueOf(params[0].get(0)).equals("Cargar")) {
                respuesta.add("Cargar");
                datosUser = irABuscarDatos();
                try {
                    //Cargamos los datos en los campos de texto
                    respuesta.add(datosUser.getString("mail"));
                    respuesta.add(datosUser.getString("nombre"));
                    respuesta.add(datosUser.getString("apellidos"));
                    respuesta.add(datosUser.getString("tel"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return respuesta;
        }

        ;

        /**
         * Se ejecuta antes de empezar la conexion con la base de datos
         */
        protected void onPreExecute() {
            btn_Guardar.setVisibility(View.GONE);
            tv_cambiarPass.setVisibility(View.GONE);
            pb_cargando.setVisibility(View.VISIBLE);
        }

        /**
         * Se ejecuta después de terminar "doInBackground".
         * <p/>
         * Se ejecuta en el hilo: PRINCIPAL
         * <p/>
         * //@param String con los valores pasados por el return de "doInBackground".
         */

        @Override
        protected void onPostExecute(ArrayList<String> respuesta) {
            if (respuesta.get(0).equals("Guardar")) {
                miToast(respuesta.get(1));
                btn_Guardar.setVisibility(View.VISIBLE);
                tv_cambiarPass.setVisibility(View.VISIBLE);
                pb_cargando.setVisibility(View.GONE);
            } else if (respuesta.get(0).equals("Cargar")) {
                et_mail.setText(respuesta.get(1));
                et_nombre.setText(respuesta.get(2));
                et_apellidos.setText(respuesta.get(3));
                et_telefono.setText(respuesta.get(4));
                btn_Guardar.setVisibility(View.VISIBLE);
                tv_cambiarPass.setVisibility(View.VISIBLE);
                pb_cargando.setVisibility(View.GONE);
            }
        }

    }
}