package euromillones.ateneasystems.es.euromillones;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import euromillones.ateneasystems.es.euromillones.Clases.ZBaseDatos;
import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentMiCuenta;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentPredicciones;
import euromillones.ateneasystems.es.euromillones.Fragments.FragmentUltimosResultados;


public class FichaUserActivity extends AppCompatActivity {
    /**
     * Declaracion de componentes globales
     */
    private Button btn_Guardar;
    private TextView tv_nombre;
    private TextView tv_mail;
    private RadioButton rb_nivel1;
    private RadioButton rb_nivel2;
    private RadioButton rb_nivel3;
    private ProgressBar pb_cargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_user);
        // Cargamos los componentes
        btn_Guardar = (Button) findViewById(R.id.btn_Guardar);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_mail = (TextView) findViewById(R.id.tv_mail);
        rb_nivel1 = (RadioButton) findViewById(R.id.rb_nivel1);
        rb_nivel2 = (RadioButton) findViewById(R.id.rb_nivel2);
        rb_nivel3 = (RadioButton) findViewById(R.id.rb_nivel3);
        pb_cargando = (ProgressBar) findViewById(R.id.pb_cargando);
        //Toast.makeText(this, "Usuario con mail: " + getIntent().getStringExtra("Mail"), Toast.LENGTH_SHORT).show();
        /**
         * Funciones de Inicio
         */
        //Cargar los datos del usuario seleccionado
        ArrayList<String> datos = new ArrayList<String>();
        datos.add("Cargar");
        datos.add(getIntent().getStringExtra("Mail"));
        InformacionUser datosUser = new InformacionUser();
        datosUser.execute(datos);
        /**
         * Funciones de los botones
         */
        btn_Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> datos = new ArrayList<String>();
                datos.add("Guardar");
                datos.add(String.valueOf(tv_mail.getText()));
                if (rb_nivel1.isChecked()){
                    datos.add("1");
                }
                if (rb_nivel2.isChecked()){
                    datos.add("2");
                }
                if (rb_nivel3.isChecked()){
                    datos.add("3");
                }
                InformacionUser datosUser = new InformacionUser();
                datosUser.execute(datos);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ficha_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    /**
     * Cargar los datos del usuario
     */
    public JSONObject irABuscarDatos(String mail) {
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON

        try {
            cadena.put("tarea", "Cargar Usuario");//Le asignamos los datos que necesitemos
            cadena.put("datos", mail);//Le asignamos los datos que necesitemos


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
     */

    public Boolean cambiarDatos(String mail, String nivel) {
        //Log.e("Datos:", String.valueOf(mail));
        //Declaramos Variables
        JSONObject respuestaJSON = new JSONObject(); //Donde ira la respuesta
        ZBaseDatos conectBD = new ZBaseDatos(); //Creamos una variable conectBD con la clase "ZBaseDatos"
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        String respuesta = new String(); //Respuesta para saber si es OK o Error
        Boolean devovlerRespuesta = new Boolean(false); //Esto es lo que devolvera si es true o false
        String cadenaJSONDatos = new String();//Esto es para pasarle varias variables en un texto plano
        cadenaJSONDatos = "{\"mail\":\"" + mail + "\",\"nivel\":\"" + nivel + "\"}";
        try {
            cadena.put("tarea", "Modificar Nivel");//Le asignamos los datos que necesitemos
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
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
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
                resultado = cambiarDatos(String.valueOf(params[0].get(1)), String.valueOf(params[0].get(2)));
                if (resultado) {
                    respuesta.add("Datos Actualizados Correctamente");
                } else {
                    respuesta.add("Error al Actualizar los Datos");
                }

            } else if (String.valueOf(params[0].get(0)).equals("Cargar")) {
                respuesta.add("Cargar");
                datosUser = irABuscarDatos(String.valueOf(params[0].get(1)));
                try {
                    //Cargamos los datos en los campos de texto
                    respuesta.add(datosUser.getString("mail"));
                    respuesta.add(datosUser.getString("nombre"));
                    respuesta.add(datosUser.getString("apellidos"));
                    respuesta.add(datosUser.getString("nivel"));


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
                pb_cargando.setVisibility(View.GONE);
            } else if (respuesta.get(0).equals("Cargar")) {
                tv_mail.setText(respuesta.get(1));
                tv_nombre.setText(respuesta.get(2) + " " + respuesta.get(3));
                switch (respuesta.get(4)){
                    case "1":
                        rb_nivel1.setChecked(true);
                        break;
                    case "2":
                        rb_nivel2.setChecked(true);
                        break;
                    case "3":
                        rb_nivel3.setChecked(true);
                        break;
                }
                btn_Guardar.setVisibility(View.VISIBLE);
                pb_cargando.setVisibility(View.GONE);
            }
        }

    }
}
