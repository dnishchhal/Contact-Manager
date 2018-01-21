package com.ndstudio.contacts;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Nishchhal on 23-Jun-16.
 */
public class splash extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        Drawable backgrounds[] = new Drawable[2];
        Resources res = getResources();
        backgrounds[0] = res.getDrawable(R.drawable.splashscreen);
        backgrounds[1] = res.getDrawable(R.drawable.splashblue);


        TransitionDrawable crossfader = new TransitionDrawable(backgrounds);

        ImageView image = (ImageView)findViewById(R.id.splash);
        image.setImageDrawable(crossfader);

        crossfader.isCrossFadeEnabled();
        crossfader.startTransition(3000);
        final Intent intent = new Intent(this,MainActivity.class);

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },3000);

    }
}
