package com.example.lightcontrol_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lightcontrol_app.menuPrincipal.MenuPrincipal;

public class Login extends AppCompatActivity {
    Button botonLogin;
    private EditText campContrasena;
    private ImageButton contrasenaVisibilidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botonLogin = findViewById(R.id.buttonLogin);

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                startActivity(intent);
            }
        });
        campContrasena = findViewById(R.id.editTextContraseñaLogin);
        contrasenaVisibilidad = findViewById(R.id.contrasenaVisibilidadLogin);

        contrasenaVisibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campContrasena.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    campContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    contrasenaVisibilidad.setSelected(false);
                } else {
                    campContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    contrasenaVisibilidad.setSelected(true);
                }
                campContrasena.setSelection(campContrasena.getText().length());
            }
        });
    }
}