package com.ndstudio.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Nishchhal on 24-Jun-16.
 */
public class DBHandler {

    public static final String DB_UNAME = "name";
    public static final String DB_MOBILE = "mobile";
    public static final String DB_EMAIL = "email";
    public static final String DB_ADDRESS = "address";
    public static final String DB_IMG = "img";
    public static final String DB_DOB = "dob";
    public static final String DB_GROUP = "grup";


    public static final String DB_NAME = "Contacts";
    public static final String DB_TABLE = "Entry";
    public static final String DB_CREATE = "CREATE TABLE "+DB_TABLE+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+DB_UNAME+" TEXT, "+DB_MOBILE+" TEXT UNIQUE , "+DB_EMAIL+" TEXT, "+DB_ADDRESS+" TEXT, "+DB_IMG+" TEXT, "+DB_DOB+" TEXT, "+DB_GROUP+" TEXT )";
    public static final int DB_VERSION = 1;

    public SQLiteDatabase db;
    public SQLHelper helper;
    public Context context;

    DBHandler(Context context)
    {
        this.context = context;
        helper = new SQLHelper();
        db = helper.getWritableDatabase();
    }

    public DBHandler openReadable() throws SQLiteException
    {
        helper = new SQLHelper();
        db = helper.getReadableDatabase();
        return this;
    }

    public long addEntry(String name, String number, String email, String address, String img, String group, String dob)
    {
        ContentValues cv = new ContentValues();
        cv.put(DB_UNAME,name);
        cv.put(DB_MOBILE,number);
        cv.put(DB_EMAIL,email);
        cv.put(DB_ADDRESS,address);
        cv.put(DB_IMG,img);
        cv.put(DB_DOB,dob);
        cv.put(DB_GROUP,group);
        return db.insert(DB_TABLE,null,cv);
    }

    public void deleteEntry(int id)
    {
        db.delete(DB_TABLE,"_id = "+id,null);
    }

    public long  editEntry(int id, String name, String number, String email, String address, String img, String group, String dob)
    {
        ContentValues cv = new ContentValues();
        cv.put(DB_UNAME,name);
        cv.put(DB_MOBILE,number);
        cv.put(DB_EMAIL,email);
        cv.put(DB_ADDRESS,address);
        cv.put(DB_IMG,img);
        cv.put(DB_DOB,dob);
        cv.put(DB_GROUP,group);
        return db.update(DB_TABLE, cv,"_id = "+id,null);
    }

    public Cursor getValues()
    {
        String column[] = {"_id", DB_UNAME, DB_MOBILE, DB_EMAIL, DB_ADDRESS, DB_IMG, DB_DOB, DB_GROUP};
        Cursor cursor = db.query(DB_TABLE, column, null, null, null, null,DB_UNAME);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getSpecific(int id)
    {
        String column[] = {"_id", DB_UNAME, DB_MOBILE, DB_EMAIL, DB_ADDRESS, DB_IMG, DB_DOB, DB_GROUP};
        //Cursor cursor = db.query(DB_TABLE, column, "_id"+" = "+id, null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM Entry WHERE _id = "+id,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getSpecific(String mobile)
    {
        String column[] = {"_id", DB_UNAME, DB_MOBILE, DB_EMAIL, DB_ADDRESS, DB_IMG, DB_DOB, DB_GROUP};
        //Cursor cursor = db.query(DB_TABLE, column, "_id"+" = "+id, null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM Entry WHERE mobile = '"+mobile+"'",null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor selectDataWithConstrain(CharSequence c) {

        Cursor cursor = db.rawQuery("SELECT * FROM  Entry WHERE name LIKE '%"+c+"%' ORDER BY name", null);
        return cursor;
    }
    public Cursor selectMobile(CharSequence mobile) {

        Cursor cursor = db.rawQuery("SELECT * FROM  Entry WHERE mobile LIKE '"+mobile+"%' ORDER BY name", null);
        return cursor;
    }

    public Cursor selectDataWithConstrain(CharSequence c , String grup) {

        Cursor cursor = db.rawQuery("SELECT * FROM  Entry WHERE name LIKE '%"+c+"%' AND grup = '"+grup+"' ORDER BY name", null);
        return cursor;
    }

    public Cursor selectGroup(String grup)
    {
        return db.rawQuery("SELECT * FROM Entry WHERE grup = '"+grup+"' ORDER BY name",null);
    }

    public Cursor selectGroup()
    {
        return db.rawQuery("SELECT DISTINCT grup FROM Entry ORDER BY name",null);
    }


    public class SQLHelper extends SQLiteOpenHelper
    {

        public SQLHelper() {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
            Log.d("Upgrade","DATABASE TABLE Upgrade from Version "+oldVersion+" to "+newVersion);
            onCreate(db);

        }
    }

}
