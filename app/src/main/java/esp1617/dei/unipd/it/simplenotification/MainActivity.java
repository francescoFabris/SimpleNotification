package esp1617.dei.unipd.it.simplenotification;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int hour=-1;
    private int min=-1;
    private int mDay;
    private int mMonth;
    private int mYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et = (EditText) findViewById(R.id.note_space);
        Button bu = (Button) findViewById(R.id.set_notification);
        final TextView mDate =(TextView) findViewById(R.id.date_text);
        final TextView mHour =(TextView) findViewById(R.id.hour_text);

        mDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //current date
                final java.util.Calendar c = java.util.Calendar.getInstance();
                mDay = c.get(java.util.Calendar.DAY_OF_MONTH);
                mMonth = c.get(java.util.Calendar.MONTH);
                mYear = c.get(java.util.Calendar.YEAR);

                showDateDialog(mYear, mMonth, mDay);
            }
        });

        mHour.setOnClickListener(new EditText.OnClickListener() {
            public void onClick(View v) {
                if (hour == -1 || min == -1) {
                    java.util.Calendar c = java.util.Calendar.getInstance();
                    hour = c.get(java.util.Calendar.HOUR);
                    min = c.get(java.util.Calendar.MINUTE);
                }

                showTimeDialog(hour, min);
            }
        });


        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                int id = preferences.getInt("id",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("id", ++id);
                editor.apply();

                Log.d(TAG, "onClick called", new Exception());
                String nota = et.getText().toString();
                Calendar cal = Calendar.getInstance(); //istanza di Calendar
                cal.set(mYear, mMonth, mDay, hour, min);  //impostazione data definita
                Date now = Calendar.getInstance().getTime();
                Date date = cal.getTime();
                long delta = date.getTime()-now.getTime();

                Log.d(TAG, mDay+"/"+mMonth+"/"+mYear+"   "+hour+":"+min, new Exception());
                Log.d(TAG, "now.getTime()="+now.getTime(), new Exception());
                Log.d(TAG, "date.getTime()="+date.getTime(), new Exception());
                Log.d(TAG, "SystemClock.elapsedRealtime()="+SystemClock.elapsedRealtime(), new Exception());
                Log.d(TAG, "delta="+delta, new Exception());

                NotificationTemplate nt = new NotificationTemplate(id, cal, nota);
                if(delta<0){
                    Toast.makeText(MainActivity.this, R.string.unsuccess,Toast.LENGTH_SHORT).show();
                }
                else{
                    scheduleNotification(createNotification(MainActivity.this,nt), nt);
                    Log.d(TAG, "Creata notifica con id "+nt.getId(), new Exception());
                    Toast.makeText(MainActivity.this, R.string.success,Toast.LENGTH_SHORT).show();
                }



                et.setText("");
                mDate.setText(R.string.input_date);
                mHour.setText(R.string.input_time);
            }
        });


    }

    private void showDateDialog(int year, int month, int dayOfMonth){
        (new DatePickerDialog(MainActivity.this, dateSetListener, year, month, dayOfMonth)).show();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            TextView mDate = (TextView) findViewById(R.id.date_text);
            mDate.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear));
        }
    };

    private void showTimeDialog(int hour, int min) {
        (new TimePickerDialog(MainActivity.this, timeSetListenerS, hour, min, true)).show();
    }

    private final TimePickerDialog.OnTimeSetListener timeSetListenerS = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            hour = hourOfDay;
            min = minute;
            TextView spaceSleep = (TextView) findViewById(R.id.hour_text);
            spaceSleep.setText(hour + " : " + min);

        }
    };

    private void scheduleNotification(Notification n, NotificationTemplate nt){

        Log.d(TAG, "scheduleNotification called", new Exception());
        Intent notificationIntent = new Intent(this, Receiver.class);
        notificationIntent.putExtra(Receiver.NOTIFICATION, n);
        notificationIntent.putExtra(Receiver.NOTIFICATION_ID, nt.getId());
        PendingIntent pInt = PendingIntent.getBroadcast(this, nt.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alMan = (AlarmManager)getSystemService(Context.ALARM_SERVICE); // alMan è un AlarmManager
        //alMan.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, when, pInt);                // alBan è un Cantante
        alMan.set(AlarmManager.RTC_WAKEUP, nt.getWhen().getTime().getTime(), pInt);
    }

    private Notification createNotification(Context context, NotificationTemplate nt){
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(getResources().getString(R.string.not_title))
                .setContentText(nt.getText())
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        return builder.build();
    }

}
