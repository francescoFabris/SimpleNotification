package esp1617.dei.unipd.it.simplenotification;

import android.app.Notification.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private int hour;
    private int min;
    private int day;
    private int month;
    private int year;

    private Notification.Builder notBuilder;
    private static final int NOTIFICATION_ID =1;

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
                min = position + 1;
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
                day = position + 1;
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
                month = position + 1;
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
                year = 1 + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        notBuilder=new Notification.Builder(this);
        notBuilder
                .setAutoCancel(true)
                .setTicker("Ticker")
                .setSmallIcon(R.mipmap.ic_launcher);



        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nota = et.getText().toString();
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day, hour, min);
                notBuilder
                        .setWhen(cal.getTimeInMillis())
                        .setContentTitle("ContentTitle")
                        .setContentText("ContentText");

                NotificationManager notManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notManager.notify(1,notBuilder.build());


                et.setText("");
                spinner_hour.setSelection(0);
                spinner_min.setSelection(0);
                spinner_day.setSelection(0);
                spinner_month.setSelection(0);
                spinner_year.setSelection(0);
            }
        });

    }


}
