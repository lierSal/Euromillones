package euromillones.ateneasystems.es.euromillones;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import euromillones.ateneasystems.es.euromillones.Clases.ZDatosTemporales;


public class NFCActivity extends ActionBarActivity {
    /**
     * Variables para TAGs NFC
     */
    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;
    Context ctx;
    TextView tv_info;
    //FIN NFC
    //Datos temporales
    String User;
    String Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ctx = this;//Context
        /**
         * temporales
         */
        final ZDatosTemporales temporales = (ZDatosTemporales) ctx.getApplicationContext(); //Para los datos temporales
        User = temporales.getMailUser();
        Pass = temporales.getPassUser();
        //Toast.makeText(this, User, Toast.LENGTH_LONG).show();
        /**
         * componentes
         */
        tv_info = (TextView) findViewById(R.id.tv_info);
        adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) { //Para detectar si tiene NFC o NO
            tv_info.setText("Dispositivo sin NFC");
            //Toast.makeText(this, "Dispositivo sin NFC", Toast.LENGTH_LONG).show();
        } else {
            if (adapter.isEnabled()) {
                tv_info.setText("Acerca el TAG NFC");
                // Toast.makeText(this, "Acerca el TAG NFC", Toast.LENGTH_LONG).show();
            } else {
                tv_info.setText("Activa el NFC en Opciones del Dispositivo");
                // Toast.makeText(this, "Activa el NFC en Opciones del Dispositivo", Toast.LENGTH_LONG).show();
            }
            /**
             * Metemos datos en las variables NFC
             */
            ctx = this;//Context
            adapter = NfcAdapter.getDefaultAdapter(this);
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
            writeTagFilters = new IntentFilter[]{tagDetected};
        }

    }

    /**
     * Necesario para detecar tags
     */
    @Override
    protected void onNewIntent(Intent intent) {

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //Toast.makeText(this, "Detectado!!" + mytag.toString(), Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Detectado!!" + mytag.describeContents(), Toast.LENGTH_LONG).show();
            /**
             * cartel de seguir o no
             */
            //creamos la alerta de dialogo
            new AlertDialog.Builder(ctx).setTitle("TAG NFC Detectado?")
                    .setMessage("Quieres guardar los datos de Usuario\nen el TAG NFC?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Como Si quiere guardamos la informacion
                            //Toast.makeText(ctx,"SI",Toast.LENGTH_LONG).show();
                            //NdefFormatable
                            try {
                                write(User, Pass, mytag);
                            } catch (IOException e) {
                                Toast.makeText(ctx, "Error - Has quitado el TAG", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (FormatException e) {
                                Toast.makeText(ctx, "Error - Has quitado el TAG", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Como no quiere mostramos el mensaje
                            Toast.makeText(ctx, "NO", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNeutralButton("Borrar TAG", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Como no quiere mostramos el mensaje
                            //Toast.makeText(ctx, "Borrando TAG", Toast.LENGTH_LONG).show();
                            try {
                                formatearNFC(mytag);
                            } catch (IOException e) {
                                Toast.makeText(ctx, "Error - Has quitado el TAG", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (FormatException e) {
                                Toast.makeText(ctx, "Error - Has quitado el TAG", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
        //comprobarNFC(adapter);//Al pausar no hace falta
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteModeOn();
        comprobarNFC(adapter);
    }

    private void WriteModeOn() {
        if (adapter != null) { //Para detectar si tiene NFC o NO
            writeMode = true;
            adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
        }
    }

    private void WriteModeOff() {
        if (adapter != null) { //Para detectar si tiene NFC o NO
            writeMode = false;
            adapter.disableForegroundDispatch(this);
        }
    }

    /**
     * Funciones para escribir el tag
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void write(String mailUser, String passUser, Tag tag) throws IOException, FormatException {
        //Variables
        String idTag = bytesToHexString(tag.getId());//ID del tag
        // Creamos una instancia NDEF con nuestro Tag
        Ndef ndef = Ndef.get(tag);
        //Creamos una variable NDEF Mensaje para meterlo tendro del Tat
        NdefMessage message;

        //Creamos el mensaje principal
        NdefRecord[] records = {
                createRecord(mailUser),
                createRecord(passUser),
                createRecord(idTag),
                NdefRecord.createApplicationRecord(getPackageName())};
        //Metemos el contenido dentro del Mensaje NDEF
        message = new NdefMessage(records);
        //comprobamos si el mensaje cabe en el tag, si no, eliminamos parte del mensaje
        if (ndef.getMaxSize()<=message.getByteArrayLength()){
            //Creamos el mensaje recortado
            NdefRecord[] recordsMini = {
                    createRecord(mailUser),
                    createRecord(passUser)};
            //Metemos el contenido dentro del Mensaje NDEF
            message = new NdefMessage(recordsMini);
            //Toast.makeText(this, "Entra en el mini: " + getPackageName(), Toast.LENGTH_LONG).show();
        }
        //Conectamos con el tag NFC
        ndef.connect();
        //Preparamos los datos de capacidad a mostrar en pantalla
        String tamTotal = String.valueOf(ndef.getMaxSize());//Muestra los Bytes disponibles
        String tamDatos = String.valueOf(message.getByteArrayLength());//Muestra los Bytes que ocupa
        //Ahora grabamos el contenido del mensaje en el Tag.
        ndef.writeNdefMessage(message);
        //Cerramos la conexion con el tag NFC
        ndef.close();
        //Mostramos los datos de antes en pantalla
        Toast.makeText(this, "Capcidad Ocupada: " + tamDatos + "/" + tamTotal + "Bytes", Toast.LENGTH_LONG).show();
        //createRecord(passUser)};
        /////

        //Toast.makeText(this, NfcAdapter.EXTRA_TAG, Toast.LENGTH_LONG ).show();
        //Toast.makeText(this, "Información Guardada", Toast.LENGTH_LONG ).show();
        /////
        //Guardamos el Resultado
        //NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        //Ndef ndef = Ndef.get(tag);
        // Enable I/O
        //ndef.connect();
        //Datos Tag
       // String tamTotal = String.valueOf(ndef.getMaxSize());//Muestra los Bytes disponibles
        //String tamDatos = String.valueOf(message.getByteArrayLength());//Muestra los Bytes que ocupa
        // Write the message
        //ndef.writeNdefMessage(message);
        // Close the connection

        //Toast.makeText(this, "Capcidad Ocupada: " + tamDatos + "/" + tamTotal + "Bytes", Toast.LENGTH_LONG).show();


    }


    //Para guardar texto plano
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "es";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);


        return recordNFC;
    }

    //Borrar Tag
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void formatearNFC(Tag tag) throws IOException, FormatException {

        //Añade un mensaje vacio
        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null);
        //Cargamos el Tag
        Ndef ndef = Ndef.get(tag);
        //Conectamos
        ndef.connect();
        //Creamos el Paquete
        NdefMessage format = new NdefMessage(recordNFC);
        //Datos Tag
        String tamTotal = String.valueOf(ndef.getMaxSize());//Muestra los Bytes disponibles
        //Lo escribimos
        ndef.writeNdefMessage(format);
        //Cerramos la conexion
        ndef.close();
        Toast.makeText(this, "TAG Borrado: " + tamTotal + "Bytes Disponibles", Toast.LENGTH_LONG).show();


    }

    //Conversion de Bytes idTag en String idTag
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

    //Comprobar NFC de Nuevo
    private void comprobarNFC(NfcAdapter mitag) {
        if (mitag == null) { //Para detectar si tiene NFC o NO
            tv_info.setText("Dispositivo sin NFC");
            //Toast.makeText(this, "Dispositivo sin NFC", Toast.LENGTH_LONG).show();
        } else {
            if (mitag.isEnabled()) {
                tv_info.setText("Acerca el TAG NFC");
                //Toast.makeText(this, "Acerca el TAG NFC", Toast.LENGTH_LONG).show();
            } else {
                tv_info.setText("Activa el NFC en Opciones del Dispositivo");
                //Toast.makeText(this, "Activa el NFC en Opciones del Dispositivo", Toast.LENGTH_LONG).show();
            }
        }
    }
}
