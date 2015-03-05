package euromillones.ateneasystems.es.euromillones.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import euromillones.ateneasystems.es.euromillones.Login_Activity;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 27/02/15.
 */
public class ZGCMIntentService extends IntentService {
    private static int NOTIF_ALERTA_ID = 1;

    public ZGCMIntentService() {
        super("ZGCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                mostrarNotification(extras.getString("message"));
            }
        }

        ZGCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void mostrarNotification(String msg) {
        /**
         * Primero cargamos la informacion del archivo de configuracion
         */
        final SharedPreferences config = getSharedPreferences("euromillones.ateneasystems.es.euromillones_preferences", Context.MODE_PRIVATE);
        if (config.getString("user", "").equals("")) {
            //No hacer nada
        } else {
            if (config.getBoolean("avisoNuevoResultado", true)) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                //Ruta del sonido personalizado
                Uri pathSonido = Uri.parse("android.resource://euromillones.ateneasystems.es.euromillones/" + R.raw.magicaldrug);
                //Notificacion
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_notificaciones_ateneasystems)
                                .setLargeIcon((((BitmapDrawable) getResources()
                                        .getDrawable(R.drawable.ic_launcher)).getBitmap()))
                                .setContentTitle("AteneaSystems")
                                        //.setSound(pathSonido)//Sonido
                                .setPriority(NotificationCompat.PRIORITY_LOW)//Prioridad de la notificacion
                                        //.setVibrate(new long[]{100, 250, 100, 500})//Vibracion, milisegungos: Esperar 100, vibrar 250, esperar 100, vibrar 500
                                .setContentText(msg);
                //IF para sonido y vibracion
                if (config.getBoolean("avisoNuevoResultado_sonido", true)) {
                    mBuilder.setSound(pathSonido);//Sonido
                }
                if (config.getBoolean("avisoNuevoResultado_vibracion", true)) {
                    //mBuilder.setVibrate(new long[]{100, 250, 100, 500});//Vibracion, milisegungos: Esperar 100, vibrar 250, esperar 100, vibrar 500
                    //mBuilder.setVibrate(new long[]{100, 150, 100, 150,100,150,100,500});//Vibracion
                    //mBuilder.setVibrate(new long[]{100, 100, 100, 100, 400, 100, 100, 100, 100, 100});//Vibracion
                    mBuilder.setVibrate(new long[]{100, 1000, 500, 100, 100, 100});//Vibracion
                }
                //Led
                if (config.getBoolean("avisoNuevoResultado_led", true)) {
                    //mBuilder.setLights(Color.argb(1,6, 0, 184),100,100);//Color en ARBG
                    mBuilder.setLights(Color.CYAN, 500, 2000);
                }


                Intent notIntent = new Intent(this, Login_Activity.class);
                PendingIntent contIntent = PendingIntent.getActivity(
                        this, 0, notIntent, 0);

                mBuilder.setContentIntent(contIntent);
                mBuilder.setAutoCancel(true); //Esto es para eliminar la notificacion al pulsarla
                mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
                //android.os.Process.killProcess(android.os.Process.myPid());//Matar proceso
                NOTIF_ALERTA_ID = NOTIF_ALERTA_ID + 1;
            }
        }
    }

}
