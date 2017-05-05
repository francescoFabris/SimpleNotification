package esp1617.dei.unipd.it.simplenotification;

import android.content.BroadcastReceiver;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by boemd on 05/05/2017.
 */

public class Reciever extends BroadcastReceiver{

    public static String NOTIFICATION_ID ="notification_id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        NotificationManager nMan = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification not = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        nMan.notify(id, not);
    }
}
