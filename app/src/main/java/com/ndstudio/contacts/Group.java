package com.ndstudio.contacts;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nishchhal on 24-Jun-16.
 */
public class Group extends list_contacts implements AdapterView.OnItemClickListener {

    ListView listGroup,ListView;
    ArrayAdapter<String> arrayAdapter;
    String groupListItem;
    DBHandler dbh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        dbh = new DBHandler(this);
        dbh.openReadable();

        listGroup = (ListView) findViewById(R.id.groupList);
        Cursor cursor = dbh.selectGroup();
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mArrayList.add(cursor.getString(cursor.getColumnIndex("grup")));
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList );
        listGroup.setAdapter(arrayAdapter);
        listGroup.setOnItemClickListener(this);

    }


    @Override
    public void onBackPressed() {

        setContentView(R.layout.group);
        listGroup = (ListView) findViewById(R.id.groupList);
        Cursor cursor = dbh.selectGroup();
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mArrayList.add(cursor.getString(cursor.getColumnIndex("grup")));
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList );
        listGroup.setAdapter(arrayAdapter);
        listGroup.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        setContentView(R.layout.list_contacts);
        groupListItem = parent.getItemAtPosition(position).toString();
        listView = (ListView)findViewById(R.id.list);
        fab = (FloatingActionButton)findViewById(R.id.add);
        fab.setVisibility(View.INVISIBLE);
        Cursor cursor = dbh.selectGroup(groupListItem);
        CursorAdapter cursorAdapter = new ContactCursorAdapter(Group.this,cursor,0);
        listView.setAdapter(cursorAdapter);
        editSearch = (EditText)findViewById(R.id.editSearch);
        editSearch.setHint(cursorAdapter.getCount() + " Contacts");
        listClick(Group.this);
        searchGroup(groupListItem);

    }

    public void searchGroup(final String grup)
    {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //editSearch.setText(searchView.getQuery());
                cursorAdapter = new ContactCursorAdapter(Group.this,dbHandler.selectDataWithConstrain(s,grup),0);
                listView.setAdapter(cursorAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
