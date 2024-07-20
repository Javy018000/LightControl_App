package com.example.lightcontrol_app.Adapter_RecycleView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightcontrol_app.Modelo_RecycleView.VerPqrs;
import com.example.lightcontrol_app.R;
import com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio.CrearOrdenActivity;
import com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio.InfoPqrActivity;
import com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio.DetallesOrdenes;

import java.util.List;

public class VerPqrsAdapter extends RecyclerView.Adapter<VerPqrsAdapter.OrderViewHolder>{
    private List<VerPqrs> camposAMostrar;

    public VerPqrsAdapter(List<VerPqrs> camposAMostrar) {
        this.camposAMostrar = camposAMostrar;
    }


    public List<VerPqrs> getCamposAMostrar() {
        return camposAMostrar;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_ver_pqrs, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        VerPqrs pqrs = camposAMostrar.get(position);
        holder.textId.setText("Pqr #"+pqrs.getIdpqrs());
        holder.textInfo.setText(pqrs.toStringRecycle());
        holder.buttonVerInfo.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, InfoPqrActivity.class);
            intent.putExtra("VerInfoPqrs", pqrs);
            context.startActivity(intent);
        });
        holder.buttonCrearOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CrearOrdenActivity.class);
                intent.putExtra("CrearOrdenServicioId", pqrs.getIdpqrs());
                intent.putExtra("CrearOrdenServicioDescription", pqrs.getDescripcionAfectacion());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return camposAMostrar.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textId;
        TextView textInfo;
        Button buttonVerInfo;
        Button buttonCrearOrden;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.textRecycleIdPqrs);
            textInfo = itemView.findViewById(R.id.textRecycleInfoPqrs);

            buttonVerInfo = itemView.findViewById(R.id.btnRecycleVerInfoPqrs);
            buttonCrearOrden = itemView.findViewById(R.id.btnRecycleCrearOrdenPqrs);

        }
    }
}
