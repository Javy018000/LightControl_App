package com.example.lightcontrol_app.Adapter_RecycleView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio.DetallesOrdenes;
import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicioVistaPrevia;
import com.example.lightcontrol_app.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdenServicioAdapter extends RecyclerView.Adapter<OrdenServicioAdapter.OrderViewHolder>{
    private List<OrdenServicioVistaPrevia> orderList;

    public OrdenServicioAdapter(List<OrdenServicioVistaPrevia> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_ver_orden_servicio, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrdenServicioVistaPrevia order = orderList.get(position);
        holder.ordenId.setText("Orden #" + order.getId());
        holder.orderDescripcion.setText(order.getDescription());
        holder.btnVerMas.setOnClickListener(v -> {

            Context context = v.getContext();
            Intent intent = new Intent(context, DetallesOrdenes.class);
            intent.putExtra("orderId", order.getId());
            intent.putExtra("orderDescription", order.getDescription());
            // Añade más datos
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<OrdenServicioVistaPrevia> orders) {
        this.orderList.clear();
        this.orderList.addAll(orders);
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView ordenId, orderDescripcion;
        Button btnVerMas;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ordenId = itemView.findViewById(R.id.tvOrderId);
            orderDescripcion = itemView.findViewById(R.id.tvOrderDescription);
            btnVerMas = itemView.findViewById(R.id.btnViewMore);

        }
    }
}
