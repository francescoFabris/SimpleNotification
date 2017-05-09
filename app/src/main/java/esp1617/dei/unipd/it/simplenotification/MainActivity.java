package esp1617.dei.unipd.it.simplenotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int hour;
    private int min;
    private int day;
    private int month;
    private int year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et = (EditText) findViewById(R.id.note_space);
        Button bu = (Button) findViewById(R.id.set_notification);
        final Spinner spinner_hour = (Spinner) findViewById(R.id.hour_spinner);
        final Spinner spinner_min = (Spinner) findViewById(R.id.minutes_spinner);
        final Spinner spinner_day = (Spinner) findViewById(R.id.day_spinner);
        final Spinner spinner_month = (Spinner) findViewById(R.id.month_spinner);
        final Spinner spinner_year = (Spinner) findViewById(R.id.year_spinner);

        ArrayAdapter<CharSequence> hour_adapter = ArrayAdapter.createFromResource(this, R.array.hour_array, android.R.layout.simple_spinner_item);
        hour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hour.setAdapter(hour_adapter);
        spinner_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> min_adapter = ArrayAdapter.createFromResource(this, R.array.minutes_array, android.R.layout.simple_spinner_item);
        min_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min.setAdapter(min_adapter);
        spinner_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                min = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> day_adapter = ArrayAdapter.createFromResource(this, R.array.day_array, android.R.layout.simple_spinner_item);
        day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(day_adapter);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> month_adapter = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
        month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(month_adapter);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this, R.array.years_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(year_adapter);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = 2017 + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                int id = preferences.getInt("id",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("id", ++id);
                editor.commit();

                Log.d(TAG, "onClick called", new Exception());
                String nota = et.getText().toString();
                Calendar cal = Calendar.getInstance(); //istanza di Calendar
                cal.set(year, month, day, hour, min);  //impostazione data predefinita
                //long time = cal.getTimeInMillis();
                Date now = Calendar.getInstance().getTime();
                Date date = cal.getTime();
                long delta = date.getTime()-now.getTime();

                Log.d(TAG, day+"/"+month+"/"+year+"   "+hour+":"+min, new Exception());
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
                spinner_hour.setSelection(0);
                spinner_min.setSelection(0);
                spinner_day.setSelection(0);
                spinner_month.setSelection(0);
                spinner_year.setSelection(0);

            }
        });


    }

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

    public Notification createNotification(Context context, NotificationTemplate nt){
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(getResources().getString(R.string.not_title))
                .setContentText(nt.getText())
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap());

        return builder.build();
    }

}
