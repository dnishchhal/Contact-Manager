package com.ndstudio.contacts;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Nishchhal on 25-Jun-16.
 */
public class ContactCursorAdapter extends CursorAdapter{

    public ContactCursorAdapter(Context context, Cursor cursor ,int flag)
    {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.listName);
        TextView number = (TextView) view.findViewById(R.id.listNumber);
        ImageView listCall = (ImageView)view.findViewById(R.id.listCall);
        ImageView listMessage = (ImageView) view.findViewById(R.id.listMessage);
        ImageView listDP = (ImageView)view.findViewById(R.id.listDP);

        String strName = cursor.getString(cursor.getColumnIndex("name"));
        String strNum = cursor.getString(cursor.getColumnIndex("mobile"));
        String img = cursor.getString(cursor.getColumnIndex("img"));

        name.setText(strName);
        number.setText(strNum);
        listCall.setTag(cursor.getInt(0));
        listMessage.setTag(cursor.getInt(0));
        listDP.setTag(cursor.getInt(0));

        if(img.equals("@drawable/blank_dp"))
        {
            listDP.setImageResource(R.drawable.blank_dp);
        }
        else
        {
            File file = new File(img);

            RoundedBitmapDrawable roundDP = RoundedBitmapDrawableFactory.create(view.getResources(), BitmapFactory.decodeFile(img));
            roundDP.setCircular(true);
            roundDP.setAntiAlias(true);

            listDP.setImageDrawable(roundDP);
            if(!file.exists())
                listDP.setImageResource(R.drawable.blank_dp);
        }

    }
}
