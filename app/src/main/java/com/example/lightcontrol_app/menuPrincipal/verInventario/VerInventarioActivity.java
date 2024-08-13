package com.example.lightcontrol_app.menuPrincipal.verInventario;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.lightcontrol_app.Adapter_RecycleView.VerInventarioAdapter;
import com.example.lightcontrol_app.Adapter_RecycleView.VerPqrsAdapter;
import com.example.lightcontrol_app.Modelo_RecycleView.VerInventario;
import com.example.lightcontrol_app.R;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerInventarioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerInventarioAdapter adapter;
    private ExecutorService executorService;
    private Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_inventario);

        Toolbar toolbar = findViewById(R.id.toolbarVerInventario);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        recyclerView = findViewById(R.id.recyclerVerInventario);
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
            List<VerInventario> resultadoInventario = baseDeDatosAux.obtenerDatos("SELECT * FROM Inventario", resultSet -> new VerInventario(
                    resultSet.getInt("ID"),
                    resultSet.getString("nombre_elemento"),
                    resultSet.getInt("cantidad"),
                    resultSet.getString("estado"),
                    resultSet.getString("descripcion")
            ));
            mainHandler.post(() -> {
                if (adapter == null){
                    adapter = new VerInventarioAdapter(resultadoInventario);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    adapter.updateData(resultadoInventario);
                }
            });
        });
    }
}