package com.example.lightcontrol_app.Adapter_RecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lightcontrol_app.Modelo_RecycleView.OrdenServicio;
import com.example.lightcontrol_app.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdenServicioAdapter extends RecyclerView.Adapter<OrdenServicioAdapter.OrderViewHolder>{
    private List<OrdenServicio> orderList;

    public OrdenServicioAdapter(List<OrdenServicio> orderList) {
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
        OrdenServicio order = orderList.get(position);
        holder.tvOrderId.setText("Orden #" + order.getId());
        holder.tvOrderDescription.setText(order.getDescription());
        holder.btnViewMore.setOnClickListener(v -> {
            // Implementar acción para ver más detalles
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDescription;
        Button btnViewMore;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDescription = itemView.findViewById(R.id.tvOrderDescription);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
        }
    }
}
