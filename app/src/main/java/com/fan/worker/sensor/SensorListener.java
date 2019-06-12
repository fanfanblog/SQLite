package com.fan.worker.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fan.worker.R;

/**
 * Created by pc on 19-3-17.
 */

public class SensorListener extends Activity implements View.OnClickListener{



    private SensorManager sensorManager;
    private Context context;
    private Sensor gyro;
    private SensorEventListener sensorEventListener;
    private TextView info;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.sensor);
        init();

    }


    private void init() {

        Button start = findViewById(R.id.start);
        Button stop = findViewById(R.id.stop);
        info = findViewById(R.id.info);

        setOnClickListener(start,stop);

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                String result = "\nx = " + x + "\ny =" + y + "\nz = " + z;
                info.setText(getString(R.string.info,result));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void startListener(){
        sensorManager.registerListener(sensorEventListener, gyro, SensorManager.SENSOR_DELAY_NORMAL);
    }


    private void stopListener(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void setOnClickListener(View...views){
        for (View view:views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                startListener();
                break;
            case R.id.stop:
                stopListener();
                break;
            default:
                break;
        }
    }
}
