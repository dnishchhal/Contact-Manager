package com.ndstudio.contacts;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends TabActivity implements SensorEventListener {

    TabHost tabHost;
    TabHost.TabSpec tabspec1, tabspec2, tabspec3;
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 5.0;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        tabHost=getTabHost();

        tabspec1=tabHost.newTabSpec("Dialer");
        tabspec1.setIndicator("Dialer");
        Intent intent1=new Intent(MainActivity.this, Dialer.class);
        tabspec1.setContent(intent1);

        tabspec2=tabHost.newTabSpec("Contact");
        tabspec2.setIndicator("Contact");
        Intent intent2=new Intent(MainActivity.this, list_contacts.class);
        tabspec2.setContent(intent2);

        tabspec3=tabHost.newTabSpec("Group");
        tabspec3.setIndicator("Group");
        Intent intent3=new Intent(MainActivity.this, Group.class);
        tabspec3.setContent(intent3);

        tabHost.addTab(tabspec1);
        tabHost.addTab(tabspec2);
        tabHost.addTab(tabspec3);

        tabHost.setCurrentTab(1);

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        AlertDialog.Builder about = new AlertDialog.Builder(MainActivity.this);
        about.setMessage("This is Our Contact App And We Are Nishchhal, Paryul and Priya");
        about.setTitle("About Us");
        about.setCancelable(false);
        about.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flag = 0;
            }
        });

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            if(flag == 1)
            {
                return;
            }

            if (deltaX > deltaY) {

                about.show();
                flag = 1;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
