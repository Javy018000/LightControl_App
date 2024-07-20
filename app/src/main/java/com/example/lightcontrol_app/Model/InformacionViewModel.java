package com.example.lightcontrol_app.Model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicioVerInfo;

import java.util.List;

public class InformacionViewModel extends ViewModel {
    private MutableLiveData<List<OrdenServicioVerInfo>> ordenes;

    public LiveData<List<OrdenServicioVerInfo>> getOrdenes(int idInformacion) {
        if (ordenes == null) {
            ordenes = new MutableLiveData<>();
            loadOrdenes(idInformacion);
        }
        return ordenes;
    }

    private void loadOrdenes(int idInformacion) {
        // Aquí puedes hacer la carga de datos en segundo plano, por ejemplo, usando un AsyncTask
        new AsyncTask<Void, Void, List<OrdenServicioVerInfo>>() {
            @Override
            protected List<OrdenServicioVerInfo> doInBackground(Void... voids) {
                BaseDeDatosAux auxBaseDeDatos = new BaseDeDatosAux();
                return auxBaseDeDatos.obtenerInfoOrden(idInformacion);
            }

            @Override
            protected void onPostExecute(List<OrdenServicioVerInfo> ordenServicioVerInfos) {
                ordenes.setValue(ordenServicioVerInfos);
            }
        }.execute();
    }
}
