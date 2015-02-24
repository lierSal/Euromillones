package euromillones.ateneasystems.es.euromillones.Clases;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cubel on 19/01/15.
 */

/**
 * Clase ZBaseDatos
 * Esta clase se encarga de conectar con PHP
 * Consta de dos funciones, una para recibir datos unicos en un JSON y otro para recibir
 * varios datos (Array) en JSON
 */
public class ZBaseDatos {
    /**
     * Funcion de consultaSQL
     *
     * @param JSON
     * @return Un JSON
     */
    public JSONObject consultaSQLJSON(JSONObject JSON) {
        Log.e("INFO", "Entra en SQL " + JSON);//Log para saber que llega el JSON
        JSONObject jsonRespuesta = new JSONObject(); //Respuesta recibida
        try {
            // Crear un cliente para la conexion
            HttpClient mClient = new DefaultHttpClient();
            //Añadimos un parametro para que no tarde mas de los milisegundos indicados
            HttpConnectionParams.setConnectionTimeout(mClient.getParams(),
                    10000); // Timeout Limit
            // Creamos una variable de respuesta
            HttpResponse response;
            //Creamos un objeto JSON que vendra con el llamamiento de la funcion
            JSONObject paqueteJSON = JSON;
            // Indicar la URL de Conexion
            String URLConnect = new String("http://www.ateneasystems.es/PW/Euromillones/appAndroid/app.php");
            //Hacemos varias comprobaciones
            Log.e("URL:", URLConnect);//Para la consola
            Log.e("JSON:", paqueteJSON.toString());//Para la consola
            // Establecer la conexion despues de indicar la url
            HttpPost mpost = new HttpPost(URLConnect);
            //Preparamos el Envio del paquete por post (metemos el JSON en formato Texto
            StringEntity se = new StringEntity(paqueteJSON.toString());
            //Añadimos la cabecera para application/json
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //Lo metemos en el paquete para enviar por post
            mpost.setEntity(se);
            //Enviamos el paquete
            response = mClient.execute(mpost);
            //Respuesta
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());
            //Metemos la respuesta en un objeto JSON
            JSONObject jsonResponse = new JSONObject(resFromServer);
            jsonRespuesta = jsonResponse;
            Log.e("RESPUESTA EN ZCLAS:", jsonRespuesta.toString());
            //Del objeto JSON cogemos los datos (Esto es de forma temporal)
            Log.i("Response from server", jsonResponse.getString("consulta"));
            // De prueba (Esto mete los dos datos del JSON en el string temporal para pruebas)
            //sb2 = jsonResponse.getString("consulta") + " " + jsonResponse.getString("sql");
        } catch (Exception e) {
            //Si se produce un error, lo mostramos
            Log.w(" error ", e.toString());
        }
        //Log.e("Return", sb2.toString());
        return jsonRespuesta;
    }

    /**
     * @param JSON
     * @return Un ArrayJSON
     */
    public JSONArray consultaSQLARRAY(JSONObject JSON) {
        Log.e("INFO", "Entra en SQL " + JSON);//Log para saber que llega el JSON
        JSONArray jsonRespuesta = new JSONArray(); //Respuesta recibida
        try {
            // Crear un cliente para la conexion
            HttpClient mClient = new DefaultHttpClient();
            //Añadimos un parametro para que no tarde mas de los milisegundos indicados
            HttpConnectionParams.setConnectionTimeout(mClient.getParams(),
                    10000); // Timeout Limit
            // Creamos una variable de respuesta
            HttpResponse response;
            //Creamos un objeto JSON que vendra con el llamamiento de la funcion
            JSONObject paqueteJSON = JSON;
            // Indicar la URL de Conexion
            String URLConnect = new String("http://www.ateneasystems.es/PW/Euromillones/appAndroid/app.php");
            //Hacemos varias comprobaciones
            Log.e("URL:", URLConnect);//Para la consola
            Log.e("JSON:", paqueteJSON.toString());//Para la consola
            // Establecer la conexion despues de indicar la url
            HttpPost mpost = new HttpPost(URLConnect);
            //Preparamos el Envio del paquete por post (metemos el JSON en formato Texto
            StringEntity se = new StringEntity(paqueteJSON.toString());
            //Añadimos la cabecera para application/json
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //Lo metemos en el paquete para enviar por post
            mpost.setEntity(se);
            //Enviamos el paquete
            response = mClient.execute(mpost);
            //Respuesta
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());
            //Metemos la respuesta en un objeto JSON
            JSONArray jsonResponse = new JSONArray(resFromServer);
            jsonRespuesta = jsonResponse;
            Log.e("RESPUESTA EN ZCLAS:", jsonRespuesta.toString());
            //Del objeto JSON cogemos los datos (Esto es de forma temporal)
            Log.i("Response from server", String.valueOf(jsonResponse.length()));
            // De prueba (Esto mete los dos datos del JSON en el string temporal para pruebas)
            //sb2 = jsonResponse.getString("consulta") + " " + jsonResponse.getString("sql");
        } catch (Exception e) {
            //Si se produce un error, lo mostramos
            Log.w(" error ", e.toString());
        }
        //Log.e("Return", sb2.toString());
        return jsonRespuesta;
    }
}
