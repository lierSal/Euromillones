package euromillones.ateneasystems.es.euromillones.Fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Personalizaciones.NumberMinMax;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 5/02/15.
 */
public class FragmentNuevoResultado extends Fragment {
    /**
     * Variables para los LOG
     */
    private String LOGe = "<-- RECIBIDO -->";
    /**
     * Varible para Errores de PHP
     */
    private String PHPError = "";
    /**
     * Componentes
     */
    private ProgressBar pb_cargando;
    private EditText et_n1;
    private EditText et_n2;
    private EditText et_n3;
    private EditText et_n4;
    private EditText et_n5;
    private EditText et_e1;
    private EditText et_e2;
    Button btn_guardar_nuevo_resultado;
    private EditText et_fecha;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * Declaracion de Componentes
         */
        et_n1 = (EditText) getActivity().findViewById(R.id.et_n1);
        et_n2 = (EditText) getActivity().findViewById(R.id.et_n2);
        et_n3 = (EditText) getActivity().findViewById(R.id.et_n3);
        et_n4 = (EditText) getActivity().findViewById(R.id.et_n4);
        et_n5 = (EditText) getActivity().findViewById(R.id.et_n5);
        et_e1 = (EditText) getActivity().findViewById(R.id.et_e1);
        et_e2 = (EditText) getActivity().findViewById(R.id.et_e2);
        btn_guardar_nuevo_resultado = (Button) getActivity().findViewById(R.id.btn_guardar_nuevo_resultado);
        et_fecha = (EditText) getActivity().findViewById(R.id.et_fecha);
        pb_cargando = (ProgressBar) getActivity().findViewById(R.id.pb_cargando);

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
         * Funcion al pulsar sobre el campo de fecha
         */
        et_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        et_fecha.setText(String.valueOf(selectedday + "-" + String.valueOf(selectedmonth + 1)) + "-" + String.valueOf(selectedyear));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Seleciona la Fecha");
                mDatePicker.show();
            }
        });


        /**
         * Funcion al pulsar el boton de guardar
         */
        btn_guardar_nuevo_resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobar si hay algun campo vacio
                if ((String.valueOf(et_n1.getText()).equals("")) || (String.valueOf(et_n2.getText()).equals("")) || (String.valueOf(et_n3.getText()).equals("")) || (String.valueOf(et_n4.getText()).equals("")) || (String.valueOf(et_n5.getText()).equals("")) || (String.valueOf(et_e1.getText()).equals("")) || (String.valueOf(et_e2.getText()).equals(""))) {
                    Toast.makeText(getActivity(), "Falta rellenar Numeros", Toast.LENGTH_LONG).show();
                } else if(String.valueOf(et_fecha.getText()).equals("")){
                    Toast.makeText(getActivity(), "Falta rellenar la fecha", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<String> datos = new ArrayList<String>();
                    datos.add("Cargar");
                    guardarResultado datosGuardar = new guardarResultado();
                    datosGuardar.execute(datos);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nuevo_resultado, container, false);
        return rootView;
    }
    /**
     * Asintask para guardar informacion del usuario o cargarla
     */
    private class guardarResultado extends AsyncTask<ArrayList<String>, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(ArrayList... params) {
            Boolean resp = false;
            Log.e(LOGe, "1");
            /**
             * Declaracion de Variables normales
             */
            JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
            String respuesta = new String();//Respuesta en plano
            JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
            String cadenaJSONDatos = new String();//Donde se genera el JSON a enviar
            String fechaCompleta = new String();//Al mes se le tiene que sumar un 1 porque empieza por 0
            ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
            /**
             * Funcion para cargar el contenido
             */
            Log.e(LOGe, "2");
            fechaCompleta = String.valueOf(et_fecha.getText());
            cadenaJSONDatos = "{\"fecha\":\"" + fechaCompleta + "\",\"n1\":\"" + String.valueOf(et_n1.getText()) + "\",\"n2\":\"" + String.valueOf(et_n2.getText()) + "\",\"n3\":\"" + String.valueOf(et_n3.getText()) + "\",\"n4\":\"" + String.valueOf(et_n4.getText()) + "\",\"n5\":\"" + String.valueOf(et_n5.getText()) + "\",\"e1\":\"" + String.valueOf(et_e1.getText()) + "\",\"e2\":\"" + String.valueOf(et_e2.getText()) + "\"}";
            try {
                Log.e(LOGe, "3");
                cadena.put("tarea", "Guardar Resultado");//Le asignamos los datos que necesitemos
                cadena.put("datos", cadenaJSONDatos);//Le asignamos los datos que necesitemos

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(LOGe, "4");
            cadena.toString(); //Para obtener la cadena de texto de tipo JSON
            // ENVIAMOS CONSULTA
            // Enviamos la consulta y cargamos los datos en los array
            Log.e(LOGe, "5");
            respuestaJSON = conectBD.consultaSQLJSON(cadena);
            Log.e(LOGe, "6");
            //Log.e("RESPUESTAJSON", String.valueOf(conectBD.consultaSQLJSON(cadena)));
            try {
                Log.e(LOGe, "7");
                //Ahora extraemos del JSON la parte "Respuesta" para saber si es un OK o un Error
                respuesta = respuestaJSON.getString("Respuesta");

                if (respuesta.equals("OK")) {
                    resp = true;


                } else {
                    PHPError = respuesta;
                    resp = false;

                }

           } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(LOGe, "9");



            return resp;

        }

        ;

        /**
         * Se ejecuta antes de empezar la conexion con la base de datos
         */
        protected void onPreExecute() {
            btn_guardar_nuevo_resultado.setVisibility(View.GONE);
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
        protected void onPostExecute(Boolean respuesta) {
            //Dejamos el boton y el cargdor como estaba y seguimos
            btn_guardar_nuevo_resultado.setVisibility(View.VISIBLE);
            pb_cargando.setVisibility(View.GONE);
            if (respuesta) {
                Toast.makeText(getActivity(), "Registro Guardado Correctamente!", Toast.LENGTH_LONG).show();
                Log.e(LOGe, "True");
                Log.e(LOGe, "8");
                //Vaciamos los campos
                et_e1.setText("");
                et_e2.setText("");
                et_n1.setText("");
                et_n2.setText("");
                et_n3.setText("");
                et_n4.setText("");
                et_n5.setText("");
                et_fecha.setText("");
            } else{
                Toast.makeText(getActivity(), PHPError, Toast.LENGTH_LONG).show();
                Log.e(LOGe, "False");
            }
        }

    }
}