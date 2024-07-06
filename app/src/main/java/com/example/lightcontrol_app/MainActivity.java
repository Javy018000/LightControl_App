package com.example.lightcontrol_app;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView animationBombillo1;
    ImageView animationBombillo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animationBombillo1 = findViewById(R.id.imageBombilloAnimation1);
        animationBombillo2 = findViewById(R.id.imageBombilloAnimation2);

        animationBombillo1.animate().translationY(-1650).setDuration(700).setStartDelay(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                animationBombillo1.animate().translationY(-1650).alpha(0f).setDuration(100).setStartDelay(1950);
            }
        });

        animationBombillo2.animate().translationY(-1650).alpha(0f).setDuration(0).setStartDelay(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                animationBombillo2.animate().translationY(-1650).alpha(1f).setDuration(100).setStartDelay(1800).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        animationBombillo2.animate().translationY(-1650).alpha(0f).scaleX(3.5f).scaleY(3.5f).setDuration(500).setStartDelay(2000);
                    }
                });
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        }, 5000);



    }
}