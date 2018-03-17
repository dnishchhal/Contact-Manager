package com.ndstudio.contacts;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nishchhal on 24-Jun-16.
 */
public class contact_edit extends AppCompatActivity {

    FloatingActionButton fab;
    Intent intent;
    Bitmap photo;
    ImageView dispPic;
    EditText name,mobile,email,address,company,date;
    String strName,strMobile,strEmail,strAddress,strDob,strGroup,choice,img,mobilePic;
    AutoCompleteTextView group;
    DBHandler dbh;
    Cursor cursor;
    int _id,yr,month,day;
    public static final int MEDIA = 1 , CAMERA = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        final Intent myIntent = getIntent();
        _id = myIntent.getIntExtra("id",-1);

        intent = new Intent(this, contact_view.class);
        dbh = new DBHandler(this);
        dbh.openReadable();

        fab = (FloatingActionButton) findViewById(R.id.save);
        name = (EditText)findViewById(R.id.fname);
        mobile = (EditText)findViewById(R.id.listNumber);
        email = (EditText)findViewById(R.id.email);
        company = (EditText)findViewById(R.id.company);
        group = (AutoCompleteTextView) findViewById(R.id.group);
        address = (EditText)findViewById(R.id.address);
        dispPic = (ImageView)findViewById(R.id.dispPic);
        date = (EditText)findViewById(R.id.birthDate);

        Cursor cursorGroup = dbh.selectGroup();
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(cursorGroup.moveToFirst(); !cursorGroup.isAfterLast(); cursorGroup.moveToNext()) {
            mArrayList.add(cursorGroup.getString(cursorGroup.getColumnIndex("grup")));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList );
        group.setAdapter(arrayAdapter);

        choice = getIntent().getStringExtra("choice");

        if(choice.equals("update"))
        {
            cursor = dbh.getSpecific(_id);

            name.setText(cursor.getString(cursor.getColumnIndex("name")));
            mobile.setText(cursor.getString(cursor.getColumnIndex("mobile")));
            email.setText(cursor.getString(cursor.getColumnIndex("email")));
            address.setText(cursor.getString(cursor.getColumnIndex("address")));
            group.setText(cursor.getString(cursor.getColumnIndex("grup")));
            date.setText(cursor.getString(cursor.getColumnIndex("dob")));
            img = cursor.getString(cursor.getColumnIndex("img"));
            mobilePic = cursor.getString(cursor.getColumnIndex("mobile"));
            File availFile = new File("/sdcard/DirName/"+mobile.getText().toString());
            if(!img.equals("@drawable/blank_dp")&&!availFile.exists())
                img = "@drawable/blank_dp";
            else if(availFile.exists()) {
                img = "/sdcard/DirName/" + mobile.getText().toString();
            }
            dispPic.setImageResource(R.drawable.blank_dp);
            Log.d("mobile",mobile.getText().toString());
            Toast.makeText(this, img, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, availFile.exists()?"true":"false", Toast.LENGTH_SHORT).show();
            if(availFile.exists())
            {
                RoundedBitmapDrawable roundDP = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeFile(img));
                roundDP.setCircular(true);
                roundDP.setAntiAlias(true);
                dispPic.setImageDrawable(roundDP);
            }
        }

        dispPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strName = name.getText().toString();
                strMobile = mobile.getText().toString();
                strEmail = email.getText().toString();
                strAddress = address.getText().toString();
                strGroup = group.getText().toString();

                if(img.equals("done") || new File("/sdcard/DirName/" + strMobile).exists())
                    img = "/sdcard/DirName/" + strMobile;

                if(strGroup.trim().isEmpty())
                    strGroup = "UNCATEGORIZED";

                strDob = date.getText().toString();

                switch (choice)
                {
                    case "add" :

                        if(strName.trim().isEmpty()) {
                            name.setError("Enter Your Name"); return;
                        }

                        if (strMobile.trim().isEmpty()) {
                            mobile.setError("Enter Mobile Number"); return;
                        }

                        long tag;
                        tag = dbh.addEntry(strName, strMobile, strEmail, strAddress, img, strGroup, strDob);
                        if(tag>0)
                        {
                            Toast.makeText(contact_edit.this, "Contact Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(contact_edit.this, "Number Already Saved as "+dbh.getSpecific(strMobile).getString(1)
                                    , Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!img.equals("@drawable/blank_dp"))
                            createDirectoryAndSaveFile(photo,strMobile);
                        cursor = dbh.getSpecific(strMobile);
                        _id = cursor.getInt(cursor.getColumnIndex("_id"));
                        break;

                    case "update" :
                        dbh.editEntry(_id,strName, strMobile, strEmail, strAddress, img, strGroup, strDob);
                        Toast.makeText(contact_edit.this, "Contact Updated Successfully", Toast.LENGTH_SHORT).show();
                        break;
                }

                intent.putExtra("id",_id);
                startActivity(intent);
                finish();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yr = Calendar.getInstance().get(Calendar.YEAR);
                month = Calendar.getInstance().get(Calendar.MONTH);
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(contact_edit.this, datePicker, yr, month, day).show();

            }
        });

    }



    public  void selectImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(contact_edit.this);
        builder.setCancelable(false);
        builder.setTitle("Profile Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    img = "done";
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA);
                } else if (items[item].equals("Choose from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    img = "done";
                    intent.setType("image/*");

                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 400);
                    intent.putExtra("outputY", 400);

                    startActivityForResult(intent, MEDIA);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDIA && resultCode == RESULT_OK && data != null){

            photo = data.getExtras().getParcelable("data");
            RoundedBitmapDrawable roundDP = RoundedBitmapDrawableFactory.create(getResources(),photo);
            roundDP.setCircular(true);
            roundDP.setAntiAlias(true);

            ImageView dispPic = (ImageView)findViewById(R.id.dispPic);
            dispPic.setImageDrawable(roundDP);
            createDirectoryAndSaveFile(roundDP.getBitmap(),mobile.getText().toString());
        }

        else if(requestCode == CAMERA && resultCode == RESULT_OK && data != null)
        {
            photo = data.getExtras().getParcelable("data");
            RoundedBitmapDrawable roundDP = RoundedBitmapDrawableFactory.create(getResources(),photo);
            roundDP.setCircular(true);
            roundDP.setAntiAlias(true);

            ImageView dispPic = (ImageView)findViewById(R.id.dispPic);
            dispPic.setImageDrawable(roundDP);
        }

    }


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/DirName/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        Toast.makeText(contact_edit.this, "Changes Discarded", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    public DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yr=year;
            month=monthOfYear;
            day=dayOfMonth;
            date.setText(day+"/"+(month+1)+"/"+yr);
        }
    };

}
