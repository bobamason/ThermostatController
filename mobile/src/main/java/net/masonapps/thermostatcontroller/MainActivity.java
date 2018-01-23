package net.masonapps.thermostatcontroller;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private TextView textStart;
    private TextView textEnd;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        textStart = findViewById(R.id.start_text);
        textEnd = findViewById(R.id.start_text);
    }

    public void onSetButtonClicked(View view) {
        if(view.getId() == R.id.buttonStart){
            launchTimePicker((timePicker, hour, minute) -> {
                textStart.setText(getTimeText(hour, minute));
                databaseRef.child("time_intervals").child("start").setValue(String.format(Locale.ENGLISH, "%d,%d", hour, minute));
            });
        } else if(view.getId() == R.id.buttonEnd){
            launchTimePicker((timePicker, hour, minute) -> {
                textEnd.setText(getTimeText(hour, minute));
                databaseRef.child("time_intervals").child("end").setValue(String.format(Locale.ENGLISH, "%d,%d", hour, minute));
            });
        }
    }

    private String getTimeText(int hour, int minute) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return df.format(c.getTime());
    }

    private void launchTimePicker(TimePickerDialog.OnTimeSetListener listener) {
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(this, listener, hour, minute, false).show();
    }
}
