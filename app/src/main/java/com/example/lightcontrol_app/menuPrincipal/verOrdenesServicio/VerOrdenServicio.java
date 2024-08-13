package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.example.lightcontrol_app.Adapter_RecycleView.OrdenServicioAdapter;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicioVistaPrevia;
import com.example.lightcontrol_app.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerOrdenServicio extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrdenServicioAdapter adapter;
    private Button btnMostrarEnMapa;
    private ExecutorService executorService;
    private Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_orden_servicio);

        Toolbar toolbar = findViewById(R.id.toolbarVerOrdenes);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerViewPqrs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        btnMostrarEnMapa = findViewById(R.id.btnMostrarEnMapa_VerOrden);
        btnMostrarEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapaPuntosSimultaneos.class);
                startActivity(intent);
            }
        });

        cargarDatos();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        executorService.execute(() -> {
            BaseDeDatosAux dbHelper = new BaseDeDatosAux();
            List<OrdenServicioVistaPrevia> orders = dbHelper.obtenerDatos("SELECT problema_relacionado, id_orden FROM ordenes_de_servicio WHERE IdEstado = 2",
                    resultSet -> new OrdenServicioVistaPrevia(
                            resultSet.getInt("id_orden"),
                            resultSet.getString("problema_relacionado")
                    ));

            mainHandler.post(() -> {
                if (adapter == null) {
                    adapter = new OrdenServicioAdapter(orders);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    adapter.updateData(orders); // Método personalizado para actualizar los datos en el adaptador
                }
            });
        });
    }
}