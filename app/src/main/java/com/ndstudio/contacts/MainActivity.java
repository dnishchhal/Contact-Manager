package com.ndstudio.contacts;

import android.Manifest;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import java.security.Permission;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TabActivity {

    TabHost tabHost;
    TabHost.TabSpec tabspec1, tabspec2, tabspec3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        int readStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int call = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if(readStorage!= PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writeStorage != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(call!=PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),1);
        }


    }

    protected void onResume() {
        super.onResume();
    }
    protected void onPause() {
        super.onPause();
    }

}
