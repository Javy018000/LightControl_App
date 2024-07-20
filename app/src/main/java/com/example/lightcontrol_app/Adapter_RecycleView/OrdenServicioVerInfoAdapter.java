package com.example.lightcontrol_app.Adapter_RecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicioVerInfo;
import com.example.lightcontrol_app.R;

import java.util.List;

public class OrdenServicioVerInfoAdapter extends RecyclerView.Adapter<OrdenServicioVerInfoAdapter.OrderViewHolder>{
    private List<OrdenServicioVerInfo> camposAMostrar;

    public OrdenServicioVerInfoAdapter(List<OrdenServicioVerInfo> camposAMostrar) {
        this.camposAMostrar = camposAMostrar;
    }


    public List<OrdenServicioVerInfo> getCamposAMostrar() {
        return camposAMostrar;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_ver_info, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrdenServicioVerInfo campo = camposAMostrar.get(position);
        System.out.println("son "+ camposAMostrar.size());
        switch (position){
            case 0: holder.info.setText("ID: " + campo.getId());
                break;
            case 1: holder.info.setText("Tipo de Elemento: " + campo.getTipo_de_elemento());
                break;
            case 2: holder.info.setText("Código de Elemento: " + campo.getCodigo_de_elemento());
                break;
            case 3: holder.info.setText("Pqrs Relacionada: " + campo.getElemento_relacionado());
                break;
            case 5: holder.info.setText("Problema Relacionado: " + campo.getDescription());
                break;
            case 6: holder.info.setText("Problema Validado: " + campo.getProblema_validado());
                break;
            case 7: holder.info.setText("Orden Prioridad: " + campo.getOrden_prioridad());
                break;
            case 8: holder.info.setText("Prioridad de Ruta: " + campo.getPrioridad_de_ruta());
                break;
            case 9: holder.info.setText("Fecha a Realizar: " + campo.getFecha_a_realizar());
                break;
            case 10: holder.info.setText("Cuadrilla: " + campo.getCuadrilla());
                break;
            case 11: holder.info.setText("Tipo de Orden: " + campo.getTipo_de_orden());
                break;
            case 12: holder.info.setText("Tipo de Solución: " + campo.getTipo_de_Solucion());
                break;
            case 13: holder.info.setText("Clase de Orden: " + campo.getClase_de_orden());
                break;
            case 14: holder.info.setText("Obra Relacionada: " + campo.getObra_relacionada());
                break;
            case 15: holder.info.setText("Estado " + verificarEstado(campo.getIdEstado()));
                break;
            default: holder.info.setText("Elemento no encontrado");
            }

        }

    private String verificarEstado(int idEstado) {
        switch (idEstado){
            case 1: return "Sin asignar";
            case 2: return "En proceso";
            case 3: return "Cerrada";
            default: return "Error";
        }
    }

    @Override
    public int getItemCount() {
        return camposAMostrar.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView info;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.campoMostradoRecycle);
        }
    }
}
