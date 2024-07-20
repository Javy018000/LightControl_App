package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lightcontrol_app.Adapter_RecycleView.OrdenServicioAdapter;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicioVistaPrevia;
import com.example.lightcontrol_app.R;

import java.util.List;

public class VerOrdenServicio extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrdenServicioAdapter adapter;
    private Button btnMostrarEnMapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_orden_servicio);

        recyclerView = findViewById(R.id.recyclerViewPqrs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnMostrarEnMapa = findViewById(R.id.btnMostrarEnMapa_VerOrden);
        btnMostrarEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapaPuntosSimultaneos.class);
                startActivity(intent);
            }
        });

        // Cargar datos desde la base de datos en un hilo separado
        new LoadOrdersTask().execute();
    }
    private class LoadOrdersTask extends AsyncTask<Void, Void, List<OrdenServicioVistaPrevia>> {

        @Override
        protected List<OrdenServicioVistaPrevia> doInBackground(Void... voids) {
            BaseDeDatosAux dbHelper = new BaseDeDatosAux();
            return dbHelper.obtenerTodasLasOrdenes();
        }

        @Override
        protected void onPostExecute(List<OrdenServicioVistaPrevia> orders) {
            adapter = new OrdenServicioAdapter(orders);
            recyclerView.setAdapter(adapter);
        }
    }
}