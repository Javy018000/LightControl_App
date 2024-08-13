package com.example.lightcontrol_app.Adapter_RecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightcontrol_app.Modelo_RecycleView.CampoInfo;
import com.example.lightcontrol_app.R;

import java.util.List;

public class OrdenServicioVerInfoAdapter extends RecyclerView.Adapter<OrdenServicioVerInfoAdapter.OrderViewHolder> {
    private List<CampoInfo> camposAMostrar;

    public OrdenServicioVerInfoAdapter(List<CampoInfo> camposAMostrar) {
        this.camposAMostrar = camposAMostrar;
    }

    public List<CampoInfo> getCamposAMostrar() {
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
        CampoInfo campo = camposAMostrar.get(position);
        holder.info.setText(campo.getTitulo() + ": " + campo.getValor());
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

