package com.ndstudio.contacts;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.telecom.PhoneAccountHandle;
import android.telephony.SubscriptionManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static com.ndstudio.contacts.R.color.Transparent;

/**
 * Created by Nishchhal on 24-Jun-16.
 */
public class list_contacts extends AppCompatActivity  {

    FloatingActionButton fab;
    ListView listView;
    DBHandler dbHandler;
    Intent viewIntent;
    Cursor cursor;
    ContactCursorAdapter cursorAdapter;
    EditText editSearch;
    StringBuffer searchBuffer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_contacts);

        fab = (FloatingActionButton)findViewById(R.id.add);
        final Intent intent = new Intent(this,contact_edit.class);

        editSearch = (EditText)findViewById(R.id.editSearch);
        searchBuffer = new StringBuffer();

        dbHandler = new DBHandler(this);
        dbHandler.openReadable();

        listView = (ListView)findViewById(R.id.list);
        listView.requestFocus();
        cursor = dbHandler.getValues();
        editSearch.setHint(cursor.getCount() + " Contacts");
        cursorAdapter = new ContactCursorAdapter(this,cursor,0);

        listView.setAdapter(cursorAdapter);

        //Code Copied From Here
        listClick(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("choice","add");
                startActivity(intent);
                finish();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //editSearch.setText(searchView.getQuery());
                cursorAdapter = new ContactCursorAdapter(list_contacts.this,dbHandler.selectDataWithConstrain(s),0);
                listView.setAdapter(cursorAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    //onCall Functions

    public void listCall(View view)
    {
        int id = Integer.parseInt(view.getTag().toString());
        Cursor cursor = dbHandler.getSpecific(id);
        String number = cursor.getString(cursor.getColumnIndex("mobile"));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(list_contacts.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(callIntent);
    }

    public void listMessage(View view)
    {
        int id = Integer.parseInt(view.getTag().toString());
        Cursor cursor = dbHandler.getSpecific(id);
        String number = cursor.getString(cursor.getColumnIndex("mobile"));
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + number));
        startActivity(sendIntent);
    }

    public void listDP(View view) throws IOException {
        int id = Integer.parseInt(view.getTag().toString());
        Cursor cursor = dbHandler.getSpecific(id);
        AlertDialog.Builder dpBulider = new AlertDialog.Builder(this);
        ImageView dpImage = new ImageView(this);
        dpImage.setScaleType(ImageView.ScaleType.FIT_XY);
        String img = cursor.getString(cursor.getColumnIndex("img"));

        dpBulider.setView(dpImage);
        if(img.equals("@drawable/blank_dp"))
        {
            dpImage.setImageResource(R.drawable.blank_dp);
        }
        else
        {
            dpImage.setImageBitmap(BitmapFactory.decodeFile(img));
        }
        dpBulider.show().getWindow().setLayout(400,400);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public  void listLongClick(final long id)
    {
        final CharSequence[] items = { "Call Contact", "Delete Contact", "Edit Contact","Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(list_contacts.this);
        builder.setCancelable(false);
        Cursor heading = dbHandler.getSpecific((int)id);
        builder.setTitle(heading.getString(heading.getColumnIndex("name")));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Dial From SIM 1")) {

                } else if (items[item].equals("Delete Contact")) {

                    dbHandler.deleteEntry((int)id);
                    File file = new File(new File("/sdcard/DirName/"), cursor.getString(cursor.getColumnIndex("mobile")));
                    if (file.exists()) {
                        file.delete();
                    }
                    cursorAdapter = new ContactCursorAdapter(list_contacts.this, dbHandler.getValues(),0);
                    listView.setAdapter(cursorAdapter);
                    editSearch.setHint(cursorAdapter.getCount() + " Contacts");

                } else if (items[item].equals("Edit Contact")) {

                    Intent edit = new Intent(list_contacts.this,contact_edit.class);
                    edit.putExtra("id",(int)id);
                    edit.putExtra("choice","update");
                    startActivity(edit);
                    finish();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    public void listClick(final Context context)
    {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                listLongClick(id);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewIntent = new Intent(context,contact_view.class);
                viewIntent.putExtra("id",(int)id);
                startActivity(viewIntent);
                finish();
            }

        });
    }

}
