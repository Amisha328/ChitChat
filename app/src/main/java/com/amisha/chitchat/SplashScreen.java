package com.amisha.chitchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    Button btn;
    Animation animation;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        btn=findViewById(R.id.button);
        animation= AnimationUtils.loadAnimation(this,R.anim.shake);

        pref = getSharedPreferences("Username", Context.MODE_PRIVATE);
        editor = pref.edit();

        btn.setAnimation(animation);
        //remove status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //remove toolbar
        getSupportActionBar().hide();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pref.getString("username_key", null);
                String key=pref.getString("logout_key",null);
                if(name==null && key==null) {
                    Intent i = new Intent(SplashScreen.this, SignInPage.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    i.putExtra("name_key",name);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
