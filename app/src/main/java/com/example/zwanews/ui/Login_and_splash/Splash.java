package com.example.zwanews.ui.Login_and_splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import com.example.zwanews.MainActivity;
import com.example.zwanews.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    TextView appname;
    LottieAnimationView lottieAnimationView;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialise firebase
        auth = FirebaseAuth.getInstance();


        appname=findViewById(R.id.appename);
        lottieAnimationView=findViewById(R.id.animationView);


        appname.animate().translationY(-1400).setDuration(2700).setStartDelay(0);
        
        lottieAnimationView.animate().translationX(2000).setDuration(2000).setStartDelay(2900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = auth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }
                else {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }



            }
        },3500);


    }






}