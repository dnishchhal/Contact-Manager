package com.ndstudio.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nishchhal on 24-Jun-16.
 */
public class Dialer extends list_contacts {

    GridView dialPad;
    String num;
    ImageButton callButton;
    TextView dialNum;
    ImageView backspace;
    ListView dialList;

    int img[] = {R.drawable.num1, R.drawable.num2, R.drawable.num3, R.drawable.num4, R.drawable.num5,
            R.drawable.num6, R.drawable.num7, R.drawable.num8, R.drawable.num9, R.drawable.star, R.drawable.num0, R.drawable.hash};
    String []arr = {"1","2","3","4","5","6","7","8","9","*","0","#"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_pad);

        dialPad = (GridView) findViewById(R.id.dialPad);
        callButton = (ImageButton) findViewById(R.id.callButton);
        dialNum = (TextView)findViewById(R.id.dialNum);
        backspace = (ImageView)findViewById(R.id.backspace);
        num = new String();
        //dialPad.setAdapter(new ImageAdapter(this));
        dialPad.setAdapter(new ArrayAdapter<String>(this,R.layout.custom_dial,arr));
        dialList = (ListView)findViewById(R.id.dialList);


        dialNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty())
                    dialList.setVisibility(View.INVISIBLE);
                else
                    dialList.setVisibility(View.VISIBLE);
                ContactCursorAdapter listCursor = new ContactCursorAdapter(Dialer.this,new DBHandler(Dialer.this).selectMobile(s),0);
                dialList.setAdapter(listCursor);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialPad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 9)
                    num+="*";
                else if (position == 10)
                    num+="0";
                else if (position == 11)
                    num+="#";
                else
                    num+=String.valueOf(position+1);
                dialNum.setText(num);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_CALL);
                num = num.replace("#",Uri.encode("#"));

                call.setData(Uri.parse("tel:" + num));
                if (ActivityCompat.checkSelfPermission(Dialer.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
               startActivity(call);
            }
        });

        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                num = new String();
                dialNum.setText(num);
                return true;
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num.length()==0)
                    return;
                num = num.replace(Uri.encode("#"),"#");
                StringBuffer numEdit = new StringBuffer(num.toString());
                numEdit.deleteCharAt(num.length()-1);
                num = numEdit.toString();
                dialNum.setText(num);
            }
        });

    }

    /*public class ImageAdapter extends BaseAdapter
    {
        Context context;

        public ImageAdapter(Context context)
        {
            this.context=context;
        }

        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imgView = new ImageView(context);
            imgView.setImageResource(img[position]);
            imgView.setLayoutParams(new GridView.LayoutParams(240,110));
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imgView;
        }
    }*/
}