package euromillones.ateneasystems.es.euromillones.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import euromillones.ateneasystems.es.euromillones.PrivateActivity;
import euromillones.ateneasystems.es.euromillones.R;

/**
 * Created by cubel on 27/02/15.
 */
public class ZGCMIntentService extends IntentService {
    private static final int NOTIF_ALERTA_ID = 1;

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
        if (config.getBoolean("avisoNuevoResultado", true)) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_notificaciones_ateneasystems)
                            .setLargeIcon((((BitmapDrawable) getResources()
                                    .getDrawable(R.drawable.ic_notification_ateneasystems_negro)).getBitmap()))
                            .setContentTitle("AteneaSystems")
                            .setPriority(NotificationCompat.PRIORITY_LOW)//Prioridad de la notificacion
                            .setVibrate(new long[]{100, 250, 100, 500})//Vibracion, milisegungos: Esperar 100, vibrar 250, esperar 100, vibrar 500
                            .setContentText(msg);

            Intent notIntent = new Intent(this, PrivateActivity.class);
            PendingIntent contIntent = PendingIntent.getActivity(
                    this, 0, notIntent, 0);

            mBuilder.setContentIntent(contIntent);
            mBuilder.setAutoCancel(true); //Esto es para eliminar la notificacion al pulsarla
            mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
        }
    }

}
