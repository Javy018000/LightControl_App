package com.example.lightcontrol_app.menuPrincipal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lightcontrol_app.R;
import com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio.VerPqrsActivity;
import com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio.VerOrdenServicio;

public class MenuPrincipal extends AppCompatActivity {

    private Button viewServiceOrdersButton;
    private Button createServiceOrderButton;
    private Button requestSuppliesButton;
    private Button viewInventoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        viewServiceOrdersButton = findViewById(R.id.viewServiceOrdersButton);
        createServiceOrderButton = findViewById(R.id.createServiceOrderButton);
        requestSuppliesButton = findViewById(R.id.requestSuppliesButton);
        viewInventoryButton = findViewById(R.id.viewInventoryButton);

        viewServiceOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en Ver órdenes de servicio
                Intent intent = new Intent(getApplicationContext(), VerOrdenServicio.class);
                startActivity(intent);
            }
        });

        createServiceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en Crear orden de servicio
                Intent intent = new Intent(getApplicationContext(), VerPqrsActivity.class);
                startActivity(intent);
            }
        });

        requestSuppliesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en Solicitar insumos
            }
        });

        viewInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en Ver insumos en inventario
            }
        });
    }
}