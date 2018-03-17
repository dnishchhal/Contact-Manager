package com.ndstudio.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Nishchhal on 24-Jun-16.
 */
public class contact_view extends AppCompatActivity {

    ImageButton msg;
    FloatingActionButton fab;
    String img;
    TextView name,number,email,address,group,dob;
    ImageView dispPic;
    int _id;
    Cursor cursor;
    DBHandler dbh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);

        Intent myIntent = getIntent();

        dbh = new DBHandler(this);
        dbh.openReadable();
        img = "@drawable/blank_dp";
        name = (TextView)findViewById(R.id.txtName);
        number = (TextView)findViewById(R.id.txtNumber);
        email = (TextView)findViewById(R.id.txtEmail);
        address = (TextView)findViewById(R.id.txtAddress);
        group = (TextView)findViewById(R.id.txtGroup);
        dispPic = (ImageView) findViewById(R.id.dispPic);
        dob = (TextView)findViewById(R.id.txtBirthDay);


        _id = myIntent.getIntExtra("id",-1);
        if(_id!=(-1))
        {
            cursor = dbh.getSpecific(_id);

            name.setText(cursor.getString(cursor.getColumnIndex("name")));
            number.setText(cursor.getString(cursor.getColumnIndex("mobile")));
            email.setText(cursor.getString(cursor.getColumnIndex("email")));
            address.setText(cursor.getString(cursor.getColumnIndex("address")));
            group.setText(cursor.getString(cursor.getColumnIndex("grup")));
            dob.setText(cursor.getString(cursor.getColumnIndex("dob")));
            img = cursor.getString(cursor.getColumnIndex("img"));
        }

        if(new File(img).exists()==false)
            img = "@drawable/blank_dp";
        else
        {
            RoundedBitmapDrawable roundDP = RoundedBitmapDrawableFactory.create(getResources(),BitmapFactory.decodeFile(img));
            roundDP.setCircular(true);
            roundDP.setAntiAlias(true);

            dispPic.setImageDrawable(roundDP);
        }

        final Intent editIntent = new Intent(this,contact_edit.class);
        fab = (FloatingActionButton)findViewById(R.id.edit);

        msg = (ImageButton)findViewById(R.id.msg);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + number.getText().toString()));
                startActivity(sendIntent);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIntent.putExtra("id",_id);
                editIntent.putExtra("choice","update");
                startActivity(editIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*public void viewDisable()
    {
        if(email.getText().toString().isEmpty())
            findViewById(R.id.imgMail).setVisibility(View.INVISIBLE);
        if(address.getText().toString().isEmpty())
            findViewById(R.id.imgAddress).setVisibility(View.INVISIBLE);
        if(group.getText().toString().isEmpty())
            findViewById(R.id.imgGroup).setVisibility(View.INVISIBLE);
        if(dob.getText().toString().isEmpty())
            findViewById(R.id.imgBirthDay).setVisibility(View.INVISIBLE);
    }*/

    public void sendMail(View view)
    {
        if(email.getText().toString().isEmpty())
            return;
        Intent mail = new Intent(Intent.ACTION_SENDTO);
        mail.setType("text/plain");
        mail.setData(Uri.parse("mailto:"+email.getText().toString()));
        startActivity(mail);
    }

    public void callNumber(View view)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number.getText().toString()));
        if (ActivityCompat.checkSelfPermission(contact_view.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(intent);
    }

}
