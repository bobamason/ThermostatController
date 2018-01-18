package net.masonapps.thermostatcontroller;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorManager.DynamicSensorCallback;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.contrib.driver.pwmservo.Servo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

/**
 * 
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PWM_BUS = "BUS NAME";
    private SensorManager sensorManager;
    private TemperaturePressureEventListener sensorEventListener;
    private DynamicSensorCallback dynamicSensorCallback = new DynamicSensorCallback() {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                Log.i(TAG, "Temperature sensor connected");
                sensorEventListener = new TemperaturePressureEventListener();
                sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    };
    private Servo servo;
    private DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startTemperaturePressureRequest();
        setupServo();
        try {
            servo.setAngle(30);
        } catch (IOException e) {
            Log.e(TAG, "Error setting the angle", e);
        }
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopTemperaturePressureRequest();
        destroyServo();
    }

    private void startTemperaturePressureRequest() {
        this.startService(new Intent(this, TemperaturePressureService.class));
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerDynamicSensorCallback(dynamicSensorCallback);
    }

    private void stopTemperaturePressureRequest() {
        this.stopService(new Intent(this, TemperaturePressureService.class));
        sensorManager.unregisterDynamicSensorCallback(dynamicSensorCallback);
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void setupServo() {
        try {
            servo = new Servo(PWM_BUS);
            servo.setAngleRange(0f, 180f);
            servo.setEnabled(true);
        } catch (IOException e) {
            Log.e(TAG, "Error creating Servo", e);
        }
    }

    private void destroyServo() {
        if (servo != null) {
            try {
                servo.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Servo");
            } finally {
                servo = null;
            }
        }
    }

    private class TemperaturePressureEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.i(TAG, "sensor changed: " + event.values[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "sensor accuracy changed: " + accuracy);
        }
    }
}
