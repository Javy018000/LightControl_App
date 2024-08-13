package com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = findViewById(R.id.toolbarVerPqrs);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        recyclerView = findViewById(R.id.recyclerViewPqrs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarCampos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCampos();
    }

    private void cargarCampos() {
        executorService.execute(() -> {
            BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
            List<VerPqrs> resultadoInsumos = baseDeDatosAux.obtenerDatos("SELECT * FROM pqrs WHERE Estado = 1", resultSet -> new VerPqrs(
                    resultSet.getInt("idpqrs"),
                    resultSet.getDate("FechaRegistro"),
                    resultSet.getString("Tipopqrs"),
                    resultSet.getString("Canal"),
                    resultSet.getString("Nombre"),
                    resultSet.getString("Apellido"),
                    resultSet.getString("TipoDoc"),
                    resultSet.getString("Documento"),
                    resultSet.getString("Correo"),
                    resultSet.getString("Referencia"),
                    resultSet.getString("DireccionAfectacion"),
                    resultSet.getString("BarrioAfectacion"),
                    resultSet.getString("TipoAlumbrado"),
                    resultSet.getString("DescripcionAfectacion"),
                    resultSet.getInt("Estado"),
                    resultSet.getString("Telefono")

            ));
            mainHandler.post(() -> {
                if (adapter == null){
                    adapter = new VerPqrsAdapter(resultadoInsumos);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    adapter.updateData(resultadoInsumos);
                }
            });
        });
    }
}