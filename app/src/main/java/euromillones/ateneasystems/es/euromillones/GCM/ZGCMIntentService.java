package euromillones.ateneasystems.es.euromillones.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notificaciones_ateneasystems)
                        .setLargeIcon((((BitmapDrawable) getResources()
                                .getDrawable(R.drawable.ic_notificaciones_ateneasystems)).getBitmap()))
                        .setContentTitle("Atenea Systems")
                        .setContentText(msg);

        Intent notIntent = new Intent(this, PrivateActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
        notIntent.putExtra("ID", NOTIF_ALERTA_ID); // Mismo ID que la notificacion para luego borrar la notificacion de la barra superior
    }
}
