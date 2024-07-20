package com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.lightcontrol_app.Adapter_RecycleView.VerPqrsAdapter;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Modelo_RecycleView.VerPqrs;
import com.example.lightcontrol_app.R;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerPqrsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerPqrsAdapter adapter;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pqrs);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        recyclerView = findViewById(R.id.recyclerViewPqrs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarCampos();
    }
    private void cargarCampos() {
        executorService.execute(() -> {
            BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
            List<VerPqrs> resultadoInsumos = baseDeDatosAux.obtenerPqrs();

            mainHandler.post(() -> {
                    adapter = new VerPqrsAdapter(resultadoInsumos);
                    recyclerView.setAdapter(adapter);
            });
        });
    }
}