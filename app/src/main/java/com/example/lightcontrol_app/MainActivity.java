package com.example.lightcontrol_app;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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

        // Obtener la altura de la pantalla
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // Calcular la posición final de la animación
        int finalPositionY = screenHeight / 2 - animationBombillo1.getHeight() / 2;

        // Animar el primer bombillo
        animationBombillo1.animate().translationY(-finalPositionY).setDuration(1000).setStartDelay(700).withEndAction(new Runnable() {
            @Override
            public void run() {
                animationBombillo1.animate().translationY(-finalPositionY).alpha(0f).setDuration(100).setStartDelay(1950);
            }
        });

        // Animar el segundo bombillo
        animationBombillo2.animate().translationY(-finalPositionY).alpha(0f).setDuration(0).setStartDelay(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                animationBombillo2.animate().translationY(-finalPositionY).alpha(1f).setDuration(100).setStartDelay(1800).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        animationBombillo2.animate().translationY(-finalPositionY).alpha(0f).scaleX(3.5f).scaleY(3.5f).setDuration(500).setStartDelay(2000);
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
