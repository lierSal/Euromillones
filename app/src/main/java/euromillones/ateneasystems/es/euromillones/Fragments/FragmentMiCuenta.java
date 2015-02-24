package euromillones.ateneasystems.es.euromillones.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import euromillones.ateneasystems.es.euromillones.CambioPassActivity;
import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */
public class FragmentMiCuenta extends Fragment {

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
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        ZDatosTemporales temporales = (ZDatosTemporales) getActivity().getApplicationContext(); //Para los datos temporales
        /**
         * Declaracion de
         */
        /**
         * Declaración de componentes en Fragment
         */
        final EditText et_mail = (EditText) getActivity().findViewById(R.id.et_mail);
        final EditText et_nombre = (EditText) getActivity().findViewById(R.id.et_nombre);
        final EditText et_apellidos = (EditText) getActivity().findViewById(R.id.et_apellidos);
        final EditText et_telefono = (EditText) getActivity().findViewById(R.id.et_telefono);
        final TextView tv_cambiarPass = (TextView) getActivity().findViewById(R.id.tv_cambiarPass);
        final Button btn_Guardar = (Button) getActivity().findViewById(R.id.btn_Guardar);
        /**
         * Funciones de los botones
         */
        btn_Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean temp;
                temp = cambiarDatos(et_mail.getText(), et_nombre.getText(), et_apellidos.getText(), et_telefono.getText());
                if (temp) {
                    Toast.makeText(getActivity(), "Datos Actualizados Correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Error al Actualizar los Datos", Toast.LENGTH_LONG).show();
                }
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
        Log.e("DATOS RECIBIDOS:", respuestaJSON.toString());
        try {
            //Cargamos los datos en los campos de texto
            et_mail.setText(respuestaJSON.getString("mail"));
            et_nombre.setText(respuestaJSON.getString("nombre"));
            et_apellidos.setText(respuestaJSON.getString("apellidos"));
            et_telefono.setText(respuestaJSON.getString("tel"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Funciones
     *
     * @param mail
     * @param nombre
     * @param apellidos
     * @param telefono
     */

    public Boolean cambiarDatos(Editable mail, Editable nombre, Editable apellidos, Editable telefono) {
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
}