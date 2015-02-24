package euromillones.ateneasystems.es.euromillones.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Personalizaciones.NumberMinMax;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */
public class FragmentNuevoResultado extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * Declaracion de Componentes
         */
        final EditText et_n1 = (EditText) getActivity().findViewById(R.id.et_n1);
        final EditText et_n2 = (EditText) getActivity().findViewById(R.id.et_n2);
        final EditText et_n3 = (EditText) getActivity().findViewById(R.id.et_n3);
        final EditText et_n4 = (EditText) getActivity().findViewById(R.id.et_n4);
        final EditText et_n5 = (EditText) getActivity().findViewById(R.id.et_n5);
        final EditText et_e1 = (EditText) getActivity().findViewById(R.id.et_e1);
        final EditText et_e2 = (EditText) getActivity().findViewById(R.id.et_e2);
        final DatePicker dp_fecha = (DatePicker) getActivity().findViewById(R.id.dp_fecha);
        Button btn_guardar_nuevo_resultado = (Button) getActivity().findViewById(R.id.btn_guardar_nuevo_resultado);

        /**
         * Aplicamos filtros en los editText (usando la clase NumberMinMax que hemos creado)
         */
        et_n1.setFilters(new InputFilter[]{new NumberMinMax("1", "50")});
        et_n2.setFilters(new InputFilter[]{new NumberMinMax("1", "50")});
        et_n3.setFilters(new InputFilter[]{new NumberMinMax("1", "50")});
        et_n4.setFilters(new InputFilter[]{new NumberMinMax("1", "50")});
        et_n5.setFilters(new InputFilter[]{new NumberMinMax("1", "50")});
        et_e1.setFilters(new InputFilter[]{new NumberMinMax("1", "11")});
        et_e2.setFilters(new InputFilter[]{new NumberMinMax("1", "11")});

        /**
         * Funcion al pulsar el boton de guardar
         */
        btn_guardar_nuevo_resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variable para seguir
                Boolean seguir = false;
                //Comprobar si hay algun campo vacio
                if ((String.valueOf(et_n1.getText()).equals("")) || (String.valueOf(et_n2.getText()).equals("")) || (String.valueOf(et_n3.getText()).equals("")) || (String.valueOf(et_n4.getText()).equals("")) || (String.valueOf(et_n5.getText()).equals("")) || (String.valueOf(et_e1.getText()).equals("")) || (String.valueOf(et_e2.getText()).equals(""))) {
                    Toast.makeText(getActivity(), "Falta rellenar Numeros", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("PASO", "1");
                    /**
                     * Declaracion de Variables normales
                     */
                    JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
                    String respuesta = new String();//Respuesta en plano
                    ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
                    JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
                    String cadenaJSONDatos = new String();//Donde se genera el JSON a enviar
                    String fechaCompleta = new String();//Al mes se le tiene que sumar un 1 porque empieza por 0
                    /**
                     * Funcion para cargar el contenido
                     */
                    Log.e("PASO", "2");
                    fechaCompleta = String.valueOf(dp_fecha.getDayOfMonth()) + "-" + String.valueOf(dp_fecha.getMonth() + 1) + "-" + String.valueOf(dp_fecha.getYear());
                    cadenaJSONDatos = "{\"fecha\":\"" + fechaCompleta + "\",\"n1\":\"" + String.valueOf(et_n1.getText()) + "\",\"n2\":\"" + String.valueOf(et_n2.getText()) + "\",\"n3\":\"" + String.valueOf(et_n3.getText()) + "\",\"n4\":\"" + String.valueOf(et_n4.getText()) + "\",\"n5\":\"" + String.valueOf(et_n5.getText()) + "\",\"e1\":\"" + String.valueOf(et_e1.getText()) + "\",\"e2\":\"" + String.valueOf(et_e2.getText()) + "\"}";
                    try {
                        Log.e("PASO", "3");
                        cadena.put("tarea", "Guardar Resultado");//Le asignamos los datos que necesitemos
                        cadena.put("datos", cadenaJSONDatos);//Le asignamos los datos que necesitemos

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("PASO", "4");
                    cadena.toString(); //Para obtener la cadena de texto de tipo JSON
                    // ENVIAMOS CONSULTA
                    // Enviamos la consulta y cargamos los datos en los array
                    Log.e("PASO", "5");
                    respuestaJSON = conectBD.consultaSQLJSON(cadena);
                    Log.e("PASO", "6");
                    //Log.e("RESPUESTAJSON", String.valueOf(conectBD.consultaSQLJSON(cadena)));
                    try {
                        Log.e("PASO", "7");
                        //Ahora extraemos del JSON la parte "Respuesta" para saber si es un OK o un Error
                        respuesta = respuestaJSON.getString("Respuesta");
                        if (respuesta.equals("OK")) {
                            Toast.makeText(getActivity(), "Registro Guardado Correctamente!", Toast.LENGTH_LONG).show();
                            Log.e("Respuesta:", "True");
                            Log.e("PASO", "8");
                            //Vaciamos los campos
                            et_e1.setText("");
                            et_e2.setText("");
                            et_n1.setText("");
                            et_n2.setText("");
                            et_n3.setText("");
                            et_n4.setText("");
                            et_n5.setText("");

                        } else {
                            Toast.makeText(getActivity(), respuesta, Toast.LENGTH_LONG).show();
                            Log.e("Respuesta:", "False");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("PASO", "9");
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nuevo_resultado, container, false);
        return rootView;
    }
}