package com.example.lightcontrol_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.lightcontrol_app.Adapter_RecycleView.OrdenServicioAdapter;
import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicio;

import java.util.ArrayList;
import java.util.List;

public class VerOrdenServicio extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrdenServicioAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_orden_servicio);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos desde la base de datos en un hilo separado
        new LoadOrdersTask().execute();
    }
    private class LoadOrdersTask extends AsyncTask<Void, Void, List<OrdenServicio>> {

        @Override
        protected List<OrdenServicio> doInBackground(Void... voids) {
            BaseDeDatosAux dbHelper = new BaseDeDatosAux();
            return dbHelper.getAllOrders();
        }

        @Override
        protected void onPostExecute(List<OrdenServicio> orders) {
            adapter = new OrdenServicioAdapter(orders);
            recyclerView.setAdapter(adapter);
        }
    }
}